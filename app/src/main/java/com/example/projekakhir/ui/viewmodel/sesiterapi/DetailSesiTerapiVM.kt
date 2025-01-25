package com.example.projekakhir.ui.viewmodel.sesiterapi

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projekakhir.model.SesiTerapi
import com.example.projekakhir.repository.SesiTerapiRepository
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
    private val sesiTerapiRepo: SesiTerapiRepository
) : ViewModel() {

    private val _idSesi: Int = checkNotNull(savedStateHandle[DestinasiDetailSesi.idSesi])

    private val _detailUiState = MutableStateFlow<DetailSesiUiState>(DetailSesiUiState.Loading)
    val detailUiState: StateFlow<DetailSesiUiState> = _detailUiState

    init {
        getDetailSesiTerapi()
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
