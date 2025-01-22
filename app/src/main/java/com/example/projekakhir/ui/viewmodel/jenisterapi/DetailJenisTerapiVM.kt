package com.example.projekakhir.ui.viewmodel.jenisterapi

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projekakhir.model.JenisTerapi
import com.example.projekakhir.repository.JenisTerapiRepository
import com.example.projekakhir.ui.view.jenisterapi.DestinasiDetailJenisTerapi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class DetailJenisTerapiUiState {
    data class Success(val jenisTerapi: JenisTerapi) : DetailJenisTerapiUiState()
    object Error : DetailJenisTerapiUiState()
    object Loading : DetailJenisTerapiUiState()
}

class DetailJenisTerapiViewModel(
    savedStateHandle: SavedStateHandle,
    private val jenisTerapiRepo: JenisTerapiRepository
) : ViewModel() {

    private val _idJenisTerapi: Int = checkNotNull(savedStateHandle[DestinasiDetailJenisTerapi.ID_JENIS_TERAPI])

    private val _detailUiState = MutableStateFlow<DetailJenisTerapiUiState>(DetailJenisTerapiUiState.Loading)
    val detailUiState: StateFlow<DetailJenisTerapiUiState> = _detailUiState

    init {
        getDetailJenisTerapi()
    }

    fun getDetailJenisTerapi() {
        viewModelScope.launch {
            try {
                _detailUiState.value = DetailJenisTerapiUiState.Loading
                val jenisTerapi = jenisTerapiRepo.getJenisTerapiById(_idJenisTerapi)
                if (jenisTerapi != null) {
                    _detailUiState.value = DetailJenisTerapiUiState.Success(jenisTerapi)
                } else {
                    _detailUiState.value = DetailJenisTerapiUiState.Error
                }
            } catch (e: Exception) {
                _detailUiState.value = DetailJenisTerapiUiState.Error
            }
        }
    }
}

fun JenisTerapi.toDetailUiEvent(): InsertJenisTerapiUiEvent {
    return InsertJenisTerapiUiEvent(
        idJenisTerapi = id_jenis_terapi,
        namaJenisTerapi = nama_jenis_terapi,
        deskripsiTerapi = deskripsi_terapi
    )
}
