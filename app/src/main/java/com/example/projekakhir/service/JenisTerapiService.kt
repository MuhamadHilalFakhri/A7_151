package com.example.projekakhir.service

import com.example.projekakhir.model.AllJenisTerapiResponse
import com.example.projekakhir.model.JenisTerapi
import com.example.projekakhir.model.JenisTerapiDetailResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface JenisTerapiService {
    @Headers(
        "Accept: application/json",
        "Content-Type: application/json",
    )
    @GET(".")
    suspend fun getAllJenisTerapi(): AllJenisTerapiResponse

    @GET("{id_jenis_terapi}")
    suspend fun getJenisTerapiById(@Path("id_jenis_terapi") idJenisTerapi: Int): JenisTerapiDetailResponse

    @POST("store")
    suspend fun insertJenisTerapi(@Body jenisTerapi: JenisTerapi)

    @PUT("{id_jenis_terapi}")
    suspend fun updateJenisTerapi(
        @Path("id_jenis_terapi") idJenisTerapi: Int,
        @Body jenisTerapi: JenisTerapi
    )

    @DELETE("{id_jenis_terapi}")
    suspend fun deleteJenisTerapi(@Path("id_jenis_terapi") idJenisTerapi: Int): Response<Void>
}
