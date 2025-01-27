package com.example.projekakhir.ui.viewmodel

import DetailTerapisViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.projekakhir.TerapiApplications
import com.example.projekakhir.ui.viewmodel.jenisterapi.DetailJenisTerapiViewModel
import com.example.projekakhir.ui.viewmodel.jenisterapi.HomeViewModelJenisTerapi
import com.example.projekakhir.ui.viewmodel.jenisterapi.InsertJenisTerapiViewModel
import com.example.projekakhir.ui.viewmodel.jenisterapi.UpdateJenisTerapiViewModel
import com.example.projekakhir.ui.viewmodel.pasien.DetailPasienViewModel
import com.example.projekakhir.ui.viewmodel.pasien.HomeViewModelPasien
import com.example.projekakhir.ui.viewmodel.pasien.InsertPasienViewModel
import com.example.projekakhir.ui.viewmodel.pasien.UpdatePasienViewModel
import com.example.projekakhir.ui.viewmodel.sesiterapi.DetailSesiTerapiViewModel
import com.example.projekakhir.ui.viewmodel.sesiterapi.HomeViewModelSesiTerapi
import com.example.projekakhir.ui.viewmodel.sesiterapi.InsertSesiTerapiViewModel
import com.example.projekakhir.ui.viewmodel.sesiterapi.UpdateSesiTerapiViewModel
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

        initializer { HomeViewModelJenisTerapi(aplikasiTerapi().container.jenisTerapiRepository) }
        initializer { InsertJenisTerapiViewModel(aplikasiTerapi().container.jenisTerapiRepository) }
        initializer { DetailJenisTerapiViewModel(createSavedStateHandle(),aplikasiTerapi().container.jenisTerapiRepository) }
        initializer { UpdateJenisTerapiViewModel(createSavedStateHandle(),aplikasiTerapi().container.jenisTerapiRepository) }

        initializer { HomeViewModelSesiTerapi(aplikasiTerapi().container.sesiTerapiRepository) }
        initializer {
            InsertSesiTerapiViewModel(
                sesiTerapiRepository = aplikasiTerapi().container.sesiTerapiRepository,
                jenisTerapiRepository = aplikasiTerapi().container.jenisTerapiRepository,
                terapisRepository = aplikasiTerapi().container.terapisRepository,
                pasienRepository = aplikasiTerapi().container.pasienRepository
            )
        }
        initializer {
            DetailSesiTerapiViewModel(
                createSavedStateHandle(),
                aplikasiTerapi().container.sesiTerapiRepository,
                aplikasiTerapi().container.jenisTerapiRepository,
                aplikasiTerapi().container.terapisRepository,
                aplikasiTerapi().container.pasienRepository,
            )
        }

        initializer {
            UpdateSesiTerapiViewModel(
                createSavedStateHandle(),
                sesiTerapiRepository = aplikasiTerapi().container.sesiTerapiRepository,
                jenisTerapiRepository = aplikasiTerapi().container.jenisTerapiRepository,
                terapisRepository = aplikasiTerapi().container.terapisRepository,
                pasienRepository = aplikasiTerapi().container.pasienRepository
            )
        }
    }
}

fun CreationExtras.aplikasiTerapi():TerapiApplications =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]as TerapiApplications)