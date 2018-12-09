package com.ellepsis.comunicaPathFinder.comunicaPathFinder.entity

/**
 * @author ellepsis created on 25-Nov-18.
 */
data class TreeNode(
        val model: String,
        val depth: Int,
        val parentResult: ExecutionResult?,
        val parentNode: TreeNode?
) {

    override fun toString(): String {
        return """
${parentNode?.toString() ?: ""}
Depth: $depth
 ${parentResult.toString()}
->
"""
    }
}
