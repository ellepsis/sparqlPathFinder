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

    @Value("\${app.modelFile.paths}")
    var pathes: Array<String> = emptyArray()
    @Value("\${app.remoteService.paths}")
    var remoteUrls: Array<String> = emptyArray()

    fun loadAll(): List<ModelWithData> {
        val models = runBlocking {
            pathes.map { m ->
                GlobalScope.async { loadModel(m) }
            }.map { deferred ->
                deferred.await()
            }.toMutableList()
        }
        models.addAll(remoteUrls.map { ModelWithData(null, it, true) })
        return models
    }

    suspend fun loadModel(path: String): ModelWithData {
        val model = ModelFactory.createDefaultModel()
        val fileStream = FileManager.get().open(path)
        fileStream.use { i ->
            model.read(i, null)
        }
        return ModelWithData(model, path, false)
    }
}