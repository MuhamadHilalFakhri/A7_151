package com.example.projekakhir.ui.viewmodel.sesiterapi

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.network.HttpException
import com.example.projekakhir.model.JenisTerapi
import com.example.projekakhir.model.Pasien
import com.example.projekakhir.model.SesiTerapi
import com.example.projekakhir.model.Terapis
import com.example.projekakhir.repository.JenisTerapiRepository
import com.example.projekakhir.repository.PasienRepository
import com.example.projekakhir.repository.SesiTerapiRepository
import com.example.projekakhir.repository.TerapisRepository
import kotlinx.coroutines.launch
import java.io.IOException

sealed class HomeUiState {
    data class Success(val sesiTerapi: List<SesiTerapi>) : HomeUiState()
    object Error : HomeUiState()
    object Loading : HomeUiState()
}

class HomeViewModelSesiTerapi(
    private val sesiTerapiRepo: SesiTerapiRepository,
    private val pasienRepository: PasienRepository,
    private val jenisTerapiRepository: JenisTerapiRepository,
    private val terapisRepository: TerapisRepository
) : ViewModel() {

    var sesiTerapiUIState: HomeUiState by mutableStateOf(HomeUiState.Loading)
        private set

    var listPasien by mutableStateOf<List<Pasien>>(listOf())
        private set
    var listTerapis by mutableStateOf<List<Terapis>>(listOf())
        private set
    var listJenisTerapi by mutableStateOf<List<JenisTerapi>>(listOf())
        private set

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            try {
                // Load pasien, terapis, dan jenis terapi
                listPasien = pasienRepository.getPasien().data
                listTerapis = terapisRepository.getTerapis().data
                listJenisTerapi = jenisTerapiRepository.getJenisTerapi().data
                // Load sesi terapi
                getSesiTerapi()
            } catch (e: Exception) {
                sesiTerapiUIState = HomeUiState.Error
            }
        }
    }

    fun getSesiTerapi() {
        viewModelScope.launch {
            sesiTerapiUIState = HomeUiState.Loading
            sesiTerapiUIState = try {
                HomeUiState.Success(sesiTerapiRepo.getSesiTerapi().data)
            } catch (e: Exception) {
                HomeUiState.Error
            }
        }
    }


    fun getNamaPasien(idPasien: Int): String =
        listPasien.find { it.id_pasien == idPasien }?.nama_pasien ?: "Tidak ditemukan"

    fun getNamaTerapis(idTerapis: Int): String =
        listTerapis.find { it.id_terapis == idTerapis }?.nama_terapis ?: "Tidak ditemukan"

    fun getNamaJenisTerapi(idJenisTerapi: Int): String =
        listJenisTerapi.find { it.id_jenis_terapi == idJenisTerapi }?.nama_jenis_terapi ?: "Tidak ditemukan"

    fun deleteSesiTerapi(idSesi: Int) {
        viewModelScope.launch {
            try {
                sesiTerapiRepo.deleteSesiTerapi(idSesi)
                getSesiTerapi()
            } catch (e: Exception) {
                sesiTerapiUIState = HomeUiState.Error
            }
        }
    }
}

