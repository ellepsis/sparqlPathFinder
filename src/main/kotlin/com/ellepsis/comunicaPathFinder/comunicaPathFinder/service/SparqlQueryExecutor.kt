package com.ellepsis.comunicaPathFinder.comunicaPathFinder.service

import com.ellepsis.comunicaPathFinder.comunicaPathFinder.entity.ExecutionResult
import org.apache.jena.query.QueryExecutionFactory
import org.apache.jena.query.QueryFactory
import org.apache.jena.rdf.model.Model
import org.apache.jena.shared.Lock
import org.springframework.stereotype.Service


/**
 * @author ellepsis created on 17-Nov-18.
 */
@Service
class SparqlQueryExecutor {

    fun executeQuery(query: String, model: Model): ArrayList<ExecutionResult> {
        println("executing on thread ${Thread.currentThread().name}\t query $query")
        val request = QueryFactory.create(query)
        model.enterCriticalSection(Lock.READ)
        try {
            QueryExecutionFactory.create(request, model).use { qexec ->
                val results = qexec.execSelect()
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
        } finally {
            model.leaveCriticalSection()
        }
    }


}