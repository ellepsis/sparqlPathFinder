package com.ellepsis.comunicaPathFinder.comunicaPathFinder.service

import com.ellepsis.comunicaPathFinder.comunicaPathFinder.entity.ModelWithData
import com.ellepsis.comunicaPathFinder.comunicaPathFinder.entity.TreeNode
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.Duration
import java.time.LocalDateTime


/**
 * @author ellepsis created on 18-Nov-18.
 */
@Service
class PathFinderService @Autowired constructor(val sparqlQueryExecutor: SparqlQueryExecutor, modelService: ModelService) {

    final var models: List<ModelWithData> = modelService.loadAll()

    fun findPath(source: String, target: String, maxDepth: Int): TreeNode? {
        if (maxDepth < 1){
            throw IllegalArgumentException("Minimum depth is 1");
        }
        val pathFinder = PathFinder(5, models, source, target, sparqlQueryExecutor)
        val now = LocalDateTime.now()
        val foundPath = pathFinder.findPath()
        println("Elapsed time: ${Duration.between(now, LocalDateTime.now()).toMillis()} ms")
        return foundPath
    }

    fun findAll() {
        models.forEach { m ->
            sparqlQueryExecutor.executeQuery(
                    """
                    SELECT ?x ?y ?z
                    WHERE {
                        ?x ?y ?z.
                    }
                    """, m.model)
        }

    }

}
