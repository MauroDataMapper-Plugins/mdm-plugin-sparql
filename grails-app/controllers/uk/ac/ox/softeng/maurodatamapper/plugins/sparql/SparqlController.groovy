package uk.ac.ox.softeng.maurodatamapper.plugins.sparql

import uk.ac.ox.softeng.maurodatamapper.core.traits.controller.ResourcelessMdmController
import uk.ac.ox.softeng.maurodatamapper.core.traits.controller.UserSecurityPolicyManagerAware

import io.micronaut.http.HttpStatus
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy

import javax.servlet.http.HttpServletResponse


class SparqlController implements ResourcelessMdmController {
	static responseFormats = ['json', 'xml']

    SparqlService sparqlService



    def sparql() {

//        System.err.println(applicationContext.dataSource)
//        System.err.println(((TransactionAwareDataSourceProxy) applicationContext.dataSource).getConnection().clientInfo)


        try {

            String query = ''
            if(params['query']) {
                query = params['query']
            } else {
                query = request.reader.text
            }
            //System.err.println("Query: " + query)
            String accept = request.getHeader("Accept")
            sparqlService.runQuery(currentUserSecurityPolicyManager, response.outputStream, query, accept)

            response.status = HttpServletResponse.SC_OK
            response.contentType = 'application/json;charset=UTF-8'


            //response.setHeader "Content-disposition", "attachment; filename=rcCandidate.csv"

        } catch (Exception e) {
            e.printStackTrace()
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage())
        }
        response.outputStream.flush()
        response.outputStream.close()

        //respond(view: "sparql/response.gson")
    }

}
