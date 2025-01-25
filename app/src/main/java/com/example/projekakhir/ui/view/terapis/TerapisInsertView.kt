package com.example.projekakhir.ui.view.terapis

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projekakhir.navigation.DestinasiNavigasi
import com.example.projekakhir.ui.custom.CostumeTopAppBar
import com.example.projekakhir.ui.viewmodel.PenyediaViewModel
import com.example.projekakhir.ui.viewmodel.terapis.InsertTerapisUiEvent
import com.example.projekakhir.ui.viewmodel.terapis.InsertTerapisUiState
import com.example.projekakhir.ui.viewmodel.terapis.InsertTerapisViewModel
import kotlinx.coroutines.launch

object DestinasiEntryTerapis : DestinasiNavigasi {
    override val route = "item_entry_terapis"
    override val titleRes = "Entry Terapis"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntryTerapisScreen(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: InsertTerapisViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CostumeTopAppBar(
                title = DestinasiEntryTerapis.titleRes,
                canNavigateBack = true,
                scrollBehavior = scrollBehavior,
                navigateUp = navigateBack
            )
        }
    ) { innerPadding ->
        EntryBodyTerapis(
            insertUiState = viewModel.uiState,
            onTerapisValueChange = viewModel::updateInsertTerapisState,
            onSaveClick = {
                coroutineScope.launch {
                    viewModel.insertTerapis()
                    navigateBack()
                }
            },
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .fillMaxWidth()
        )
    }
}

@Composable
fun EntryBodyTerapis(
    insertUiState: InsertTerapisUiState,
    onTerapisValueChange: (InsertTerapisUiEvent) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val showValidationError = rememberSaveable { mutableStateOf(false) }
    val showConfirmationDialog = rememberSaveable { mutableStateOf(false) }

    Column(
        verticalArrangement = Arrangement.spacedBy(18.dp),
        modifier = modifier.padding(12.dp)
    ) {
        FormInputTerapis(
            insertUiEvent = insertUiState.insertUiEvent,
            onValueChange = onTerapisValueChange,
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                val event = insertUiState.insertUiEvent
                if (event.namaTerapis.isBlank() || event.spesialisasi.isBlank() || event.nomorIzinPraktik.isBlank()) {
                    showValidationError.value = true
                } else {
                    showConfirmationDialog.value = true
                }
            },
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Simpan")
        }
    }

    if (showValidationError.value) {
        AlertDialog(
            onDismissRequest = { showValidationError.value = false },
            title = { Text("Error") },
            text = { Text("Semua kolom harus diisi.") },
            confirmButton = {
                Button(onClick = { showValidationError.value = false }) {
                    Text("OK")
                }
            }
        )
    }

    if (showConfirmationDialog.value) {
        AlertDialog(
            onDismissRequest = { showConfirmationDialog.value = false },
            title = { Text("Konfirmasi") },
            text = { Text("Apakah data sudah benar?") },
            confirmButton = {
                Button(onClick = {
                    showConfirmationDialog.value = false
                    onSaveClick()
                }) {
                    Text("Ya")
                }
            },
            dismissButton = {
                Button(onClick = { showConfirmationDialog.value = false }) {
                    Text("Tidak")
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormInputTerapis(
    insertUiEvent: InsertTerapisUiEvent,
    modifier: Modifier = Modifier,
    onValueChange: (InsertTerapisUiEvent) -> Unit = {},
    enabled: Boolean = true
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(
            value = insertUiEvent.namaTerapis,
            onValueChange = { onValueChange(insertUiEvent.copy(namaTerapis = it)) },
            label = { Text("Nama Terapis") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )

        OutlinedTextField(
            value = insertUiEvent.spesialisasi,
            onValueChange = { onValueChange(insertUiEvent.copy(spesialisasi = it)) },
            label = { Text("Spesialisasi") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )

        OutlinedTextField(
            value = insertUiEvent.nomorIzinPraktik,
            onValueChange = { onValueChange(insertUiEvent.copy(nomorIzinPraktik = it)) },
            label = { Text("Nomor Izin Praktik") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )

        Divider(
            thickness = 8.dp,
            modifier = Modifier.padding(12.dp)
        )
    }
}
