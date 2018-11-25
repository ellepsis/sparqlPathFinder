package com.ellepsis.comunicaPathFinder.comunicaPathFinder.service

import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.apache.jena.rdf.model.Model
import org.apache.jena.rdf.model.ModelFactory
import org.apache.jena.util.FileManager
import org.springframework.stereotype.Service

/**
 * @author ellepsis created on 25-Nov-18.
 */
@Service
class ModelService {
    val pathes: Array<String> = arrayOf(
//            "C:\\Users\\ellepsis\\Downloads\\go.owl",
            "C:\\Users\\ellepsis\\Downloads\\dcatap.rdf"
    )

    fun loadAll(): List<Model?> {
        return runBlocking {
            pathes.map { m ->
                async { loadModel(m) }
            }.map { deferred ->
                deferred.await()
            }
        }
    }

    suspend fun loadModel(path: String): Model? {
        val model = ModelFactory.createDefaultModel()
        val fileStream = FileManager.get().open(path)
        fileStream.use { i ->
            model.read(i, null)
        }
        return model
    }
}