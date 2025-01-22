package com.example.projekakhir.model
import kotlinx.serialization.*
@Serializable
data class JenisTerapi(
    val id_jenis_terapi: Int,
    val nama_jenis_terapi: String,
    val deskripsi_terapi: String
)

@Serializable
data class AllJenisTerapiResponse(
    val status: Boolean,
    val message: String,
    val data: List<JenisTerapi>
)

@Serializable
data class JenisTerapiDetailResponse(
    val status: Boolean,
    val message: String,
    val data: JenisTerapi
)
