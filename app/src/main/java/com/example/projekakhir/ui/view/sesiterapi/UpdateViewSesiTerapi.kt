package com.example.projekakhir.ui.view.sesiterapi

import FormInputSesiTerapi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projekakhir.model.JenisTerapi
import com.example.projekakhir.model.Pasien
import com.example.projekakhir.model.Terapis
import com.example.projekakhir.navigation.DestinasiNavigasi
import com.example.projekakhir.ui.custom.CostumeTopAppBar
import com.example.projekakhir.ui.viewmodel.PenyediaViewModel
import com.example.projekakhir.ui.viewmodel.sesiterapi.InsertSesiTerapiUiEvent
import com.example.projekakhir.ui.viewmodel.sesiterapi.InsertSesiTerapiUiState
import com.example.projekakhir.ui.viewmodel.sesiterapi.UpdateSesiTerapiViewModel
import kotlinx.coroutines.launch

object DestinasiUpdateSesiTerapi : DestinasiNavigasi {
    override val route = "update sesi"
    const val idsesi = "idsesi"
    val routeWithArg = "$route/{$idsesi}"
    override val titleRes = "Update Sesi"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateSesiTerapiView(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: UpdateSesiTerapiViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    val uiState = viewModel.uiState
    val terapisList = viewModel.listTerapis
    val jenisTerapiList = viewModel.listJnsTerapi
    val pasienList = viewModel.listPasien

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CostumeTopAppBar(
                title = DestinasiUpdateSesiTerapi.titleRes,
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
            // Form Body untuk Update Sesi Terapi
            EntryBodySesiTerapi(
                insertUiState = uiState,
                onSesiValueChange = { updatedValue ->
                    viewModel.updateSesiTerapiState(updatedValue)
                },
                onSaveClick = {
                    coroutineScope.launch {
                        viewModel.updateSesiTerapi()
                        navigateBack() // Navigate back after saving
                    }
                },
                jenisTerapiList = jenisTerapiList,
                terapisList = terapisList,
                pasienList = pasienList
            )
        }
    }
}

@Composable
fun EntryBodySesiTerapi(
    insertUiState: InsertSesiTerapiUiState,
    onSesiValueChange: (InsertSesiTerapiUiEvent) -> Unit,
    onSaveClick: () -> Unit,
    jenisTerapiList: List<JenisTerapi>,
    terapisList: List<Terapis>,
    pasienList: List<Pasien>,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(18.dp),
        modifier = modifier
    ) {
        // Form Input untuk Sesi Terapi
        FormInputSesiTerapi(
            insertUiEvent = insertUiState.insertUiEvent,
            onValueChange = onSesiValueChange,
            jenisTerapiList = jenisTerapiList,
            terapisList = terapisList,
            pasienList = pasienList,
            modifier = Modifier.fillMaxWidth()
        )

        // Button untuk Simpan Data
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
