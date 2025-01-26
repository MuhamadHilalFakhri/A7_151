package com.example.projekakhir.ui.view.sesiterapi

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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projekakhir.model.SesiTerapi
import com.example.projekakhir.navigation.DestinasiNavigasi
import com.example.projekakhir.ui.custom.CostumeTopAppBar
import com.example.projekakhir.ui.viewmodel.PenyediaViewModel
import com.example.projekakhir.ui.viewmodel.sesiterapi.DetailSesiTerapiViewModel
import com.example.projekakhir.ui.viewmodel.sesiterapi.DetailSesiUiState
import java.text.SimpleDateFormat
import java.util.Locale

object DestinasiDetailSesi : DestinasiNavigasi {
    override val route = "detail sesi terapi"
    const val idSesi = "idSesi"
    val routeWithArg = "$route/{$idSesi}"
    override val titleRes = "Detail Sesi Terapi"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailSesiTerapiView(
    idSesi: Int,
    modifier: Modifier = Modifier,
    viewModel: DetailSesiTerapiViewModel = viewModel(factory = PenyediaViewModel.Factory),
    onEditClick: (Int) -> Unit = {}, // Callback for the edit button
    navigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            CostumeTopAppBar(
                title = DestinasiDetailSesi.titleRes,
                canNavigateBack = true,
                navigateUp = navigateBack,
                onRefresh = { viewModel.getDetailSesiTerapi() }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onEditClick(idSesi) }, // Navigate to the edit screen
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(16.dp),
                containerColor = Color(0xFF4A90E2) // Blue color
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit Sesi Terapi",
                    tint = Color.White // Ensure the icon color contrasts with the button color
                )
            }
        }
    ) { innerPadding ->
        val detailUiState by viewModel.detailUiState.collectAsState()

        BodyDetailSesiTerapi(
            modifier = Modifier.padding(innerPadding),
            detailUiState = detailUiState,
            retryAction = { viewModel.getDetailSesiTerapi() }
        )
    }
}

@Composable
fun BodyDetailSesiTerapi(
    modifier: Modifier = Modifier,
    detailUiState: DetailSesiUiState,
    retryAction: () -> Unit = {}
) {
    when (detailUiState) {
        is DetailSesiUiState.Loading -> {
            OnLoading(modifier = modifier.fillMaxSize())
        }

        is DetailSesiUiState.Success -> {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                ItemDetailSesiTerapi(sesiTerapi = detailUiState.sesiTerapi)
            }
        }

        is DetailSesiUiState.Error -> {
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
fun ItemDetailSesiTerapi(
    sesiTerapi: SesiTerapi
) {
    val formattedTanggalSesi = remember(sesiTerapi.tanggal_sesi) {
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val parsedDate = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).parse(sesiTerapi.tanggal_sesi)
        parsedDate?.let { format.format(it) } ?: sesiTerapi.tanggal_sesi
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF003f5c), // Blue color for consistency
            contentColor = Color.White
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            ComponentDetailSesi(judul = "ID Sesi", isinya = sesiTerapi.id_sesi.toString())
            Spacer(modifier = Modifier.padding(4.dp))
            ComponentDetailSesi(judul = "ID Pasien", isinya = sesiTerapi.id_pasien.toString())
            Spacer(modifier = Modifier.padding(4.dp))
            ComponentDetailSesi(judul = "ID Terapis", isinya = sesiTerapi.id_terapis.toString())
            Spacer(modifier = Modifier.padding(4.dp))
            ComponentDetailSesi(judul = "ID Jenis Terapi", isinya = sesiTerapi.id_jenis_terapi.toString())
            Spacer(modifier = Modifier.padding(4.dp))
            ComponentDetailSesi(judul = "Tanggal Sesi", isinya = formattedTanggalSesi) // Display formatted date here
            Spacer(modifier = Modifier.padding(4.dp))
            ComponentDetailSesi(
                judul = "Catatan Sesi",
                isinya = sesiTerapi.catatan_sesi ?: "Tidak ada catatan"
            )
        }
    }
}

@Composable
fun ComponentDetailSesi(
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
            color = Color.Gray
        )

        Text(
            text = isinya,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
