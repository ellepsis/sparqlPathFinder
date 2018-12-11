package com.ellepsis.comunicaPathFinder.comunicaPathFinder.entity

import org.apache.jena.rdf.model.Model

/**
 * @author Ilya Mikhailov (Ilya.Mikhailov@lanit-tercom.com) created on 09-Dec-18.
 */
data class ModelWithData(val model: Model?, val name: String, val isRemote: Boolean)
