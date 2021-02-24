package uk.ac.ox.softeng.maurodatamapper.plugins.sparql

import uk.ac.ox.softeng.maurodatamapper.core.model.ModelService
import uk.ac.ox.softeng.maurodatamapper.core.traits.controller.UserSecurityPolicyManagerAware
import uk.ac.ox.softeng.maurodatamapper.datamodel.DataModel
import uk.ac.ox.softeng.maurodatamapper.datamodel.DataModelService
import uk.ac.ox.softeng.maurodatamapper.security.UserSecurityPolicyManager

import com.hp.hpl.jena.query.Query
import com.hp.hpl.jena.query.QueryExecutionFactory
import com.hp.hpl.jena.query.QueryFactory
import com.hp.hpl.jena.query.ResultSet
import com.hp.hpl.jena.query.ResultSetFormatter

/*import com.hp.hpl.jena.query.Query
import com.hp.hpl.jena.query.QueryExecutionFactory
import com.hp.hpl.jena.query.QueryFactory
import com.hp.hpl.jena.query.QuerySolution
import com.hp.hpl.jena.query.ResultSet
import com.hp.hpl.jena.query.ResultSetFormatter
*/

import de.fuberlin.wiwiss.d2rq.jena.ModelD2RQ
import grails.core.GrailsApplication
import grails.gorm.transactions.Transactional
import grails.io.IOUtils
import org.springframework.beans.factory.annotation.Autowired

import java.nio.charset.StandardCharsets
import java.nio.file.Paths
import javax.sql.DataSource

@Transactional

class SparqlService {

    DataSource dataSource
    GrailsApplication grailsApplication

    @Autowired(required = false)
    DataModelService dataModelService

    String mappingFile = readMappingFile()

    static final String[] mappingFileComponentNames = [
            "prefix",
            "datamodel",
            "dataclass",
            "dataelement"]

    void runQuery(UserSecurityPolicyManager userSecurityPolicyManager, OutputStream responseStream, String query, String accept) {

        String instantiatedMapping = instantiateMapping(userSecurityPolicyManager)

        File.createTempFile("temp",".ttl").with {tempFile ->
            deleteOnExit()  // Including this line will cause the temp file to be deleted automatically

            write instantiatedMapping    // write to the temp file

            ModelD2RQ m = new ModelD2RQ(tempFile.path, null, 'http://metadata-catalogue.org/')

            Query q = QueryFactory.create(query)
            ResultSet rs = QueryExecutionFactory.create(q, m).execSelect()
            if(accept == "application/sparql-results+xml" || accept == "xml" || accept == "application/xml") {
                ResultSetFormatter.outputAsXML(responseStream, rs)
            } else if (accept == "text/csv" || accept == "csv") {
                ResultSetFormatter.outputAsCSV(responseStream, rs)
            //} else if (accept == "application/rdf+xml " || accept == "rdf") {
            //    ResultSetFormatter.outputAsRDF(responseStream, rs)
            } else {
                ResultSetFormatter.outputAsJSON(responseStream, rs)
            }

            m.close()
        }
    }

    String readFileString(InputStream inputStream) {
        IOUtils.toString(inputStream,"UTF-8");
    }


    String getIdList(UserSecurityPolicyManager userSecurityPolicyManager, Class clazz) {

        List<String> ids = userSecurityPolicyManager.listReadableSecuredResourceIds(clazz).collect {
            """'${it.toString()}'""".toString()
        }
        // dummy value in case of empty list
        ids.add('null')
        return ids.join(",")

    }


    String instantiateMapping(UserSecurityPolicyManager userSecurityPolicyManager) {
        String password = grailsApplication.config.getProperty("environments.development.dataSource.password")
        String username = grailsApplication.config.getProperty("environments.development.dataSource.username")
        String jdbcUrl = dataSource.getConnection().getMetaData().getURL()

        String dataModelIds = getIdList(userSecurityPolicyManager, DataModel.class)

        String instantiatedMapping = mappingFile

        Map<String, String> replacements = [
                '''JDBC_URI''': jdbcUrl,
                '''USERNAME''': username,
                '''PASSWORD''': password,
                '''DATAMODEL_IDS''': dataModelIds
        ]
        replacements.entrySet().each {
            String regex = "\\\$\\{${it.key}}"
            instantiatedMapping = instantiatedMapping.replaceAll(regex, it.value)
        }
        return instantiatedMapping
    }

    String readMappingFile() {
        mappingFileComponentNames.collect{fileName ->
            String ttlFilename = "${fileName}.ttl"
            InputStream is = SparqlService.getClassLoader().getResourceAsStream(ttlFilename)
            String fileContents = readFileString(is)
            //is.close()
            fileContents
        }.join("\n")
    }
}
