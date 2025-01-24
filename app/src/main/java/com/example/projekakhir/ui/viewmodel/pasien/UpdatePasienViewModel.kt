package com.example.projekakhir.ui.viewmodel.pasien


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projekakhir.model.Pasien
import com.example.projekakhir.repository.PasienRepository
import com.example.projekakhir.ui.view.pasien.DestinasiUpdatePasien
import kotlinx.coroutines.launch


class UpdatePasienViewModel(
    savedStateHandle: SavedStateHandle,
    private val pasienRepository: PasienRepository
) : ViewModel() {
    val idPasien: Int = checkNotNull(savedStateHandle[DestinasiUpdatePasien.idpasien])

    var uiState by mutableStateOf(InsertPasienUiState())
        private set

    init {
        getPasien()
    }

    // Fungsi untuk mengambil data pasien
    private fun getPasien() {
        viewModelScope.launch {
            try {
                val pasien = pasienRepository.getPasienById(idPasien)
                pasien?.let {
                    uiState = it.toUiStatePasien() // Update UI State dengan data pasien
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // Fungsi untuk melakukan update pasien
    fun updatePasien(idPasien: Int, pasien: Pasien) {
        viewModelScope.launch {
            try {
                pasienRepository.updatePasien(idPasien, pasien) // Panggil repository untuk update
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // Fungsi untuk memperbarui UI State
    fun updatePasienState(insertUiEvent: InsertPasienUiEvent) {
        uiState = uiState.copy(insertUiEvent = insertUiEvent)
    }
}
