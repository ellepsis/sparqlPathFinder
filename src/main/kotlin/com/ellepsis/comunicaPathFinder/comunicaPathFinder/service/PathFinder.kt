package com.ellepsis.comunicaPathFinder.comunicaPathFinder.service

import com.ellepsis.comunicaPathFinder.comunicaPathFinder.entity.ExecutionResult
import com.ellepsis.comunicaPathFinder.comunicaPathFinder.entity.TreeNode
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.apache.jena.rdf.model.Model
import java.util.concurrent.atomic.AtomicBoolean

/**
 * @author ellepsis created on 25-Nov-18.
 */
class PathFinder(val maxDepth: Int = 3, val models: List<Model>,
                 val source: String, val target: String,
                 val queryExecutor: SparqlQueryExecutor) {

    var isResultFound: AtomicBoolean = AtomicBoolean(false)

    fun findPath() = executeQuery(1, source, null)

    private fun executeQuery(depth: Int, source: String, parentNode: TreeNode?): TreeNode? {
        val query = """
                    SELECT ?s ?r ?t
                    WHERE {
                        ?s ?r ?t
                        FILTER (?s = <$source> && !isLiteral(?t)).
                    }
                    """
//        FILTER (?s = <$source> && !isLiteral(?t)).
        return runBlocking {
            val deferredMap = models.map {
                GlobalScope.async { createNodeTree(query, it, depth, parentNode) }
            }
            deferredMap.map { it.await() }
        }.filterNotNull().firstOrNull()
    }

    private fun executeQuery(depth: Int, executionResult: ExecutionResult, parentNode: TreeNode?): TreeNode? {
        return executeQuery(depth, executionResult.target.asResource().uri, parentNode)
    }

    private fun createNodeTree(query: String, model: Model, depth: Int, parentNode: TreeNode?): TreeNode? {
        if (isResultFound.get()) {
            return null
        }
        val results = queryExecutor.executeQuery(query, model)

        for (result in results) {
            if (result.target.asResource().uri == target) {
                return TreeNode(model, depth, result, parentNode)
            }
        }
        val newDepth = depth + 1
        return if (newDepth < maxDepth) {
            results.map {
                val newParent = TreeNode(model, depth, it, parentNode)
                executeQuery(newDepth, it, newParent)
            }.filterNotNull().firstOrNull()
        } else {
            null
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
