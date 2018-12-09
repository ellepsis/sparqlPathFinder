package com.ellepsis.comunicaPathFinder.comunicaPathFinder.service

import com.ellepsis.comunicaPathFinder.comunicaPathFinder.entity.ExecutionResult
import com.ellepsis.comunicaPathFinder.comunicaPathFinder.entity.ModelWithData
import com.ellepsis.comunicaPathFinder.comunicaPathFinder.entity.TreeNode
import java.util.concurrent.ForkJoinPool
import java.util.concurrent.atomic.AtomicBoolean

/**
 * @author ellepsis created on 25-Nov-18.
 */
class PathFinder(val maxDepth: Int = 3, val models: List<ModelWithData>,
                 val source: String, val target: String,
                 val queryExecutor: SparqlQueryExecutor) {

    val forkJoinPool: ForkJoinPool = ForkJoinPool(8)

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
        val tasks = models.map {
            val function = { createNodeTree(query, it, depth, parentNode) }
            forkJoinPool.submit(function)
        }
        for (task in tasks) {
            val join = task.join()
            if (join != null) {
                return join
            }
        }
        return null;
    }

    private fun executeQuery(depth: Int, executionResult: ExecutionResult, parentNode: TreeNode?): TreeNode? {
        return executeQuery(depth, executionResult.target.asResource().uri, parentNode)
    }

    private fun createNodeTree(query: String, model: ModelWithData, depth: Int, parentNode: TreeNode?): TreeNode? {
        if (isResultFound.get()) {
            return null
        }
        val results = queryExecutor.executeQuery(query, model.model)

        for (result in results) {
            if (result.target.asResource().uri == target) {
                return TreeNode(model.name, depth, result, parentNode)
            }
        }
        val newDepth = depth + 1
        return if (newDepth <= maxDepth) {
            results.map {
                val newParent = TreeNode(model.name, depth, it, parentNode)
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
