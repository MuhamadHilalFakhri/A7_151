package com.example.projekakhir.ui.view.pasien

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projekakhir.navigation.DestinasiNavigasi
import com.example.projekakhir.ui.customwidget.CostumeTopAppBar
import com.example.projekakhir.ui.viewmodel.PenyediaViewModel
import com.example.projekakhir.ui.viewmodel.pasien.UpdatePasienViewModel
import com.example.projekakhir.ui.viewmodel.pasien.toPasien
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

object DestinasiUpdatePasien : DestinasiNavigasi {
    override val route = "update pasien"
    const val idpasien = "idpasien"
    val routesWithArg = "$route/{$idpasien}"
    override val titleRes = "Update Pasien"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdatePasienView(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: UpdatePasienViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    val uiState = viewModel.uiState

    // Format tanggal lahir yang diterima (UTC format)
    val formattedTanggalLahir = remember(uiState.insertUiEvent?.tanggalLahir) {
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        try {
            // Pastikan tanggal lahir yang diterima tidak kosong dan valid
            val parsedDate = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
                .parse(uiState.insertUiEvent?.tanggalLahir ?: "")
            parsedDate?.let { format.format(it) } ?: uiState.insertUiEvent?.tanggalLahir
        } catch (e: Exception) {
            // Tangani tanggal yang tidak valid, kembalikan string kosong atau nilai default
            uiState.insertUiEvent?.tanggalLahir ?: ""
        }
    }

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CostumeTopAppBar(
                title = DestinasiUpdatePasien.titleRes,
                canNavigateBack = true,
                scrollBehavior = scrollBehavior,
                navigateUp = navigateBack,
                showRefreshIcon = false
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            // Entry Pasien Form Body
            EntryBodyPasien(
                insertUiState = uiState.copy(
                    insertUiEvent = uiState.insertUiEvent?.copy(tanggalLahir = formattedTanggalLahir) ?: uiState.insertUiEvent
                ),
                onPasienValueChange = { updatedValue ->
                    viewModel.updatePasienState(updatedValue)
                },
                onSaveClick = {
                    uiState.insertUiEvent?.let { insertUiEvent ->
                        coroutineScope.launch {
                            viewModel.updatePasien(
                                idPasien = viewModel.idPasien,
                                pasien = insertUiEvent.toPasien()
                            )
                            navigateBack()
                        }
                    }
                }
            )
        }
    }
}

