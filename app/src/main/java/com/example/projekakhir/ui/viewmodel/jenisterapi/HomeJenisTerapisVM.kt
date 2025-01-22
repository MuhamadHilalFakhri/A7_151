package com.example.projekakhir.ui.viewmodel.jenisterapi

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.network.HttpException
import com.example.projekakhir.model.JenisTerapi
import com.example.projekakhir.repository.JenisTerapiRepository
import kotlinx.coroutines.launch
import java.io.IOException

sealed class HomeUiStateJenisTerapi {
    data class Success(val jenisTerapi: List<JenisTerapi>) : HomeUiStateJenisTerapi()
    object Error : HomeUiStateJenisTerapi()
    object Loading : HomeUiStateJenisTerapi()
}

class HomeViewModelJenisTerapi(private val jenisTerapiRepo: JenisTerapiRepository) : ViewModel() {
    var jenisTerapiUIState: HomeUiStateJenisTerapi by mutableStateOf(HomeUiStateJenisTerapi.Loading)
        private set

    init {
        getJenisTerapi()
    }

    fun getJenisTerapi() {
        viewModelScope.launch {
            jenisTerapiUIState = HomeUiStateJenisTerapi.Loading
            jenisTerapiUIState = try {
                HomeUiStateJenisTerapi.Success(jenisTerapiRepo.getJenisTerapi().data)
            } catch (e: IOException) {
                HomeUiStateJenisTerapi.Error
            } catch (e: HttpException) {
                HomeUiStateJenisTerapi.Error
            }
        }
    }

    fun deleteJenisTerapi(idJenisTerapi: Int) {
        viewModelScope.launch {
            try {
                jenisTerapiRepo.deleteJenisTerapi(idJenisTerapi)
                // Refresh the list after deletion
                getJenisTerapi()
            } catch (e: IOException) {
                jenisTerapiUIState = HomeUiStateJenisTerapi.Error
            } catch (e: HttpException) {
                jenisTerapiUIState = HomeUiStateJenisTerapi.Error
            }
        }
    }
}
