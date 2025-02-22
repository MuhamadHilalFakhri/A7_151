import android.app.DatePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projekakhir.model.JenisTerapi
import com.example.projekakhir.model.Pasien
import com.example.projekakhir.model.Terapis
import com.example.projekakhir.navigation.DestinasiNavigasi
import com.example.projekakhir.ui.customwidget.CostumeTopAppBar
import com.example.projekakhir.ui.customwidget.Dropdown
import com.example.projekakhir.ui.viewmodel.PenyediaViewModel
import com.example.projekakhir.ui.viewmodel.sesiterapi.InsertSesiTerapiUiEvent
import com.example.projekakhir.ui.viewmodel.sesiterapi.InsertSesiTerapiUiState
import com.example.projekakhir.ui.viewmodel.sesiterapi.InsertSesiTerapiViewModel
import com.example.projekakhir.ui.viewmodel.sesiterapi.showConfirmationDialog
import com.example.projekakhir.ui.viewmodel.sesiterapi.showErrorDialog
import com.example.projekakhir.ui.viewmodel.sesiterapi.validateFields
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object DestinasiInsertSesiTerapi : DestinasiNavigasi {
    override val route = "insert_sesi_terapi"
    override val titleRes = "Insert Sesi Terapi"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InsertSesiTerapiScreen(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: InsertSesiTerapiViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    val terapisList = viewModel.listTerapis
    val jenisTerapiList = viewModel.listJnsTerapi
    val pasienList = viewModel.listPasien

    LaunchedEffect(Unit) {
        viewModel.loadExistingData()
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            CostumeTopAppBar(
                title = DestinasiInsertSesiTerapi.titleRes,
                canNavigateBack = true,
                navigateUp = navigateBack,
                showRefreshIcon = false
            )
        }
    ) { innerPadding ->
        InsertSesiTerapiBody(
            insertUiState = viewModel.uiState,
            onValueChange = viewModel::updateInsertSesiTerapiState,
            onSaveClick = {
                if (validateFields(viewModel.uiState.insertUiEvent)) {
                    showConfirmationDialog(context, onConfirm = {
                        coroutineScope.launch {
                            viewModel.insertSesiTerapi()
                            navigateBack()
                        }
                    })
                } else {
                    showErrorDialog(context)
                }
            },
            terapisList = terapisList,
            jenisTerapiList = jenisTerapiList,
            pasienList = pasienList,
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .fillMaxWidth()
        )
    }
}

@Composable
fun InsertSesiTerapiBody(
    insertUiState: InsertSesiTerapiUiState,
    onValueChange: (InsertSesiTerapiUiEvent) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier,
    jenisTerapiList: List<JenisTerapi>,
    terapisList: List<Terapis>,
    pasienList: List<Pasien>,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(18.dp),
        modifier = modifier
            .padding(12.dp)
            .fillMaxWidth()
    ) {
        FormInputSesiTerapi(
            insertUiEvent = insertUiState.insertUiEvent,
            onValueChange = onValueChange,
            jenisTerapiList = jenisTerapiList,
            terapisList = terapisList,
            pasienList = pasienList,
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = onSaveClick,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF4A90E2),
                contentColor = Color.White
            )
        ) {
            Text(text = "Simpan", style = MaterialTheme.typography.bodyLarge)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormInputSesiTerapi(
    insertUiEvent: InsertSesiTerapiUiEvent,
    onValueChange: (InsertSesiTerapiUiEvent) -> Unit,
    jenisTerapiList: List<JenisTerapi>,
    terapisList: List<Terapis>,
    pasienList: List<Pasien>,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val calendar = Calendar.getInstance()

    var showDatePickerDialog by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf(insertUiEvent.tanggalSesi) }

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier
    ) {
        // Dropdown Pasien
        Dropdown(
            label = "Pilih Pasien",
            items = pasienList.map { it.nama_pasien },
            currentItem = pasienList.find { it.id_pasien == insertUiEvent.idPasien }?.nama_pasien ?: "",
            onItemSelected = { selected ->
                val selectedPasien = pasienList.find { it.nama_pasien == selected }
                selectedPasien?.let {
                    onValueChange(insertUiEvent.copy(idPasien = it.id_pasien))
                }
            }
        )

        if (insertUiEvent.idPasien == 0) {
            Text("* Pasien harus dipilih", color = Color.Red, style = MaterialTheme.typography.bodySmall)
        }

        // Dropdown Jenis Terapi
        Dropdown(
            label = "Pilih Jenis Terapi",
            items = jenisTerapiList.map { it.nama_jenis_terapi },
            currentItem = jenisTerapiList.find { it.id_jenis_terapi == insertUiEvent.idJenisTerapi }?.nama_jenis_terapi ?: "",
            onItemSelected = { selected ->
                val selectedJenisTerapi = jenisTerapiList.find { it.nama_jenis_terapi == selected }
                selectedJenisTerapi?.let {
                    onValueChange(insertUiEvent.copy(idJenisTerapi = it.id_jenis_terapi))
                }
            }
        )

        if (insertUiEvent.idJenisTerapi == 0) {
            Text("* Jenis terapi harus dipilih", color = Color.Red, style = MaterialTheme.typography.bodySmall)
        }

        // Dropdown Terapis
        Dropdown(
            label = "Pilih Terapis",
            items = terapisList.map { it.nama_terapis },
            currentItem = terapisList.find { it.id_terapis == insertUiEvent.idTerapis }?.nama_terapis ?: "",
            onItemSelected = { selected ->
                val selectedTerapis = terapisList.find { it.nama_terapis == selected }
                selectedTerapis?.let {
                    onValueChange(insertUiEvent.copy(idTerapis = it.id_terapis))
                }
            }
        )

        if (insertUiEvent.idTerapis == 0) {
            Text("* Terapis harus dipilih", color = Color.Red, style = MaterialTheme.typography.bodySmall)
        }

        // Date Picker Button
        OutlinedTextField(
            value = selectedDate,
            onValueChange = { },
            label = { Text("Tanggal Sesi") },
            modifier = Modifier.fillMaxWidth(),
            readOnly = true,
            leadingIcon = {
                IconButton(onClick = { showDatePickerDialog = true }) {
                    Icon(imageVector = Icons.Default.DateRange, contentDescription = "Pick Date")
                }
            },
            shape = RoundedCornerShape(12.dp)
        )

        if (selectedDate.isEmpty()) {
            Text("* Tanggal sesi harus dipilih", color = Color.Red, style = MaterialTheme.typography.bodySmall)
        }

        if (showDatePickerDialog) {
            val datePickerDialog = DatePickerDialog(
                context,
                { _, year, month, dayOfMonth ->
                    val selectedCalendar = Calendar.getInstance().apply {
                        set(year, month, dayOfMonth)
                    }
                    val formattedDate = dateFormatter.format(selectedCalendar.time)
                    onValueChange(insertUiEvent.copy(tanggalSesi = formattedDate))
                    selectedDate = formattedDate
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.show()
            showDatePickerDialog = false
        }

        // Input Catatan Sesi
        OutlinedTextField(
            value = insertUiEvent.catatanSesi ?: "",
            onValueChange = { onValueChange(insertUiEvent.copy(catatanSesi = it)) },
            label = { Text("Catatan Sesi") },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 3,
            leadingIcon = {
                Icon(imageVector = Icons.Default.Info, contentDescription = "Catatan Sesi")
            },
            shape = RoundedCornerShape(12.dp)
        )
    }
}


