/*
 * Copyright 2020-2022 University of Oxford and Health and Social Care Information Centre, also known as NHS Digital
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package uk.ac.ox.softeng.maurodatamapper.plugins.sparql

import uk.ac.ox.softeng.maurodatamapper.datamodel.DataModel
import uk.ac.ox.softeng.maurodatamapper.datamodel.DataModelService
import uk.ac.ox.softeng.maurodatamapper.security.UserSecurityPolicyManager
import uk.ac.ox.softeng.maurodatamapper.terminology.Terminology

import com.hp.hpl.jena.query.Query
import com.hp.hpl.jena.query.QueryExecutionFactory
import com.hp.hpl.jena.query.QueryFactory
import com.hp.hpl.jena.query.ResultSet
import com.hp.hpl.jena.query.ResultSetFormatter
import de.fuberlin.wiwiss.d2rq.jena.ModelD2RQ
import grails.core.GrailsApplication
import grails.gorm.transactions.Transactional
import grails.io.IOUtils
import org.springframework.beans.factory.annotation.Autowired

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
            "dataelement",
            "datatype",
            "enumerationvalue",
            "terminology",
            "term"]

    void runQuery(UserSecurityPolicyManager userSecurityPolicyManager, OutputStream responseStream, String query, String accept) {
        try {
            String instantiatedMapping = instantiateMapping(userSecurityPolicyManager)
            File.createTempFile("temp", ".ttl").with { tempFile ->
                deleteOnExit()  // Including this line will cause the temp file to be deleted automatically

                write instantiatedMapping    // write to the temp file
                ModelD2RQ m = new ModelD2RQ(tempFile.path, null, 'http://metadata-catalogue.org/')

                Query q = QueryFactory.create(query)
                ResultSet rs = QueryExecutionFactory.create(q, m).execSelect()
                if (accept == "application/sparql-results+xml" || accept == "xml" || accept == "application/xml") {
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
        } catch (Exception e) {
            e.printStackTrace(System.err)
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
        String username = grailsApplication.config.getProperty("dataSource.username")
        String password = grailsApplication.config.getProperty("dataSource.password")
        String jdbcUrl = dataSource.getConnection().getMetaData().getURL()

        String dataModelIds = getIdList(userSecurityPolicyManager, DataModel.class)?:""
        String terminologyIds = getIdList(userSecurityPolicyManager, Terminology.class)?:""

        String instantiatedMapping = mappingFile

        Map<String, String> replacements = [
                '''JDBC_URI''': jdbcUrl,
                '''USERNAME''': username,
                '''PASSWORD''': password,
                '''DATAMODEL_IDS''': dataModelIds,
                '''TERMINOLOGY_IDS''': terminologyIds
        ]
        replacements.entrySet().each {
            String regex = "\\\$\\{${it.key}}"
            instantiatedMapping = instantiatedMapping.replaceAll(regex, it.value)
        }
        return instantiatedMapping
    }

    String readMappingFile() {
        try {
            String result =  mappingFileComponentNames.collect{fileName ->
                String ttlFilename = "${fileName}.ttl"
                InputStream is = getClass().classLoader.getResourceAsStream(ttlFilename)
                String fileContents = readFileString(is)
                is.close()
                fileContents
            }.join("\n")
            return result
        } catch (Exception e) {
            e.printStackTrace(System.err)
        }
    }
}
