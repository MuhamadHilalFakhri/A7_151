package com.example.projekakhir.service
import com.example.projekakhir.model.AllSesiResponse
import com.example.projekakhir.model.SesiTerapi
import com.example.projekakhir.model.SesiDetailResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path


interface SesiTerapiService {
    @Headers(
        "Accept: application/json",
        "Content-Type: application/json",
    )
    @GET(".")
    suspend fun getAllSesiTerapi(): AllSesiResponse

    @GET("{id_sesi}")
    suspend fun getSesiTerapiById(@Path("id_sesi") idSesi: Int): SesiDetailResponse

    @POST("store")
    suspend fun insertSesiTerapi(@Body sesi: SesiTerapi)

    @PUT("{id_sesi}")
    suspend fun updateSesiTerapi(@Path("id_sesi") idSesi: Int, @Body sesi: SesiTerapi)

    @DELETE("{id_sesi}")
    suspend fun deleteSesiTerapi(@Path("id_sesi") idSesi: Int): Response<Void>
}
