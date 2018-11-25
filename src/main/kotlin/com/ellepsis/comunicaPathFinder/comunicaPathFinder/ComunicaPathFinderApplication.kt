package com.ellepsis.comunicaPathFinder.comunicaPathFinder

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ComunicaPathFinderApplication {

}

fun main(args: Array<String>) {
//    PathFinderService(SparqlQueryExecutor(), ModelService())
//            .findPath("http://rdf.adalab-project.org/ontology/adalab/biochemicalEntity", "http://purl.obolibrary.org/obo/BFO_0000002")
    runApplication<ComunicaPathFinderApplication>(*args)
}
