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

import uk.ac.ox.softeng.maurodatamapper.core.traits.controller.ResourcelessMdmController

import groovy.util.logging.Slf4j

import javax.servlet.http.HttpServletResponse

@Slf4j
class SparqlController implements ResourcelessMdmController {

    SparqlService sparqlService

    def sparql() {

        try {
            String query
            if(params['query']) {
                query = params['query']
            } else {
                query = request.reader.text
            }
            log.info("Query: " + query)
            String accept = request.getHeader("Accept")
            sparqlService.runQuery(currentUserSecurityPolicyManager, response.outputStream, query, accept)

            response.status = HttpServletResponse.SC_OK
            response.contentType = 'application/json;charset=UTF-8'

        } catch (Exception e) {
            log.error("Error handling sparql", e)
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage())
        }
        response.outputStream.flush()
        response.outputStream.close()
    }

}
