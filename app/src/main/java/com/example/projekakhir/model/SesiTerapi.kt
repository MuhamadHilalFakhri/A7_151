package com.example.projekakhir.model

import kotlinx.serialization.*

@Serializable
data class SesiTerapi(
    val id_sesi: Int,
    val id_pasien: Int,
    val id_terapis: Int,
    val id_jenis_terapi: Int,
    val tanggal_sesi: String,
    val catatan_sesi: String
)

@Serializable
data class AllSesiResponse(
    val status: Boolean,
    val message: String,
    val data: List<SesiTerapi>
)

@Serializable
data class SesiDetailResponse(
    val status: Boolean,
    val message: String,
    val data: SesiTerapi
)
