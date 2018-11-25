package com.ellepsis.comunicaPathFinder.comunicaPathFinder.entity

import org.apache.jena.rdf.model.RDFNode

/**
 * @author ellepsis created on 25-Nov-18.
 */
data class ExecutionResult(val subject: RDFNode, val relation: RDFNode, val target: RDFNode){
    override fun toString(): String {
        return "$subject \n $relation \n $target"
    }
}