package com.example.projekakhir.model

import kotlinx.serialization.*

@Serializable
data class Pasien(
    val idPasien: Int,
    val namaPasien: String,
    val alamat: String,
    val nomorTelepon: String,
    val tanggal_lahir: String,
    val riwayat_medikal: String
)

@Serializable
data class AllPasienResponse(
    val status: Boolean,
    val message: String,
    val data: List<Pasien>
)

@Serializable
data class PasienDetailResponse(
    val status: Boolean,
    val message: String,
    val data: Pasien
)
