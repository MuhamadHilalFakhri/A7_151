package com.example.projekakhir.ui.view.pasien


import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.projekakhir.R
import com.example.projekakhir.model.Pasien
import com.example.projekakhir.navigation.DestinasiNavigasi
import com.example.projekakhir.ui.customwidget.HamburgerMenu
import com.example.projekakhir.ui.viewmodel.PenyediaViewModel
import com.example.projekakhir.ui.viewmodel.pasien.HomeUiState
import com.example.projekakhir.ui.viewmodel.pasien.HomeViewModelPasien
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

object DestinasiHomePasien : DestinasiNavigasi {
    override val route = "home pasien"
    override val titleRes = "Home Pasien"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenPasien(
    navigateToItemEntry: () -> Unit,
    modifier: Modifier = Modifier,
    onDetailClick: (Int) -> Unit = {},
    viewModel: HomeViewModelPasien = viewModel(factory = PenyediaViewModel.Factory),
    navController: NavController
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            HamburgerMenu(
                title = DestinasiHomePasien.titleRes,
                canNavigateBack = false,
                scrollBehavior = scrollBehavior,
                onRefresh = {
                    viewModel.getPasien()
                },
                navController = navController, // Pass navController here
                currentPage = DestinasiHomePasien.route
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = navigateToItemEntry,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(18.dp),
                containerColor = Color(0xFF4A90E2) // Warna biru terang untuk container
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Pasien",
                    tint = Color.White // Warna putih untuk ikon agar kontras
                )
            }
        },
    ) { innerPadding ->
        HomeStatus(
            homeUiState = viewModel.pasienUIState,
            retryAction = { viewModel.getPasien() },
            modifier = Modifier.padding(innerPadding),
            onDetailClick = onDetailClick,
            onDeleteClick = {
                viewModel.deletePasien(it.id_pasien)
                viewModel.getPasien()
            }
        )
    }
}


@Composable
fun HomeStatus(
    homeUiState: HomeUiState,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier,
    onDeleteClick: (Pasien) -> Unit = {},
    onDetailClick: (Int) -> Unit
) {
    when (homeUiState) {
        is HomeUiState.Loading -> OnLoading(modifier = modifier.fillMaxSize())

        is HomeUiState.Success ->
            if (homeUiState.pasien.isEmpty()) {
                Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "Tidak ada data pasien")
                }
            } else {
                PasienLayout(
                    pasien = homeUiState.pasien,
                    modifier = modifier.fillMaxWidth(),
                    onDetailClick = { onDetailClick(it.id_pasien) },
                    onDeleteClick = { onDeleteClick(it) }
                )
            }
        is HomeUiState.Error -> OnError(retryAction, modifier = modifier.fillMaxSize())
    }
}

@Composable
fun OnLoading(modifier: Modifier = Modifier){
    Image(
        modifier = modifier.size(200.dp),
        painter = painterResource(R.drawable.loading_img),
        contentDescription = stringResource(R.string.loading)
    )
}

@Composable
fun OnError(retryAction:()->Unit, modifier: Modifier = Modifier){
    Column(
        modifier=modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_connection_error), contentDescription = ""
        )

        Text(text = stringResource(R.string.loading_failed), modifier = Modifier.padding(16.dp))
        Button(onClick = retryAction) {
            Text(stringResource(R.string.retry))
        }
    }
}


@Composable
fun PasienLayout(
    pasien: List<Pasien>,
    modifier: Modifier = Modifier,
    onDetailClick: (Pasien) -> Unit,
    onDeleteClick: (Pasien) -> Unit = {}
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(pasien) { pasien ->
            PasienCard(
                pasien = pasien,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onDetailClick(pasien) },
                onDeleteClick = { onDeleteClick(pasien) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasienCard(
    pasien: Pasien,
    modifier: Modifier = Modifier,
    onDeleteClick: (Pasien) -> Unit = {}
) {
    // State to track whether the dialog is visible
    var showConfirmationDialog by remember { mutableStateOf(false) }

    // Format tanggal menggunakan SimpleDateFormat
    val formattedTanggalLahir = remember(pasien.tanggal_lahir) {
        try {
            val utcFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            utcFormat.timeZone = TimeZone.getTimeZone("UTC")
            val parsedDate = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).apply {
                timeZone = TimeZone.getTimeZone("UTC")
            }.parse(pasien.tanggal_lahir)
            parsedDate?.let { utcFormat.format(it) } ?: pasien.tanggal_lahir
        } catch (e: Exception) {
            pasien.tanggal_lahir
        }
    }

    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF003f5c))
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = pasien.nama_pasien,
                    style = MaterialTheme.typography.titleLarge.copy(color = Color.White),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.weight(1f))
                IconButton(onClick = { showConfirmationDialog = true }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = null,
                        tint = Color.White
                    )
                }
            }

            Text(
                text = "Alamat: ${pasien.alamat}",
                style = MaterialTheme.typography.bodyMedium.copy(color = Color.White)
            )
            Text(
                text = "Nomor Telepon: ${pasien.nomor_telepon}",
                style = MaterialTheme.typography.bodyMedium.copy(color = Color.White)
            )
            Text(
                text = "Tanggal Lahir: $formattedTanggalLahir",
                style = MaterialTheme.typography.bodyMedium.copy(color = Color.White)
            )
        }
    }

    // Show confirmation dialog
    if (showConfirmationDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmationDialog = false },
            title = { Text(text = "Konfirmasi") },
            text = { Text(text = "Apakah Anda yakin ingin menghapus data ini?") },
            confirmButton = {
                Button(onClick = {
                    showConfirmationDialog = false
                    onDeleteClick(pasien)
                }) {
                    Text("Hapus")
                }
            },
            dismissButton = {
                Button(onClick = { showConfirmationDialog = false }) {
                    Text("Batal")
                }
            }
        )
    }
}
