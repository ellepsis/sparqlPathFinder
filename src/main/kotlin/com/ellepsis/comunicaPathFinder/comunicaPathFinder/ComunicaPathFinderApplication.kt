package com.ellepsis.comunicaPathFinder.comunicaPathFinder

import com.ellepsis.comunicaPathFinder.comunicaPathFinder.service.ModelService
import com.ellepsis.comunicaPathFinder.comunicaPathFinder.service.PathFinderService
import com.ellepsis.comunicaPathFinder.comunicaPathFinder.service.SparqlQueryExecutor
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class ComunicaPathFinderApplication {

}

fun main(args: Array<String>) {
    PathFinderService(SparqlQueryExecutor(), ModelService())
            .findPath("https://www.wikidata.org/about#liveDataLDn3", "http://www.w3.org/ns/dcat#Distribution")
//    runApplication<ComunicaPathFinderApplication>(*args)
}
