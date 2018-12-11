package com.ellepsis.comunicaPathFinder.comunicaPathFinder.service

import com.ellepsis.comunicaPathFinder.comunicaPathFinder.entity.ExecutionResult
import com.ellepsis.comunicaPathFinder.comunicaPathFinder.entity.ModelWithData
import com.ellepsis.comunicaPathFinder.comunicaPathFinder.entity.TreeNode
import java.util.concurrent.ForkJoinPool
import java.util.concurrent.ForkJoinTask
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger

/**
 * @author ellepsis created on 25-Nov-18.
 */
class PathFinder(val maxDepth: Int = 3, val models: List<ModelWithData>,
                 val source: String, val target: String,
                 val queryExecutor: SparqlQueryExecutor) {

    val forkJoinPool: ForkJoinPool = ForkJoinPool(32)
    val localForkJoinPool: ForkJoinPool = ForkJoinPool()

    var isResultFound: AtomicBoolean = AtomicBoolean(false)
    val currentLevel: AtomicInteger = AtomicInteger()
    fun findPath() = executeQuery(1, source, null)

    private fun executeQuery(depth: Int, source: String, parentNode: TreeNode?): TreeNode? {
        val query = """
                    SELECT ?s ?r ?t
                    WHERE {
                        ?s ?r ?t
                        FILTER (?s = <$source> && isIRI(?t)).
                    }
                    """
//        FILTER (?s = <$source> && !isLiteral(?t)).
        val localTasks = mutableListOf<ForkJoinTask<TreeNode?>>()
        val remoteTasks = mutableListOf<ForkJoinTask<TreeNode?>>()
        for (model in models) {
            val function = { createNodeTree(query, model, depth, parentNode) }
            if (model.isRemote) {
                remoteTasks.add(forkJoinPool.submit(function))
            } else {
                localTasks.add(localForkJoinPool.submit(function))
            }
        }
        for (task in localTasks) {
            val join = task.join()
            if (join != null) {
                isResultFound.set(true)
                return join
            }
        }
        for (task in remoteTasks) {
            val join = task.join()
            if (join != null) {
                isResultFound.set(true)
                return join
            }
        }
        return null
    }

    private fun executeQuery(depth: Int, executionResult: ExecutionResult, parentNode: TreeNode?): TreeNode? {
        return executeQuery(depth, executionResult.target.asResource().uri, parentNode)
    }

    private fun createNodeTree(query: String, model: ModelWithData, depth: Int, parentNode: TreeNode?): TreeNode? {
        if (isResultFound.get()) {
            return null
        }
        val results = queryExecutor.executeQuery(query, model)

        for (result in results) {
            if (result.target.asResource().uri == target) {
                return TreeNode(model.name, depth, result, parentNode)
            }
        }
        val newDepth = depth + 1
        return if (newDepth <= maxDepth && !results.isEmpty()) {
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
