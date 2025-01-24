package com.example.projekakhir.ui.viewmodel.sesiterapi

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.network.HttpException
import com.example.projekakhir.model.SesiTerapi
import com.example.projekakhir.repository.SesiTerapiRepository
import kotlinx.coroutines.launch
import java.io.IOException

sealed class HomeUiState {
    data class Success(val sesiTerapi: List<SesiTerapi>) : HomeUiState()
    object Error : HomeUiState()
    object Loading : HomeUiState()
}

class HomeViewModelSesiTerapi(private val sesiTerapiRepo: SesiTerapiRepository) : ViewModel() {
    var sesiTerapiUIState: HomeUiState by mutableStateOf(HomeUiState.Loading)
        private set

    init {
        getSesiTerapi()
    }

    fun getSesiTerapi() {
        viewModelScope.launch {
            sesiTerapiUIState = HomeUiState.Loading
            sesiTerapiUIState = try {
                // Mengambil data sesi terapi
                HomeUiState.Success(sesiTerapiRepo.getSesiTerapi().data)
            } catch (e: IOException) {
                HomeUiState.Error
            } catch (e: HttpException) {
                HomeUiState.Error
            }
        }
    }

    fun deleteSesiTerapi(idSesi: Int) {
        viewModelScope.launch {
            try {
                // Menghapus sesi terapi berdasarkan id
                sesiTerapiRepo.deleteSesiTerapi(idSesi)
                // Setelah menghapus, kita refresh data
                getSesiTerapi()
            } catch (e: IOException) {
                sesiTerapiUIState = HomeUiState.Error
            } catch (e: HttpException) {
                sesiTerapiUIState = HomeUiState.Error
            }
        }
    }
}
