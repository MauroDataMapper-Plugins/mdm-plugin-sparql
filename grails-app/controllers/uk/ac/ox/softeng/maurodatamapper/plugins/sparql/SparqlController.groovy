package uk.ac.ox.softeng.maurodatamapper.plugins.sparql

import uk.ac.ox.softeng.maurodatamapper.core.traits.controller.ResourcelessMdmController


class SparqlController implements ResourcelessMdmController {
	static responseFormats = ['json', 'xml']
	

    def sparql() {
        System.err.println("Sparqly sparql")
        respond(view: "sparql/response.gson")
    }

}
