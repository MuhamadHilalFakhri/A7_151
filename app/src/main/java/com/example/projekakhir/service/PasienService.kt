package com.example.projekakhir.service

import com.example.projekakhir.model.AllPasienResponse
import com.example.projekakhir.model.Pasien
import com.example.projekakhir.model.PasienDetailResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface PasienService {
    @Headers(
        "Accept: application/json",
        "Content-Type: application/json",
    )
    @GET(".")
    suspend fun getAllPasien(): AllPasienResponse

    @GET("{id_pasien}")
    suspend fun getPasienById(@Path("id_pasien") idPasien: Int): PasienDetailResponse

    @POST("store")
    suspend fun insertPasien(@Body pasien: Pasien)

    @PUT("{id_pasien}")
    suspend fun updatePasien(@Path("id_pasien") idPasien: Int, @Body pasien: Pasien)

    @DELETE("{id_pasien}")
    suspend fun deletePasien(@Path("id_pasien") idPasien: Int): Response<Void>
}
