package com.example.projekakhir.repository

import com.example.projekakhir.model.AllSesiResponse
import com.example.projekakhir.model.SesiTerapi
import com.example.projekakhir.service.SesiTerapiService
import java.io.IOException

interface SesiTerapiRepository {
    suspend fun getSesiTerapi(): AllSesiResponse

    suspend fun insertSesiTerapi(sesiTerapi: SesiTerapi)

    suspend fun updateSesiTerapi(idSesi: Int, sesiTerapi: SesiTerapi)

    suspend fun deleteSesiTerapi(idSesi: Int)

    suspend fun getSesiTerapiById(idSesi: Int): SesiTerapi
}

class NetworkSesiTerapiRepository(
    private val sesiTerapiApiService: SesiTerapiService
) : SesiTerapiRepository {
    override suspend fun getSesiTerapi(): AllSesiResponse =
        sesiTerapiApiService.getAllSesiTerapi()

    override suspend fun insertSesiTerapi(sesiTerapi: SesiTerapi) {
        sesiTerapiApiService.insertSesiTerapi(sesiTerapi)
    }

    override suspend fun updateSesiTerapi(idSesi: Int, sesiTerapi: SesiTerapi) {
        sesiTerapiApiService.updateSesiTerapi(idSesi, sesiTerapi)
    }

    override suspend fun deleteSesiTerapi(idSesi: Int) {
        try {
            val response = sesiTerapiApiService.deleteSesiTerapi(idSesi)
            if (!response.isSuccessful) {
                throw IOException(
                    "Failed to delete SesiTerapi. HTTP Status Code: ${response.code()}"
                )
            } else {
                println(response.message())
            }
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun getSesiTerapiById(idSesi: Int): SesiTerapi {
        return sesiTerapiApiService.getSesiTerapiById(idSesi).data
    }
}
