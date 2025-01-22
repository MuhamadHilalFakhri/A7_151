package com.example.projekakhir.ui.viewmodel.jenisterapi

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projekakhir.model.JenisTerapi
import com.example.projekakhir.repository.JenisTerapiRepository
import kotlinx.coroutines.launch

class InsertJenisTerapiViewModel(private val jenisTerapiRepository: JenisTerapiRepository) : ViewModel() {
    var uiState by mutableStateOf(InsertJenisTerapiUiState())
        private set

    // Fungsi untuk memperbarui state form
    fun updateInsertJenisTerapiState(insertUiEvent: InsertJenisTerapiUiEvent) {
        uiState = InsertJenisTerapiUiState(insertUiEvent = insertUiEvent)
    }

    // Fungsi untuk menyimpan data jenis terapi
    suspend fun insertJenisTerapi() {
        viewModelScope.launch {
            try {
                jenisTerapiRepository.insertJenisTerapi(uiState.insertUiEvent.toJenisTerapi())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

data class InsertJenisTerapiUiState(
    val insertUiEvent: InsertJenisTerapiUiEvent = InsertJenisTerapiUiEvent()
)

data class InsertJenisTerapiUiEvent(
    val idJenisTerapi: Int = 0,
    val namaJenisTerapi: String = "",
    val deskripsiTerapi: String = ""
)

// Fungsi untuk mengubah InsertJenisTerapiUiEvent menjadi objek JenisTerapi
fun InsertJenisTerapiUiEvent.toJenisTerapi(): JenisTerapi = JenisTerapi(
    id_jenis_terapi = idJenisTerapi,
    nama_jenis_terapi = namaJenisTerapi,
    deskripsi_terapi = deskripsiTerapi
)

// Fungsi untuk mengubah objek JenisTerapi ke UI State
fun JenisTerapi.toUiStateJenisTerapi(): InsertJenisTerapiUiState = InsertJenisTerapiUiState(
    insertUiEvent = toInsertJenisTerapiUiEvent()
)

// Fungsi untuk mengubah objek JenisTerapi ke InsertJenisTerapiUiEvent
fun JenisTerapi.toInsertJenisTerapiUiEvent(): InsertJenisTerapiUiEvent = InsertJenisTerapiUiEvent(
    idJenisTerapi = id_jenis_terapi,
    namaJenisTerapi = nama_jenis_terapi,
    deskripsiTerapi = deskripsi_terapi
)
