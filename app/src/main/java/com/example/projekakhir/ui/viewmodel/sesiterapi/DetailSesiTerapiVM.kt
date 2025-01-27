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
import com.example.projekakhir.ui.view.sesiterapi.DestinasiDetailSesi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class DetailSesiUiState {
    data class Success(val sesiTerapi: SesiTerapi) : DetailSesiUiState()
    object Error : DetailSesiUiState()
    object Loading : DetailSesiUiState()
}

class DetailSesiTerapiViewModel(
    savedStateHandle: SavedStateHandle,
    private val sesiTerapiRepo: SesiTerapiRepository,
    private val jenisTerapiRepository: JenisTerapiRepository,
    private val terapisRepository: TerapisRepository,
    private val pasienRepository: PasienRepository
) : ViewModel() {

    private val _idSesi: Int = checkNotNull(savedStateHandle[DestinasiDetailSesi.idSesi])

    private val _detailUiState = MutableStateFlow<DetailSesiUiState>(DetailSesiUiState.Loading)
    val detailUiState: StateFlow<DetailSesiUiState> = _detailUiState

    var listPasien by mutableStateOf<List<Pasien>>(listOf())
        private set
    var listTerapis by mutableStateOf<List<Terapis>>(listOf())
        private set
    var listJenisTerapi by mutableStateOf<List<JenisTerapi>>(listOf())
        private set

    init {
        loadExistingData()
        getDetailSesiTerapi()
    }

    private fun loadExistingData() {
        viewModelScope.launch {
            try {
                listPasien = pasienRepository.getPasien().data
                listTerapis = terapisRepository.getTerapis().data
                listJenisTerapi = jenisTerapiRepository.getJenisTerapi().data
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getDetailSesiTerapi() {
        viewModelScope.launch {
            try {
                _detailUiState.value = DetailSesiUiState.Loading
                val sesiTerapi = sesiTerapiRepo.getSesiTerapiById(_idSesi)
                if (sesiTerapi != null) {
                    _detailUiState.value = DetailSesiUiState.Success(sesiTerapi)
                } else {
                    _detailUiState.value = DetailSesiUiState.Error
                }
            } catch (e: Exception) {
                _detailUiState.value = DetailSesiUiState.Error
            }
        }
    }

    fun getNamaPasien(idPasien: Int): String =
        listPasien.find { it.id_pasien == idPasien }?.nama_pasien ?: "Tidak ditemukan"

    fun getNamaTerapis(idTerapis: Int): String =
        listTerapis.find { it.id_terapis == idTerapis }?.nama_terapis ?: "Tidak ditemukan"

    fun getNamaJenisTerapi(idJenisTerapi: Int): String =
        listJenisTerapi.find { it.id_jenis_terapi == idJenisTerapi }?.nama_jenis_terapi ?: "Tidak ditemukan"
}




fun SesiTerapi.toDetailUiEvent(): InsertSesiTerapiUiEvent {
    return InsertSesiTerapiUiEvent(
        idSesi = id_sesi,
        idPasien = id_pasien,
        idTerapis = id_terapis,
        idJenisTerapi = id_jenis_terapi,
        tanggalSesi = tanggal_sesi,
        catatanSesi = catatan_sesi.orEmpty() // Mengubah null menjadi string kosong
    )
}
