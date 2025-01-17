package com.example.projekakhir.ui

import com.example.projekakhir.repository.NetworkPasienRepository
import com.example.projekakhir.repository.PasienRepository
import com.example.projekakhir.service.PasienService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit


interface AppContainer {
    val pasienRepository: PasienRepository
}

class TerapiContainer: AppContainer {
    private val baseUrl = "http://10.0.2.2:3000/api/pasien/"
    private val json = Json{ignoreUnknownKeys = true}
    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(baseUrl).build()

    private val pasienService:PasienService by lazy {
        retrofit.create(PasienService::class.java)
    }

    override val pasienRepository: PasienRepository by lazy {
        NetworkPasienRepository(pasienService)
    }

}