package com.example.projekakhir.service
import com.example.projekakhir.model.AllTerapisResponse
import com.example.projekakhir.model.Terapis
import com.example.projekakhir.model.TerapisDetailResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
interface TerapisService {
    @Headers(
        "Accept: application/json",
        "Content-Type: application/json",
    )
    @GET(".")
    suspend fun getAllTerapis(): AllTerapisResponse

    @GET("{id_terapis}")
    suspend fun getTerapisById(@Path("id_terapis") idTerapis: Int): TerapisDetailResponse

    @POST("store")
    suspend fun insertTerapis(@Body terapis: Terapis)

    @PUT("{id_terapis}")
    suspend fun updateTerapis(@Path("id_terapis") idTerapis: Int, @Body terapis: Terapis)

    @DELETE("{id_terapis}")
    suspend fun deleteTerapis(@Path("id_terapis") idTerapis: Int): Response<Void>
}
