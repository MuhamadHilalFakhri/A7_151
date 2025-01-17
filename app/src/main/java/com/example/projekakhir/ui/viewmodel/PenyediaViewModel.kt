package com.example.projekakhir.ui.viewmodel

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.projekakhir.TerapiApplications
import com.example.projekakhir.ui.viewmodel.pasien.HomeViewModelPasien

object PenyediaViewModel{
    val Factory = viewModelFactory {
        initializer { HomeViewModelPasien(aplikasiTerapi().container.pasienRepository) }

    }
}

fun CreationExtras.aplikasiTerapi():TerapiApplications =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]as TerapiApplications)