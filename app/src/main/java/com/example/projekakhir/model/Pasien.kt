package com.example.projekakhir.model

import kotlinx.serialization.*

@Serializable
data class Pasien(
    val id_pasien: Int,
    val nama_pasien: String,
    val alamat: String,
    val nomor_telepon: String,
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
