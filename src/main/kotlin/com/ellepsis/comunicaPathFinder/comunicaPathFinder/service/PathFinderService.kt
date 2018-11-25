package com.ellepsis.comunicaPathFinder.comunicaPathFinder.service

import org.apache.jena.rdf.model.Model
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service


/**
 * @author ellepsis created on 18-Nov-18.
 */
@Service
class PathFinderService @Autowired constructor(val sparqlQueryExecutor: SparqlQueryExecutor, modelService: ModelService) {

    final var models: List<Model> = modelService.loadAll().filterNotNull()

    fun findPath(source: String, target: String) {
        val pathFinder = PathFinder(3, models, source, target, sparqlQueryExecutor)
        val findPath = pathFinder.findPath()
        println(findPath)
    }

    fun findAll() {
        models.forEach { m ->
            sparqlQueryExecutor.executeQuery(
                    """
                    SELECT ?x ?y ?z
                    WHERE {
                        ?x ?y ?z.
                    }
                    """, m)
        }

    }

}
