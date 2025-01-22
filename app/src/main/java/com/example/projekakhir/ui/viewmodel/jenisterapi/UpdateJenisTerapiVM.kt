package com.example.projekakhir.ui.viewmodel.jenisterapi

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projekakhir.model.JenisTerapi
import com.example.projekakhir.repository.JenisTerapiRepository
import com.example.projekakhir.ui.view.jenisterapi.DestinasiUpdateJenisTerapi
import kotlinx.coroutines.launch

class UpdateJenisTerapiViewModel(
    savedStateHandle: SavedStateHandle,
    private val jenisTerapiRepo: JenisTerapiRepository
) : ViewModel() {

    val idJenisTerapi: Int = checkNotNull(savedStateHandle[DestinasiUpdateJenisTerapi.ID_JENIS_TERAPI])

    var uiState = mutableStateOf(InsertJenisTerapiUiState())

    init {
        getJenisTerapi()
    }

    private fun getJenisTerapi() {
        viewModelScope.launch {
            try {
                val jenisTerapi = jenisTerapiRepo.getJenisTerapiById(idJenisTerapi)
                jenisTerapi?.let {
                    uiState.value = it.toInsertUIEvent()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun updateJenisTerapi(idJenisTerapi: Int, jenisTerapi: JenisTerapi) {
        viewModelScope.launch {
            try {
                jenisTerapiRepo.updateJenisTerapi(idJenisTerapi, jenisTerapi)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun updateJenisTerapiState(insertUiEvent: InsertJenisTerapiUiEvent) {
        uiState.value = uiState.value.copy(insertUiEvent = insertUiEvent)
    }
}

fun JenisTerapi.toInsertUIEvent(): InsertJenisTerapiUiState = InsertJenisTerapiUiState(
    insertUiEvent = this.toInsertUiEvent()
)

fun JenisTerapi.toInsertUiEvent(): InsertJenisTerapiUiEvent = InsertJenisTerapiUiEvent(
    idJenisTerapi = id_jenis_terapi,
    namaJenisTerapi = nama_jenis_terapi,
    deskripsiTerapi = deskripsi_terapi
)
