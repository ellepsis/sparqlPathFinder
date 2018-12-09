package com.ellepsis.comunicaPathFinder.comunicaPathFinder.entity

import com.ellepsis.comunicaPathFinder.comunicaPathFinder.config.CustomSerializer
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import org.apache.jena.rdf.model.RDFNode

/**
 * @author ellepsis created on 25-Nov-18.
 */
data class ExecutionResult(@JsonSerialize(using = CustomSerializer::class) val subject: RDFNode,
                           @JsonSerialize(using = CustomSerializer::class) val relation: RDFNode,
                           @JsonSerialize(using = CustomSerializer::class) val target: RDFNode) {
    override fun toString(): String {
        return "$subject \n $relation \n $target"
    }

}