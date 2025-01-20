package com.example.projekakhir.ui.viewmodel.terapis

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projekakhir.model.Terapis
import com.example.projekakhir.repository.TerapisRepository
import kotlinx.coroutines.launch

class InsertTerapisViewModel(private val terapisRepository: TerapisRepository) : ViewModel() {
    var uiState by mutableStateOf(InsertTerapisUiState())
        private set

    // Fungsi untuk memperbarui state form
    fun updateInsertTerapisState(insertUiEvent: InsertTerapisUiEvent) {
        uiState = InsertTerapisUiState(insertUiEvent = insertUiEvent)
    }

    // Fungsi untuk menyimpan data terapis
    suspend fun insertTerapis() {
        viewModelScope.launch {
            try {
                terapisRepository.insertTerapis(uiState.insertUiEvent.toTerapis())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

data class InsertTerapisUiState(
    val insertUiEvent: InsertTerapisUiEvent = InsertTerapisUiEvent()
)

data class InsertTerapisUiEvent(
    val idTerapis: Int = 0,
    val namaTerapis: String = "",
    val spesialisasi: String = "",
    val nomorIzinPraktik: String = ""
)

// Fungsi untuk mengubah InsertTerapisUiEvent menjadi objek Terapis
fun InsertTerapisUiEvent.toTerapis(): Terapis = Terapis(
    id_terapis = idTerapis,
    nama_terapis = namaTerapis,
    spesialisasi = spesialisasi,
    nomor_izin_praktik = nomorIzinPraktik
)

// Fungsi untuk mengubah objek Terapis ke UI State
fun Terapis.toUiStateTerapis(): InsertTerapisUiState = InsertTerapisUiState(
    insertUiEvent = toInsertTerapisUiEvent()
)

// Fungsi untuk mengubah objek Terapis ke InsertTerapisUiEvent
fun Terapis.toInsertTerapisUiEvent(): InsertTerapisUiEvent = InsertTerapisUiEvent(
    idTerapis = id_terapis,
    namaTerapis = nama_terapis,
    spesialisasi = spesialisasi,
    nomorIzinPraktik = nomor_izin_praktik
)
