package com.example.projekakhir.ui.viewmodel.terapis

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projekakhir.model.Terapis
import com.example.projekakhir.repository.TerapisRepository
import com.example.projekakhir.ui.view.sesiterapi.DestinasiUpdateTerapis
import kotlinx.coroutines.launch

class UpdateTerapisViewModel(
    savedStateHandle: SavedStateHandle,
    private val terapisRepo: TerapisRepository
) : ViewModel() {

    val idTerapis: Int = checkNotNull(savedStateHandle[DestinasiUpdateTerapis.ID_TERAPIS])

    var uiState = mutableStateOf(InsertTerapisUiState())

    init {
        getTerapis()
    }

    private fun getTerapis() {
        viewModelScope.launch {
            try {
                val terapis = terapisRepo.getTerapisById(idTerapis)
                terapis?.let {
                    uiState.value = it.toInsertUIEvent()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun updateTerapis(idTerapis: Int, terapis: Terapis) {
        viewModelScope.launch {
            try {
                terapisRepo.updateTerapis(idTerapis, terapis)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun updateTerapisState(insertUiEvent: InsertTerapisUiEvent) {
        uiState.value = uiState.value.copy(insertUiEvent = insertUiEvent)
    }
}

fun Terapis.toInsertUIEvent(): InsertTerapisUiState = InsertTerapisUiState(
    insertUiEvent = this.toInsertUiEvent()
)

fun Terapis.toInsertUiEvent(): InsertTerapisUiEvent = InsertTerapisUiEvent(
    idTerapis = id_terapis,
    namaTerapis = nama_terapis,
    spesialisasi = spesialisasi,
    nomorIzinPraktik = nomor_izin_praktik
)
