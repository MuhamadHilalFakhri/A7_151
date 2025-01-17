package com.example.projekakhir.ui.viewmodel.pasien

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projekakhir.model.Pasien
import com.example.projekakhir.repository.PasienRepository
import com.example.projekakhir.ui.view.pasien.DestinasiDetail
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class DetailUiState {
    data class Success(val pasien: Pasien) : DetailUiState()
    object Error : DetailUiState()
    object Loading : DetailUiState()
}
class DetailPasienViewModel(
    savedStateHandle: SavedStateHandle,
    private val pasienRepo: PasienRepository
) : ViewModel() {

    // Ambil ID Pasien dari SavedStateHandle
    private val _idPasien: Int = checkNotNull(savedStateHandle[DestinasiDetail.idpasien])

    // MutableStateFlow untuk mengatur UI state
    private val _detailUiState = MutableStateFlow<DetailUiState>(DetailUiState.Loading)
    val detailUiState: StateFlow<DetailUiState> = _detailUiState

    init {
        getDetailPasien()
    }

    // Fungsi untuk mengambil detail pasien
    fun getDetailPasien() {
        viewModelScope.launch {
            try {
                _detailUiState.value = DetailUiState.Loading
                val pasien = pasienRepo.getPasienById(_idPasien)
                if (pasien != null) {
                    _detailUiState.value = DetailUiState.Success(pasien)
                } else {
                    _detailUiState.value = DetailUiState.Error
                }
            } catch (e: Exception) {
                _detailUiState.value = DetailUiState.Error
            }
        }
    }
}
fun Pasien.toDetailUiEvent(): InsertPasienUiEvent {
    return InsertPasienUiEvent(
        idPasien = id_pasien,
        namaPasien = nama_pasien,
        alamat = alamat,
        nomorTelepon = nomor_telepon,
        tanggalLahir = tanggal_lahir,
        riwayatMedikal = riwayat_medikal
    )
}


