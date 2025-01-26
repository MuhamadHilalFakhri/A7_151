package com.example.projekakhir.ui.view.jenisterapi

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projekakhir.model.JenisTerapi
import com.example.projekakhir.navigation.DestinasiNavigasi
import com.example.projekakhir.ui.custom.CostumeTopAppBar
import com.example.projekakhir.ui.viewmodel.PenyediaViewModel
import com.example.projekakhir.ui.viewmodel.jenisterapi.DetailJenisTerapiUiState
import com.example.projekakhir.ui.viewmodel.jenisterapi.DetailJenisTerapiViewModel

object DestinasiDetailJenisTerapi : DestinasiNavigasi {
    override val route = "detail_jenis_terapi"
    const val ID_JENIS_TERAPI = "id_jenis_terapi"
    val routeWithArg = "$route/{$ID_JENIS_TERAPI}"
    override val titleRes = "Detail Jenis Terapi"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailJenisTerapiView(
    idJenisTerapi: Int,
    modifier: Modifier = Modifier,
    viewModel: DetailJenisTerapiViewModel = viewModel(factory = PenyediaViewModel.Factory),
    onEditClick: (Int) -> Unit = {},  // Parameter onEditClick untuk navigasi ke update
    navigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            CostumeTopAppBar(
                title = DestinasiDetailJenisTerapi.titleRes,
                canNavigateBack = true,
                navigateUp = navigateBack,
                onRefresh = { viewModel.getDetailJenisTerapi() }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    onEditClick(idJenisTerapi) // Menggunakan idJenisTerapi untuk navigasi ke update
                },
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(16.dp),
                containerColor = Color(0xFF4A90E2)
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit Jenis Terapi",
                    tint = Color.White
                )
            }
        }
    ) { innerPadding ->
        val detailUiState by viewModel.detailUiState.collectAsState()

        BodyDetailJenisTerapi(
            modifier = Modifier.padding(innerPadding),
            detailUiState = detailUiState,
            retryAction = { viewModel.getDetailJenisTerapi() }
        )
    }
}

@Composable
fun BodyDetailJenisTerapi(
    modifier: Modifier = Modifier,
    detailUiState: DetailJenisTerapiUiState,
    retryAction: () -> Unit = {}
) {
    when (detailUiState) {
        is DetailJenisTerapiUiState.Loading -> {
            OnLoading(modifier = modifier.fillMaxSize())
        }

        is DetailJenisTerapiUiState.Success -> {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                ItemDetailJenisTerapi(jenisTerapi = detailUiState.jenisTerapi)
            }
        }

        is DetailJenisTerapiUiState.Error -> {
            OnError(
                retryAction = retryAction,
                modifier = modifier.fillMaxSize()
            )
        }

        else -> {
            Text("Unexpected")
        }
    }
}

@Composable
fun ItemDetailJenisTerapi(
    jenisTerapi: JenisTerapi
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF003f5c), // Dark blue color
            contentColor = Color.White // White text color
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            ComponentDetailJenisTerapi(judul = "ID Jenis Terapi", isinya = jenisTerapi.id_jenis_terapi.toString())
            Spacer(modifier = Modifier.padding(4.dp))
            ComponentDetailJenisTerapi(judul = "Nama Jenis Terapi", isinya = jenisTerapi.nama_jenis_terapi)
            Spacer(modifier = Modifier.padding(4.dp))
            ComponentDetailJenisTerapi(judul = "Deskripsi Terapi", isinya = jenisTerapi.deskripsi_terapi)
        }
    }
}

@Composable
fun ComponentDetailJenisTerapi(
    judul: String,
    isinya: String
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "$judul :",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White // White color for title
        )

        Text(
            text = isinya,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White // White text color
        )
    }
}

