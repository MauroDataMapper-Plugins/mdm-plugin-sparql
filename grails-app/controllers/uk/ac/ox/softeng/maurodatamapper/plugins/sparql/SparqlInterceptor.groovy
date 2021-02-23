package uk.ac.ox.softeng.maurodatamapper.plugins.sparql

import uk.ac.ox.softeng.maurodatamapper.core.traits.controller.MdmInterceptor


class SparqlInterceptor implements MdmInterceptor {

    boolean before() {
        true
    }

    boolean after() {
        true
    }

    void afterView() {
        // no-op
    }
}
