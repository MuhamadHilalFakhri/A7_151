package com.example.projekakhir.ui.view.jenisterapi

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projekakhir.navigation.DestinasiNavigasi
import com.example.projekakhir.ui.customwidget.CostumeTopAppBar
import com.example.projekakhir.ui.viewmodel.PenyediaViewModel
import com.example.projekakhir.ui.viewmodel.jenisterapi.InsertJenisTerapiUiEvent
import com.example.projekakhir.ui.viewmodel.jenisterapi.InsertJenisTerapiUiState
import com.example.projekakhir.ui.viewmodel.jenisterapi.InsertJenisTerapiViewModel
import kotlinx.coroutines.launch

object DestinasiEntryJenisTerapi : DestinasiNavigasi {
    override val route = "item_entry_jenis_terapi"
    override val titleRes = "Entry Jenis Terapi"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntryJenisTerapiScreen(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: InsertJenisTerapiViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    // States for validation and confirmation dialog
    val showValidationError = rememberSaveable { mutableStateOf(false) }
    val showConfirmationDialog = rememberSaveable { mutableStateOf(false) }

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CostumeTopAppBar(
                title = DestinasiEntryJenisTerapi.titleRes,
                canNavigateBack = true,
                scrollBehavior = scrollBehavior,
                navigateUp = navigateBack,
                showRefreshIcon = false
            )
        }
    ) { innerPadding ->
        EntryBodyJenisTerapi(
            insertUiState = viewModel.uiState,
            onJenisTerapiValueChange = viewModel::updateInsertJenisTerapiState,
            onSaveClick = {
                val event = viewModel.uiState.insertUiEvent
                if (event.namaJenisTerapi.isBlank() || event.deskripsiTerapi.isBlank()) {
                    showValidationError.value = true
                } else {
                    showConfirmationDialog.value = true
                }
            },
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .fillMaxWidth()
        )
    }

    // Validation Error Dialog
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

    // Confirmation Dialog
    if (showConfirmationDialog.value) {
        AlertDialog(
            onDismissRequest = { showConfirmationDialog.value = false },
            title = { Text("Konfirmasi") },
            text = { Text("Apakah data sudah benar?") },
            confirmButton = {
                Button(onClick = {
                    showConfirmationDialog.value = false
                    coroutineScope.launch {
                        viewModel.insertJenisTerapi()
                        navigateBack()
                    }
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

@Composable
fun EntryBodyJenisTerapi(
    insertUiState: InsertJenisTerapiUiState,
    onJenisTerapiValueChange: (InsertJenisTerapiUiEvent) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(18.dp),
        modifier = modifier.padding(12.dp)
    ) {
        FormInputJenisTerapi(
            insertUiEvent = insertUiState.insertUiEvent,
            onValueChange = onJenisTerapiValueChange,
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = onSaveClick,
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF4A90E2), // Warna biru terang untuk background button
                contentColor = Color.White // Warna putih untuk teks
            )
        ) {
            Text(text = "Simpan")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormInputJenisTerapi(
    insertUiEvent: InsertJenisTerapiUiEvent,
    modifier: Modifier = Modifier,
    onValueChange: (InsertJenisTerapiUiEvent) -> Unit = {},
    enabled: Boolean = true
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(
            value = insertUiEvent.namaJenisTerapi,
            onValueChange = { onValueChange(insertUiEvent.copy(namaJenisTerapi = it)) },
            label = { Text("Nama Jenis Terapi") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )

        OutlinedTextField(
            value = insertUiEvent.deskripsiTerapi,
            onValueChange = { onValueChange(insertUiEvent.copy(deskripsiTerapi = it)) },
            label = { Text("Deskripsi Terapi") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )

        if (enabled) {
            Text(
                text = "Isi Semua Data!",
                modifier = Modifier.padding(12.dp)
            )
        }

        Divider(
            thickness = 8.dp,
            modifier = Modifier.padding(12.dp)
        )
    }
}
