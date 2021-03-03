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

import uk.ac.ox.softeng.maurodatamapper.test.unit.BaseUnitSpec

import grails.testing.services.ServiceUnitTest
import org.springframework.beans.factory.annotation.Autowired
import spock.lang.Specification

class SparqlServiceSpec extends BaseUnitSpec implements ServiceUnitTest<SparqlService>{


    def setup() {
        mockArtefact(SparqlService)
    }

    def cleanup() {
    }

    void "test contents of mapping file"() {
        expect:"mapping file contains prefix"
            service.mappingFile.contains("@prefix mdm:   <http://metadata-catalogue.org/> .")

        and:"mapping file contains datamodel mapping"
        service.mappingFile.contains("map:datamodel_data_model")
    }
}
