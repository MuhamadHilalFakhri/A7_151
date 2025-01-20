package com.example.projekakhir.ui.viewmodel

import DetailTerapisViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.projekakhir.TerapiApplications
import com.example.projekakhir.ui.viewmodel.pasien.DetailPasienViewModel
import com.example.projekakhir.ui.viewmodel.pasien.HomeViewModelPasien
import com.example.projekakhir.ui.viewmodel.pasien.InsertPasienViewModel
import com.example.projekakhir.ui.viewmodel.pasien.UpdatePasienViewModel
import com.example.projekakhir.ui.viewmodel.terapis.HomeViewModelTerapis
import com.example.projekakhir.ui.viewmodel.terapis.InsertTerapisViewModel
import com.example.projekakhir.ui.viewmodel.terapis.UpdateTerapisViewModel

object PenyediaViewModel{
    val Factory = viewModelFactory {
        initializer { HomeViewModelPasien(aplikasiTerapi().container.pasienRepository) }
        initializer { InsertPasienViewModel(aplikasiTerapi().container.pasienRepository) }
        initializer { DetailPasienViewModel(createSavedStateHandle(),aplikasiTerapi().container.pasienRepository) }
        initializer { UpdatePasienViewModel(createSavedStateHandle(),aplikasiTerapi().container.pasienRepository) }
        initializer { HomeViewModelTerapis(aplikasiTerapi().container.terapisRepository) }
        initializer { InsertTerapisViewModel(aplikasiTerapi().container.terapisRepository) }
        initializer { DetailTerapisViewModel(createSavedStateHandle(),aplikasiTerapi().container.terapisRepository) }
        initializer { UpdateTerapisViewModel(createSavedStateHandle(),aplikasiTerapi().container.terapisRepository) }
    }
}

fun CreationExtras.aplikasiTerapi():TerapiApplications =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]as TerapiApplications)