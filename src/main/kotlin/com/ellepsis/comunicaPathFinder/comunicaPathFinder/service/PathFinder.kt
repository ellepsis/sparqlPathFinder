package com.ellepsis.comunicaPathFinder.comunicaPathFinder.service

import com.ellepsis.comunicaPathFinder.comunicaPathFinder.entity.ExecutionResult
import com.ellepsis.comunicaPathFinder.comunicaPathFinder.entity.TreeNode
import org.apache.jena.rdf.model.Model
import java.util.concurrent.atomic.AtomicBoolean

/**
 * @author ellepsis created on 25-Nov-18.
 */
class PathFinder(val maxDepth: Int = 3, val models: List<Model>,
                 val source: String, val target: String,
                 val queryExecutor: SparqlQueryExecutor) {

    var isResultFound: AtomicBoolean = AtomicBoolean(false)

    fun findPath() = executeQuery(1, source)

    private fun executeQuery(depth: Int, source: String): TreeNode? {
        val query = """
                    SELECT ?s ?r ?t
                    WHERE {
                        ?s ?r ?t
                        FILTER (?s = <$source> && !isLiteral(?t)).
                    }
                    """
        return models.map {
            createNodeTree(query, it, depth, null)
        }.filterNotNull().firstOrNull()
    }

    private fun executeQuery(depth: Int, executionResult: ExecutionResult): TreeNode? {
        return executeQuery(depth, executionResult.target.asResource().uri)
    }

    private fun createNodeTree(query: String, model: Model, depth: Int, executionResult: ExecutionResult?): TreeNode? {
        if (isResultFound.get()) {
            return null
        }
        val results = queryExecutor.executeQuery(query, model)

        if (depth > 1 && results.stream().anyMatch { it.target.asResource().uri == target }) {
            isResultFound.set(true)
            return TreeNode(model, depth, executionResult, null)
        } else {
            val newDepth = depth + 1
            return if (newDepth < maxDepth) {
                results.map {
                    executeQuery(newDepth, it)
                }.filterNotNull().firstOrNull()
            } else {
                null
            }
        }
    }

//    fun executeTerminalQuery(source: String, target: String) {
//        val query = """
//                    SELECT ?s ?r ?t
//                    WHERE {
//                        ?s ?r ?t
//                        FILTER (?t = $target && ?s = $source).
//                    }
//                    """
//        models.filterNotNull().map { m ->
//            val executeQuery = sparqlQueryExecutor.executeQuery(query, m)
//            executeQuery.forEach { println(it) }
//        }
//
//    }
}
