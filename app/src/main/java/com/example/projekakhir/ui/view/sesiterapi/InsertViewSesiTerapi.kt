import android.app.DatePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projekakhir.model.JenisTerapi
import com.example.projekakhir.model.Pasien
import com.example.projekakhir.model.Terapis
import com.example.projekakhir.navigation.DestinasiNavigasi
import com.example.projekakhir.ui.custom.CostumeTopAppBar
import com.example.projekakhir.ui.custom.DropdownSelector
import com.example.projekakhir.ui.viewmodel.PenyediaViewModel
import com.example.projekakhir.ui.viewmodel.sesiterapi.InsertSesiTerapiUiEvent
import com.example.projekakhir.ui.viewmodel.sesiterapi.InsertSesiTerapiUiState
import com.example.projekakhir.ui.viewmodel.sesiterapi.InsertSesiTerapiViewModel
import kotlinx.coroutines.launch
import java.util.Calendar

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
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    val terapisList = viewModel.listTerapis
    val jenisTerapiListList = viewModel.listJnsTerapi
    val pasienList = viewModel.listPasien


    remember {
        coroutineScope.launch {
            viewModel.loadExistingData()
        }
    }
    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CostumeTopAppBar(
                title = DestinasiInsertSesiTerapi.titleRes,
                canNavigateBack = true,
                scrollBehavior = scrollBehavior,
                navigateUp = navigateBack
            )
        }
    ) { innerPadding ->
        InsertSesiTerapiBody(
            insertUiState = viewModel.uiState,
            onValueChange = viewModel::updateInsertSesiTerapiState,
            onSaveClick = {
                coroutineScope.launch {
                    viewModel.loadExistingData()
                    viewModel.insertSesiTerapi()
                    navigateBack()
                }
            },
            terapisList = terapisList,
            jenisTerapiList = jenisTerapiListList,
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
        modifier = modifier.padding(12.dp)
    ) {
        // Form Input untuk Sesi Terapi
        FormInputSesiTerapi(
            insertUiEvent = insertUiState.insertUiEvent,
            onValueChange = onValueChange,
            jenisTerapiList = jenisTerapiList,
            terapisList = terapisList,
            pasienList = pasienList,
            modifier = Modifier.fillMaxWidth()
        )

        // Button untuk Simpan Data
        Button(
            onClick = onSaveClick,
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Simpan")
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
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(12.dp)) {

        // Dropdown Pasien
        DropdownSelector(
            label = "Pilih Pasien",
            items = pasienList.map { it.nama_pasien }, // Assuming Pasien has a 'nama' field
            selectedItem = pasienList.find { it.id_pasien == insertUiEvent.idPasien }?.nama_pasien ?: "",
            onItemSelected = { selected ->
                val selectedPasien = pasienList.find { it.nama_pasien == selected }
                selectedPasien?.let {
                    onValueChange(insertUiEvent.copy(idPasien = it.id_pasien))
                }
            }
        )

        // Dropdown Jenis Terapi
        DropdownSelector(
            label = "Pilih Jenis Terapi",
            items = jenisTerapiList.map { it.nama_jenis_terapi }, // Assuming JenisTerapi has a 'nama' field
            selectedItem = jenisTerapiList.find { it.id_jenis_terapi == insertUiEvent.idJenisTerapi }?.nama_jenis_terapi ?: "",
            onItemSelected = { selected ->
                val selectedJenisTerapi = jenisTerapiList.find { it.nama_jenis_terapi == selected }
                selectedJenisTerapi?.let {
                    onValueChange(insertUiEvent.copy(idJenisTerapi = it.id_jenis_terapi))
                }
            }
        )

        // Dropdown Terapis
        DropdownSelector(
            label = "Pilih Terapis",
            items = terapisList.map { it.nama_terapis }, // Assuming Terapis has a 'nama' field
            selectedItem = terapisList.find { it.id_terapis == insertUiEvent.idTerapis }?.nama_terapis ?: "",
            onItemSelected = { selected ->
                val selectedTerapis = terapisList.find { it.nama_terapis == selected }
                selectedTerapis?.let {
                    onValueChange(insertUiEvent.copy(idTerapis = it.id_terapis))
                }
            }
        )

        // Input Tanggal Sesi
        OutlinedTextField(
            value = insertUiEvent.tanggalSesi,
            onValueChange = { onValueChange(insertUiEvent.copy(tanggalSesi = it)) },
            label = { Text("Tanggal Sesi") },
            modifier = Modifier.fillMaxWidth()
        )

        // Input Catatan Sesi
        OutlinedTextField(
            value = insertUiEvent.catatanSesi ?: "",
            onValueChange = { onValueChange(insertUiEvent.copy(catatanSesi = it)) },
            label = { Text("Catatan Sesi") },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 3
        )
    }
}
