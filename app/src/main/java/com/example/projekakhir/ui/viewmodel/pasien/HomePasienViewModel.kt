package com.example.projekakhir.ui.viewmodel.pasien

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.network.HttpException
import com.example.projekakhir.model.Pasien
import com.example.projekakhir.repository.PasienRepository
import kotlinx.coroutines.launch
import java.io.IOException

sealed class HomeUiState {
    data class Success(val pasien: List<Pasien>) : HomeUiState()
    object Error : HomeUiState()
    object Loading : HomeUiState()
}

class HomeViewModelPasien(private val pasienRepo: PasienRepository) : ViewModel() {
    var pasienUIState: HomeUiState by mutableStateOf(HomeUiState.Loading)
        private set

    init {
        getPasien()
    }

    fun getPasien() {
        viewModelScope.launch {
            pasienUIState = HomeUiState.Loading
            pasienUIState = try {
                HomeUiState.Success(pasienRepo.getPasien().data)
            } catch (e: IOException) {
                HomeUiState.Error
            } catch (e: HttpException) {
                HomeUiState.Error
            }
        }
    }

    fun deletePasien(idPasien: Int) {
        viewModelScope.launch {
            try {
                pasienRepo.deletePasien(idPasien)
            } catch (e: IOException) {
                pasienUIState = HomeUiState.Error
            } catch (e: HttpException) {
                pasienUIState = HomeUiState.Error
            }
        }
    }
}
