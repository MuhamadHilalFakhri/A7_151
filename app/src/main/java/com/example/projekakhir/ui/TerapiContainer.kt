package com.example.projekakhir.ui

import com.example.projekakhir.repository.JenisTerapiRepository
import com.example.projekakhir.repository.NetworkJenisTerapiRepository
import com.example.projekakhir.repository.NetworkPasienRepository
import com.example.projekakhir.repository.NetworkSesiTerapiRepository
import com.example.projekakhir.repository.NetworkTerapisRepository
import com.example.projekakhir.repository.PasienRepository
import com.example.projekakhir.repository.SesiTerapiRepository
import com.example.projekakhir.repository.TerapisRepository
import com.example.projekakhir.service.JenisTerapiService
import com.example.projekakhir.service.PasienService
import com.example.projekakhir.service.SesiTerapiService
import com.example.projekakhir.service.TerapisService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit


interface AppContainer {
    val pasienRepository: PasienRepository
    val terapisRepository: TerapisRepository
    val jenisTerapiRepository: JenisTerapiRepository
    val sesiTerapiRepository : SesiTerapiRepository
}

class TerapiContainer : AppContainer {

    // Base URL untuk Pasien dan Terapis
    private val pasienBaseUrl = "http://10.0.2.2:3000/api/pasien/"
    private val terapisBaseUrl = "http://10.0.2.2:3000/api/terapis/"
    private val jenisTerapiBaseUrl = "http://10.0.2.2:3000/api/jenis_terapi/"
    private val sesiTerapiBaseUrl = "http://10.0.2.2:3000/api/sesi_terapi/"

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

    private val jenisTerapiRetrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(jenisTerapiBaseUrl)
        .build()

    private val sesiTerapiRetrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(sesiTerapiBaseUrl)
        .build()

    // Layanan Pasien
    private val pasienService: PasienService by lazy {
        pasienRetrofit.create(PasienService::class.java)
    }

    // Layanan Terapis
    private val terapisService: TerapisService by lazy {
        terapisRetrofit.create(TerapisService::class.java)
    }

    // Layanan Jenis Terapi
    private val jenisTerapiService: JenisTerapiService by lazy {
        jenisTerapiRetrofit.create(JenisTerapiService::class.java)
    }

    // Layanan Sesi Terapi
    private val sesiTerapiService: SesiTerapiService by lazy {
        sesiTerapiRetrofit.create(SesiTerapiService::class.java)
    }
    // Repository Pasien
    override val pasienRepository: PasienRepository by lazy {
        NetworkPasienRepository(pasienService)
    }

    // Repository Terapis
    override val terapisRepository: TerapisRepository by lazy {
        NetworkTerapisRepository(terapisService)
    }

    // Repository Jenis Terapi
    override val jenisTerapiRepository: JenisTerapiRepository by lazy {
        NetworkJenisTerapiRepository(jenisTerapiService)
    }

    // Repository Sesi Terapi
    override val sesiTerapiRepository: SesiTerapiRepository by lazy {
        NetworkSesiTerapiRepository(sesiTerapiService)
    }
}
