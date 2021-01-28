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

import grails.testing.web.interceptor.InterceptorUnitTest
import spock.lang.Specification

class SparqlInterceptorSpec extends Specification implements InterceptorUnitTest<SparqlInterceptor> {

    def setup() {
    }

    def cleanup() {

    }

    void "Test sparql interceptor matching"() {
        when:"A request matches the interceptor"
        withRequest(controller:"sparql")

        then:"The interceptor does match"
        interceptor.doesMatch()
    }
}
