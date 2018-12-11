package com.ellepsis.comunicaPathFinder.comunicaPathFinder.service

import com.ellepsis.comunicaPathFinder.comunicaPathFinder.entity.ExecutionResult
import com.ellepsis.comunicaPathFinder.comunicaPathFinder.entity.ModelWithData
import org.apache.jena.query.QueryExecutionFactory
import org.apache.jena.query.QueryFactory
import org.apache.jena.query.ResultSet
import org.apache.jena.rdf.model.Model
import org.apache.jena.shared.Lock
import org.springframework.stereotype.Service


/**
 * @author ellepsis created on 17-Nov-18.
 */
@Service
class SparqlQueryExecutor {

    fun executeQuery(query: String, modelWithData: ModelWithData): ArrayList<ExecutionResult> {
        if (modelWithData.isRemote) {
            return executeRemoteQuery(query, modelWithData.name)
        } else {
            return executeLocalQuery(query, modelWithData.model!!)
        }
    }

    fun executeRemoteQuery(query: String, url: String): ArrayList<ExecutionResult> {
        println("executing on thread ${Thread.currentThread().name}\t query $query")
        QueryExecutionFactory.sparqlService(url, query).use { qexec ->
            val results = qexec.execSelect()
            return parseResponse(results)
        }
    }

    fun executeLocalQuery(query: String, model: Model): ArrayList<ExecutionResult> {
        println("executing on thread ${Thread.currentThread().name}\t query $query")
        val request = QueryFactory.create(query)
        model.enterCriticalSection(Lock.READ)
        try {
            QueryExecutionFactory.create(request, model).use { qexec ->
                val results = qexec.execSelect()
                return parseResponse(results)
            }
        } finally {
            model.leaveCriticalSection()
        }
    }

    private fun parseResponse(results: ResultSet): ArrayList<ExecutionResult> {
        val res: ArrayList<ExecutionResult> = ArrayList()
        while (results.hasNext()) {
            val solution = results.nextSolution()
            val source = solution.get("s")!!
            val relation = solution.get("r")!!
            val target = solution.get("t")!!
            res.add(ExecutionResult(source, relation, target))
        }
        return res
    }
}