package com.ellepsis.comunicaPathFinder.comunicaPathFinder.service

import com.ellepsis.comunicaPathFinder.comunicaPathFinder.entity.ModelWithData
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.apache.jena.rdf.model.ModelFactory
import org.apache.jena.util.FileManager
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

/**
 * @author ellepsis created on 25-Nov-18.
 */
@Service
class ModelService {

    @Value("\${app.files.paths}")
    var pathes: Array<String> = emptyArray()
//    val pathes: Array<String> = arrayOf(
////            "C:\\Users\\ellepsis\\Downloads\\go.owl",
//
//    )

    fun loadAll(): List<ModelWithData> {
        return runBlocking {
            pathes.map { m ->
                GlobalScope.async { loadModel(m) }
            }.map { deferred ->
                deferred.await()
            }
        }
    }

    suspend fun loadModel(path: String): ModelWithData {
        val model = ModelFactory.createDefaultModel()
        val fileStream = FileManager.get().open(path)
        fileStream.use { i ->
            model.read(i, null)
        }
        return ModelWithData(model, path)
    }
}