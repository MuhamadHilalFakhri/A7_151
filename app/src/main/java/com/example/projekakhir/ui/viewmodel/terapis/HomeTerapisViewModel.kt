package com.example.projekakhir.ui.viewmodel.terapis

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.network.HttpException
import com.example.projekakhir.model.Terapis
import com.example.projekakhir.repository.TerapisRepository
import kotlinx.coroutines.launch
import java.io.IOException

sealed class HomeUiState {
    data class Success(val terapis: List<Terapis>) : HomeUiState()
    object Error : HomeUiState()
    object Loading : HomeUiState()
}

class HomeViewModelTerapis(private val terapisRepo: TerapisRepository) : ViewModel() {
    var terapisUIState: HomeUiState by mutableStateOf(HomeUiState.Loading)
        private set

    init {
        getTerapis()
    }

    fun getTerapis() {
        viewModelScope.launch {
            terapisUIState = HomeUiState.Loading
            terapisUIState = try {
                HomeUiState.Success(terapisRepo.getTerapis().data)
            } catch (e: IOException) {
                HomeUiState.Error
            } catch (e: HttpException) {
                HomeUiState.Error
            }
        }
    }

    fun deleteTerapis(idTerapis: Int) {
        viewModelScope.launch {
            try {
                terapisRepo.deleteTerapis(idTerapis)
            } catch (e: IOException) {
                terapisUIState = HomeUiState.Error
            } catch (e: HttpException) {
                terapisUIState = HomeUiState.Error
            }
        }
    }
}
