package com.example.projekakhir.repository

import com.example.projekakhir.model.AllJenisTerapiResponse
import com.example.projekakhir.model.JenisTerapi
import com.example.projekakhir.service.JenisTerapiService
import java.io.IOException

interface JenisTerapiRepository {
    suspend fun getJenisTerapi(): AllJenisTerapiResponse

    suspend fun insertJenisTerapi(jenisTerapi: JenisTerapi)

    suspend fun updateJenisTerapi(idJenisTerapi: Int, jenisTerapi: JenisTerapi)

    suspend fun deleteJenisTerapi(idJenisTerapi: Int)

    suspend fun getJenisTerapiById(idJenisTerapi: Int): JenisTerapi
}

class NetworkJenisTerapiRepository(
    private val jenisTerapiApiService: JenisTerapiService
) : JenisTerapiRepository {
    override suspend fun getJenisTerapi(): AllJenisTerapiResponse =
        jenisTerapiApiService.getAllJenisTerapi()

    override suspend fun insertJenisTerapi(jenisTerapi: JenisTerapi) {
        jenisTerapiApiService.insertJenisTerapi(jenisTerapi)
    }

    override suspend fun updateJenisTerapi(idJenisTerapi: Int, jenisTerapi: JenisTerapi) {
        jenisTerapiApiService.updateJenisTerapi(idJenisTerapi, jenisTerapi)
    }

    override suspend fun deleteJenisTerapi(idJenisTerapi: Int) {
        try {
            val response = jenisTerapiApiService.deleteJenisTerapi(idJenisTerapi)
            if (!response.isSuccessful) {
                throw IOException(
                    "Failed to delete JenisTerapi. HTTP Status Code: ${response.code()}"
                )
            } else {
                println(response.message())
            }
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun getJenisTerapiById(idJenisTerapi: Int): JenisTerapi {
        return jenisTerapiApiService.getJenisTerapiById(idJenisTerapi).data
    }
}
