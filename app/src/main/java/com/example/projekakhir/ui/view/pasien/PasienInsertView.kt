package com.example.projekakhir.ui.view.pasien

import android.app.DatePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projekakhir.navigation.DestinasiNavigasi
import com.example.projekakhir.ui.custom.CostumeTopAppBar
import com.example.projekakhir.ui.viewmodel.PenyediaViewModel
import com.example.projekakhir.ui.viewmodel.pasien.InsertPasienUiEvent
import com.example.projekakhir.ui.viewmodel.pasien.InsertPasienUiState
import com.example.projekakhir.ui.viewmodel.pasien.InsertPasienViewModel
import kotlinx.coroutines.launch
import java.util.Calendar

object DestinasiEntryPasien : DestinasiNavigasi {
    override val route = "insert pasien"
    override val titleRes = "Insert Pasien"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntryPasienScreen(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: InsertPasienViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CostumeTopAppBar(
                title = DestinasiEntryPasien.titleRes,
                canNavigateBack = true,
                scrollBehavior = scrollBehavior,
                navigateUp = navigateBack
            )
        }
    ) { innerPadding ->
        EntryBodyPasien(
            insertUiState = viewModel.uiState,
            onPasienValueChange = viewModel::updateInsertPasienState,
            onSaveClick = {
                coroutineScope.launch {
                    viewModel.insertPasien()
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
fun EntryBodyPasien(
    insertUiState: InsertPasienUiState,
    onPasienValueChange: (InsertPasienUiEvent) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showValidationDialog by remember { mutableStateOf(false) }
    var isFormValid by remember { mutableStateOf(true) }

    Column(
        verticalArrangement = Arrangement.spacedBy(18.dp),
        modifier = modifier.padding(12.dp)
    ) {
        FormInputPasien(
            insertUiEvent = insertUiState.insertUiEvent,
            onValueChange = onPasienValueChange,
            modifier = Modifier.fillMaxWidth(),
            onValidationChange = { isValid -> isFormValid = isValid }
        )

        Button(
            onClick = {
                if (isFormValid) {
                    showValidationDialog = true
                }
            },
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth(),
            enabled = isFormValid
        ) {
            Text(text = "Simpan")
        }

        if (showValidationDialog) {
            AlertDialog(
                onDismissRequest = { showValidationDialog = false },
                title = { Text("Konfirmasi") },
                text = { Text("Apakah data sudah benar?") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showValidationDialog = false
                            onSaveClick()
                        }
                    ) {
                        Text("Ya")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showValidationDialog = false }) {
                        Text("Tidak")
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormInputPasien(
    insertUiEvent: InsertPasienUiEvent,
    modifier: Modifier = Modifier,
    onValueChange: (InsertPasienUiEvent) -> Unit = {},
    onValidationChange: (Boolean) -> Unit
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    // DatePickerDialog initialization
    val datePickerDialog = remember {
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                val formattedDate = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth)
                onValueChange(insertUiEvent.copy(tanggalLahir = formattedDate))
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
    }

    var isNamaPasienValid by remember { mutableStateOf(true) }
    var isAlamatValid by remember { mutableStateOf(true) }
    var isNomorTeleponValid by remember { mutableStateOf(true) }
    var isTanggalLahirValid by remember { mutableStateOf(true) }
    var isRiwayatMedikalValid by remember { mutableStateOf(true) } // Validation for riwayatMedikal

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(
            value = insertUiEvent.namaPasien,
            onValueChange = {
                isNamaPasienValid = it.isNotBlank()
                onValueChange(insertUiEvent.copy(namaPasien = it))
            },
            label = { Text("Nama Pasien") },
            modifier = Modifier.fillMaxWidth(),
            isError = !isNamaPasienValid,
            singleLine = true
        )

        OutlinedTextField(
            value = insertUiEvent.alamat,
            onValueChange = {
                isAlamatValid = it.isNotBlank()
                onValueChange(insertUiEvent.copy(alamat = it))
            },
            label = { Text("Alamat") },
            modifier = Modifier.fillMaxWidth(),
            isError = !isAlamatValid,
            singleLine = true
        )

        OutlinedTextField(
            value = insertUiEvent.nomorTelepon,
            onValueChange = {
                isNomorTeleponValid = it.isNotBlank()
                onValueChange(insertUiEvent.copy(nomorTelepon = it))
            },
            label = { Text("Nomor Telepon") },
            modifier = Modifier.fillMaxWidth(),
            isError = !isNomorTeleponValid,
            singleLine = true
        )

        OutlinedTextField(
            value = insertUiEvent.tanggalLahir,
            onValueChange = {},
            label = { Text("Tanggal Lahir") },
            modifier = Modifier
                .fillMaxWidth()
                .clickable { datePickerDialog.show() },
            isError = !isTanggalLahirValid,
            singleLine = true,
            enabled = false
        )

        OutlinedTextField(
            value = insertUiEvent.riwayatMedikal,
            onValueChange = {
                isRiwayatMedikalValid = it.isNotBlank() // Validation for riwayatMedikal
                onValueChange(insertUiEvent.copy(riwayatMedikal = it))
            },
            label = { Text("Riwayat Medikal") },
            modifier = Modifier.fillMaxWidth(),
            isError = !isRiwayatMedikalValid, // Show error state if invalid
            singleLine = true
        )

        // Call validation callback after all fields are checked
        onValidationChange(
            isNamaPasienValid &&
                    isAlamatValid &&
                    isNomorTeleponValid &&
                    isTanggalLahirValid &&
                    isRiwayatMedikalValid && // Include validation for riwayatMedikal
                    insertUiEvent.tanggalLahir.isNotBlank()
        )
    }
}
