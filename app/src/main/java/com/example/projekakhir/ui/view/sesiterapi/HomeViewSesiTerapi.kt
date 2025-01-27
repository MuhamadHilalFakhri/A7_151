package com.example.projekakhir.ui.view.sesiterapi

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.example.projekakhir.model.SesiTerapi
import com.example.projekakhir.navigation.DestinasiNavigasi
import com.example.projekakhir.ui.customwidget.HamburgerMenu
import com.example.projekakhir.ui.viewmodel.PenyediaViewModel
import com.example.projekakhir.ui.viewmodel.sesiterapi.HomeUiState
import com.example.projekakhir.ui.viewmodel.sesiterapi.HomeViewModelSesiTerapi
import java.text.SimpleDateFormat
import java.util.*

object DestinasiHomeSesiTerapi : DestinasiNavigasi {
    override val route = "home sesi terapi"
    override val titleRes = "Home Sesi Terapi"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeSesiTerapi(
    navigateToItemEntry: () -> Unit,
    modifier: Modifier = Modifier,
    onDetailClick: (Int) -> Unit = {},
    viewModel: HomeViewModelSesiTerapi = viewModel(factory = PenyediaViewModel.Factory),
    navController: NavController // Added navController as a parameter
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val (showDeleteDialog, setShowDeleteDialog) = remember { mutableStateOf(false) }
    val itemToDelete = remember { mutableStateOf<SesiTerapi?>(null) }

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            HamburgerMenu(
                title = DestinasiHomeSesiTerapi.titleRes,
                canNavigateBack = false,
                scrollBehavior = scrollBehavior,
                onRefresh = {
                    viewModel.getSesiTerapi()
                },
                navController = navController, // Pass navController here
                currentPage = DestinasiHomeSesiTerapi.route // Menambahkan currentPage yang sesuai
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = navigateToItemEntry,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(18.dp),
                containerColor = Color(0xFF4A90E2)
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Sesi Terapi", tint = Color.White)
            }
        },
    ) { innerPadding ->
        HomeStatusSesiTerapi(
            homeUiState = viewModel.sesiTerapiUIState,
            retryAction = { viewModel.getSesiTerapi() },
            modifier = Modifier.padding(innerPadding),
            onDetailClick = onDetailClick,
            onDeleteClick = { sesiTerapi ->
                itemToDelete.value = sesiTerapi
                setShowDeleteDialog(true)
            },
            viewModel = viewModel // Pass the viewModel here
        )
    }


    // Confirmation dialog for deletion
    if (showDeleteDialog && itemToDelete.value != null) {
        AlertDialog(
            onDismissRequest = { setShowDeleteDialog(false) },
            title = {
                Text(text = "Konfirmasi Hapus")
            },
            text = {
                Text("Apakah Anda yakin ingin menghapus sesi terapi ID: ${itemToDelete.value?.id_sesi}?")
            },
            confirmButton = {
                Button(
                    onClick = {
                        itemToDelete.value?.let { sesi ->
                            viewModel.deleteSesiTerapi(sesi.id_sesi)
                            viewModel.getSesiTerapi()
                        }
                        setShowDeleteDialog(false)
                    }
                ) {
                    Text("Hapus")
                }
            },
            dismissButton = {
                Button(onClick = { setShowDeleteDialog(false) }) {
                    Text("Batal")
                }
            }
        )
    }
}


@Composable
fun HomeStatusSesiTerapi(
    homeUiState: HomeUiState,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier,
    onDeleteClick: (SesiTerapi) -> Unit = {},
    onDetailClick: (Int) -> Unit,
    viewModel: HomeViewModelSesiTerapi
) {
    when (homeUiState) {
        is HomeUiState.Loading -> OnLoading(modifier = modifier.fillMaxSize())

        is HomeUiState.Success ->
            if (homeUiState.sesiTerapi.isEmpty()) {
                Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "Tidak ada data sesi terapi")
                }
            } else {
                SesiTerapiLayout(
                    sesiTerapi = homeUiState.sesiTerapi,
                    modifier = modifier.fillMaxWidth(),
                    onDetailClick = { onDetailClick(it.id_sesi) },
                    onDeleteClick = { onDeleteClick(it) },
                    viewModel = viewModel
                )
            }
        is HomeUiState.Error -> OnError(retryAction, modifier = modifier.fillMaxSize())
    }
}

@Composable
fun OnLoading(modifier: Modifier = Modifier) {
    Image(
        modifier = modifier.size(200.dp),
        painter = painterResource(R.drawable.loading_img),
        contentDescription = stringResource(R.string.loading)
    )
}

@Composable
fun OnError(retryAction: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
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
fun SesiTerapiLayout(
    viewModel: HomeViewModelSesiTerapi,
    sesiTerapi: List<SesiTerapi>,
    modifier: Modifier = Modifier,
    onDetailClick: (SesiTerapi) -> Unit,
    onDeleteClick: (SesiTerapi) -> Unit = {}
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(sesiTerapi) { sesi ->
            SesiTerapiCard(
                sesiTerapi = sesi,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onDetailClick(sesi) },
                onDeleteClick = { onDeleteClick(sesi) },
                viewModel = viewModel
            )
        }
    }
}

@Composable
fun SesiTerapiCard(
    sesiTerapi: SesiTerapi,
    viewModel: HomeViewModelSesiTerapi,
    modifier: Modifier = Modifier,
    onDeleteClick: (SesiTerapi) -> Unit = {}
) {
    var isExpanded by remember { mutableStateOf(false) }
    var showConfirmationDialog by remember { mutableStateOf(false) }

    val namaPasien = viewModel.getNamaPasien(sesiTerapi.id_pasien)
    val namaTerapis = viewModel.getNamaTerapis(sesiTerapi.id_terapis)
    val namaJenisTerapi = viewModel.getNamaJenisTerapi(sesiTerapi.id_jenis_terapi)

    val formattedTanggalSesi = remember(sesiTerapi.tanggal_sesi) {
        try {
            val utcFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            utcFormat.timeZone = TimeZone.getTimeZone("UTC")
            val parsedDate = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).apply {
                timeZone = TimeZone.getTimeZone("UTC")
            }.parse(sesiTerapi.tanggal_sesi)
            parsedDate?.let { utcFormat.format(it) } ?: sesiTerapi.tanggal_sesi
        } catch (e: Exception) {
            sesiTerapi.tanggal_sesi
        }
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF003f5c))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Sesi Terapi ID: ${sesiTerapi.id_sesi}",
                    style = MaterialTheme.typography.titleLarge.copy(color = Color.White),
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = { isExpanded = !isExpanded }) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.ArrowDropDown,
                        contentDescription = null,
                        tint = Color.White
                    )
                }
                IconButton(onClick = { showConfirmationDialog = true }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = null,
                        tint = Color(0xFFF95959)
                    )
                }
            }
            if (isExpanded) {
                Column(modifier = Modifier.padding(8.dp)) {
                    Text("Nama Pasien: $namaPasien", color = Color.White)
                    Text("Nama Terapis: $namaTerapis", color = Color.White)
                    Text("Jenis Terapi: $namaJenisTerapi", color = Color.White)
                    Text("Tanggal Sesi: $formattedTanggalSesi", color = Color.White)
                    Text("Catatan: ${sesiTerapi.catatan_sesi ?: "Tidak ada catatan"}", color = Color.White)
                }
            }
        }
    }

    if (showConfirmationDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmationDialog = false },
            title = { Text("Konfirmasi") },
            text = { Text("Apakah Anda yakin ingin menghapus sesi terapi ID: ${sesiTerapi.id_sesi}?") },
            confirmButton = {
                Button(onClick = {
                    showConfirmationDialog = false
                    onDeleteClick(sesiTerapi)
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
