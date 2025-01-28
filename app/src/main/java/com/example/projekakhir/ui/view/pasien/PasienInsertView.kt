package com.example.projekakhir.ui.view.pasien

import android.app.DatePickerDialog
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projekakhir.navigation.DestinasiNavigasi
import com.example.projekakhir.ui.customwidget.CostumeTopAppBar
import com.example.projekakhir.ui.viewmodel.PenyediaViewModel
import com.example.projekakhir.ui.viewmodel.pasien.InsertPasienUiEvent
import com.example.projekakhir.ui.viewmodel.pasien.InsertPasienUiState
import com.example.projekakhir.ui.viewmodel.pasien.InsertPasienViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

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
                navigateUp = navigateBack,
                showRefreshIcon = false
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
            enabled = isFormValid,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF4A90E2),
                contentColor = Color.White
            )
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
    var isNamaPasienValid by remember { mutableStateOf(true) }
    var isAlamatValid by remember { mutableStateOf(true) }
    var isNomorTeleponValid by remember { mutableStateOf(true) }
    var isTanggalLahirValid by remember { mutableStateOf(true) }
    var isRiwayatMedikalValid by remember { mutableStateOf(true) }

    val context = LocalContext.current
    val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val calendar = Calendar.getInstance()

    var showDatePickerDialog by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf(insertUiEvent.tanggalLahir) }

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
            singleLine = true,
            leadingIcon = {
                Icon(imageVector = Icons.Default.Person, contentDescription = "Nama Pasien")
            },
            shape = RoundedCornerShape(12.dp)
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
            singleLine = true,
            leadingIcon = {
                Icon(imageVector = Icons.Default.Home, contentDescription = "Alamat")
            },
            shape = RoundedCornerShape(12.dp)
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
            singleLine = true,
            leadingIcon = {
                Icon(imageVector = Icons.Default.Phone, contentDescription = "Nomor Telepon")
            },
            shape = RoundedCornerShape(12.dp)
        )

        OutlinedTextField(
            value = selectedDate,
            onValueChange = { },
            label = { Text("Tanggal Lahir") },
            modifier = Modifier.fillMaxWidth(),
            readOnly = true,
            isError = !isTanggalLahirValid,
            leadingIcon = {
                IconButton(onClick = { showDatePickerDialog = true }) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Pick Date"
                    )
                }
            },
            shape = RoundedCornerShape(12.dp)
        )


        if (showDatePickerDialog) {
            val datePickerDialog = DatePickerDialog(
                context,
                { _, year, month, dayOfMonth ->
                    val selectedCalendar = Calendar.getInstance().apply {
                        set(year, month, dayOfMonth)
                    }
                    val formattedDate = dateFormatter.format(selectedCalendar.time)
                    onValueChange(insertUiEvent.copy(tanggalLahir = formattedDate))
                    selectedDate = formattedDate
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.show()
            showDatePickerDialog = false
        }

        OutlinedTextField(
            value = insertUiEvent.riwayatMedikal,
            onValueChange = {
                isRiwayatMedikalValid = it.isNotBlank()
                onValueChange(insertUiEvent.copy(riwayatMedikal = it))
            },
            label = { Text("Riwayat Medikal") },
            modifier = Modifier.fillMaxWidth(),
            isError = !isRiwayatMedikalValid,
            singleLine = true,
            leadingIcon = {
                Icon(imageVector = Icons.Default.Info, contentDescription = "Riwayat Medikal")
            },
            shape = RoundedCornerShape(12.dp)
        )

        onValidationChange(
            isNamaPasienValid && isAlamatValid && isNomorTeleponValid &&
                    isTanggalLahirValid && insertUiEvent.tanggalLahir.isNotBlank() &&
                    isRiwayatMedikalValid
        )
    }
}




