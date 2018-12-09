package com.ellepsis.comunicaPathFinder.comunicaPathFinder.config

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import org.apache.jena.rdf.model.RDFNode

class CustomSerializer @JvmOverloads constructor(t: Class<RDFNode>? = null) : StdSerializer<RDFNode>(t) {
    override fun serialize(rdfNode: RDFNode?, jsonGenerator: JsonGenerator?, serializerProvider: SerializerProvider?) {
        jsonGenerator!!.writeString(rdfNode!!.asResource().uri)
    }
}