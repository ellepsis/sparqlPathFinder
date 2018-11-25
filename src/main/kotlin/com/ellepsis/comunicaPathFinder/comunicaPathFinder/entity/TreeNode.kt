package com.ellepsis.comunicaPathFinder.comunicaPathFinder.entity

import org.apache.jena.rdf.model.Model

/**
 * @author ellepsis created on 25-Nov-18.
 */
data class TreeNode(
        val model: Model,
        val depth: Int,
        val parentResult: ExecutionResult?,
        val executionResults: List<TreeNode>?
) {
    override fun toString(): String {
        return depth.toString() + parentResult.toString() + executionResults.toString()
    }
}
