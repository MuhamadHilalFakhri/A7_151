package com.example.projekakhir.ui.view.jenisterapi

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.projekakhir.R
import com.example.projekakhir.model.JenisTerapi
import com.example.projekakhir.navigation.DestinasiNavigasi
import com.example.projekakhir.ui.customwidget.HamburgerMenu
import com.example.projekakhir.ui.viewmodel.PenyediaViewModel
import com.example.projekakhir.ui.viewmodel.jenisterapi.HomeUiStateJenisTerapi
import com.example.projekakhir.ui.viewmodel.jenisterapi.HomeViewModelJenisTerapi

object DestinasiHomeJenisTerapi : DestinasiNavigasi {
    override val route = "homejenis"
    override val titleRes = "Home Jenis Terapi"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenJenisTerapi(
    navigateToItemEntry: () -> Unit,
    modifier: Modifier = Modifier,
    onDetailClick: (Int) -> Unit = {},
    viewModel: HomeViewModelJenisTerapi = viewModel(factory = PenyediaViewModel.Factory),
    navController: NavController
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val (showDeleteDialog, setShowDeleteDialog) = remember { mutableStateOf(false) }
    val itemToDelete = remember { mutableStateOf<JenisTerapi?>(null) }

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            HamburgerMenu(
                title = DestinasiHomeJenisTerapi.titleRes,
                canNavigateBack = false,
                scrollBehavior = scrollBehavior,
                onRefresh = {
                    viewModel.getJenisTerapi()
                },
                navController = navController, // Pass navController here
                currentPage = DestinasiHomeJenisTerapi.route
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = navigateToItemEntry,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(18.dp),
                containerColor = Color(0xFF4A90E2)
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Jenis Terapi",
                    tint = Color.White)
            }
        },
    ) { innerPadding ->
        HomeStatusJenisTerapi(
            homeUiState = viewModel.jenisTerapiUIState,
            retryAction = { viewModel.getJenisTerapi() },
            modifier = Modifier.padding(innerPadding),
            onDetailClick = onDetailClick,
            onDeleteClick = { jenisTerapi ->
                itemToDelete.value = jenisTerapi
                setShowDeleteDialog(true)
            }
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
                Text("Apakah Anda yakin ingin menghapus jenis terapi '${itemToDelete.value?.nama_jenis_terapi}'?")
            },
            confirmButton = {
                Button(
                    onClick = {
                        itemToDelete.value?.let { terapi ->
                            viewModel.deleteJenisTerapi(terapi.id_jenis_terapi)
                            viewModel.getJenisTerapi()
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
fun HomeStatusJenisTerapi(
    homeUiState: HomeUiStateJenisTerapi,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier,
    onDeleteClick: (JenisTerapi) -> Unit = {},
    onDetailClick: (Int) -> Unit
) {
    when (homeUiState) {
        is HomeUiStateJenisTerapi.Loading -> OnLoading(modifier = modifier.fillMaxSize())

        is HomeUiStateJenisTerapi.Success ->
            if (homeUiState.jenisTerapi.isEmpty()) {
                Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "Tidak ada data jenis terapi")
                }
            } else {
                JenisTerapiLayout(
                    jenisTerapi = homeUiState.jenisTerapi,
                    modifier = modifier.fillMaxWidth(),
                    onDetailClick = { onDetailClick(it.id_jenis_terapi) },
                    onDeleteClick = { onDeleteClick(it) }
                )
            }
        is HomeUiStateJenisTerapi.Error -> OnError(retryAction, modifier = modifier.fillMaxSize())
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
fun JenisTerapiLayout(
    jenisTerapi: List<JenisTerapi>,
    modifier: Modifier = Modifier,
    onDetailClick: (JenisTerapi) -> Unit,
    onDeleteClick: (JenisTerapi) -> Unit = {}
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(jenisTerapi) { terapi ->
            JenisTerapiCard(
                jenisTerapi = terapi,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onDetailClick(terapi) },
                onDeleteClick = { onDeleteClick(terapi) }
            )
        }
    }
}

@Composable
fun JenisTerapiCard(
    jenisTerapi: JenisTerapi,
    modifier: Modifier = Modifier,
    onDeleteClick: (JenisTerapi) -> Unit = {}
) {
    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF003f5c), // Dark blue color
            contentColor = Color.White // White text color
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = jenisTerapi.nama_jenis_terapi,
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White // White text color
                )
                Spacer(Modifier.weight(1f))
                IconButton(onClick = { onDeleteClick(jenisTerapi) }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = null,
                        tint = Color.White // White delete icon color
                    )
                }
            }

            Text(
                text = "Deskripsi: ${jenisTerapi.deskripsi_terapi}",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White // White text color for description
            )
        }
    }
}
