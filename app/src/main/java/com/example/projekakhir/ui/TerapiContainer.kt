package com.example.projekakhir.ui

import com.example.projekakhir.repository.NetworkPasienRepository
import com.example.projekakhir.repository.NetworkTerapisRepository
import com.example.projekakhir.repository.PasienRepository
import com.example.projekakhir.repository.TerapisRepository
import com.example.projekakhir.service.PasienService
import com.example.projekakhir.service.TerapisService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit


interface AppContainer {
    val pasienRepository: PasienRepository
    val terapisRepository: TerapisRepository
}

class TerapiContainer : AppContainer {

    // Base URL untuk Pasien dan Terapis
    private val pasienBaseUrl = "http://10.0.2.2:3000/api/pasien/"
    private val terapisBaseUrl = "http://10.0.2.2:3000/api/terapis/"

    // Inisialisasi JsonConverter dan Retrofit
    private val json = Json { ignoreUnknownKeys = true }

    private val pasienRetrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(pasienBaseUrl)
        .build()

    private val terapisRetrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(terapisBaseUrl)
        .build()

    // Layanan Pasien
    private val pasienService: PasienService by lazy {
        pasienRetrofit.create(PasienService::class.java)
    }

    // Layanan Terapis
    private val terapisService: TerapisService by lazy {
        terapisRetrofit.create(TerapisService::class.java)
    }

    // Repository Pasien
    override val pasienRepository: PasienRepository by lazy {
        NetworkPasienRepository(pasienService)
    }

    // Repository Terapis
    override val terapisRepository: TerapisRepository by lazy {
        NetworkTerapisRepository(terapisService)
    }
}
