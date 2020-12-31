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
