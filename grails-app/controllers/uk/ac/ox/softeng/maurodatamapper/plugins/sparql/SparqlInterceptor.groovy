package uk.ac.ox.softeng.maurodatamapper.plugins.sparql

import uk.ac.ox.softeng.maurodatamapper.core.traits.controller.MdmInterceptor


class SparqlInterceptor implements MdmInterceptor {

    boolean before() {
        System.err.println("Intercepting!")
        true
    }

    boolean after() {
        System.err.println("Intercepted!")
        true
    }

    void afterView() {
        // no-op
    }
}
