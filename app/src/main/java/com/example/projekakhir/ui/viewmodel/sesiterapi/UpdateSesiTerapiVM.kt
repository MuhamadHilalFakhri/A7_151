package com.example.projekakhir.ui.viewmodel.sesiterapi

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projekakhir.model.JenisTerapi
import com.example.projekakhir.model.Pasien
import com.example.projekakhir.model.SesiTerapi
import com.example.projekakhir.model.Terapis
import com.example.projekakhir.repository.JenisTerapiRepository
import com.example.projekakhir.repository.PasienRepository
import com.example.projekakhir.repository.SesiTerapiRepository
import com.example.projekakhir.repository.TerapisRepository
import com.example.projekakhir.ui.view.sesiterapi.DestinasiUpdateSesiTerapi
import kotlinx.coroutines.launch

class UpdateSesiTerapiViewModel(
    savedStateHandle: SavedStateHandle,
    private val sesiTerapiRepository: SesiTerapiRepository,
    private val jenisTerapiRepository: JenisTerapiRepository,
    private val terapisRepository: TerapisRepository,
    private val pasienRepository: PasienRepository
) : ViewModel() {

    val idSesi: Int = checkNotNull(savedStateHandle[DestinasiUpdateSesiTerapi.idsesi])

    var uiState by mutableStateOf(InsertSesiTerapiUiState())
        private set

    var listPasien by mutableStateOf<List<Pasien>>(listOf())
        private set
    var listTerapis by mutableStateOf<List<Terapis>>(listOf())
        private set
    var listJnsTerapi by mutableStateOf<List<JenisTerapi>>(listOf())
        private set

    init {
        loadSesiTerapi()
        loadExistingData()
    }

    // Fungsi untuk memuat data sesi terapi berdasarkan ID
    private fun loadSesiTerapi() {
        viewModelScope.launch {
            try {
                val sesiTerapi = sesiTerapiRepository.getSesiTerapiById(idSesi)
                sesiTerapi?.let {
                    uiState = it.toUiStateSesiTerapi()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // Fungsi untuk memuat data terkait (Pasien, Terapis, Jenis Terapi)
    private fun loadExistingData() {
        viewModelScope.launch {
            try {
                listJnsTerapi = jenisTerapiRepository.getJenisTerapi().data
                listTerapis = terapisRepository.getTerapis().data
                listPasien = pasienRepository.getPasien().data
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // Fungsi untuk memperbarui state UI
    fun updateSesiTerapiState(updateUiEvent: InsertSesiTerapiUiEvent) {
        uiState = uiState.copy(insertUiEvent = updateUiEvent)
    }

    // Fungsi untuk menyimpan perubahan ke database
    fun updateSesiTerapi() {
        viewModelScope.launch {
            try {
                sesiTerapiRepository.updateSesiTerapi(
                    idSesi,
                    uiState.insertUiEvent.toSesiTerapi()
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

// Extension function untuk konversi model ke UI state
fun SesiTerapi.toUiStateSesiTerapi(): InsertSesiTerapiUiState {
    return InsertSesiTerapiUiState(
        insertUiEvent = InsertSesiTerapiUiEvent(
            idSesi = id_sesi,
            idPasien = id_pasien,
            idTerapis = id_terapis,
            idJenisTerapi = id_jenis_terapi,
            tanggalSesi = tanggal_sesi,
            catatanSesi = catatan_sesi
        )
    )
}
