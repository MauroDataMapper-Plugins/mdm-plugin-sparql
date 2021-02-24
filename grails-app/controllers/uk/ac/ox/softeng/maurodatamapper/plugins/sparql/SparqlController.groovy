/*
 * Copyright 2020 University of Oxford and Health and Social Care Information Centre, also known as NHS Digital
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

import uk.ac.ox.softeng.maurodatamapper.core.traits.controller.ResourcelessMdmController

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
