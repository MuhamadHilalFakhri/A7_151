package com.example.projekakhir.ui.view.sesiterapi

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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projekakhir.R
import com.example.projekakhir.model.SesiTerapi
import com.example.projekakhir.navigation.DestinasiNavigasi
import com.example.projekakhir.ui.custom.CostumeTopAppBar
import com.example.projekakhir.ui.viewmodel.PenyediaViewModel
import com.example.projekakhir.ui.viewmodel.sesiterapi.HomeUiState
import com.example.projekakhir.ui.viewmodel.sesiterapi.HomeViewModelSesiTerapi
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

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
    viewModel: HomeViewModelSesiTerapi = viewModel(factory = PenyediaViewModel.Factory)
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CostumeTopAppBar(
                title = DestinasiHomeSesiTerapi.titleRes,
                canNavigateBack = false,
                scrollBehavior = scrollBehavior,
                onRefresh = {
                    viewModel.getSesiTerapi()
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = navigateToItemEntry,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(18.dp)
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Sesi Terapi")
            }
        },
    ) { innerPadding ->
        HomeStatusSesiTerapi(
            homeUiState = viewModel.sesiTerapiUIState,
            retryAction = { viewModel.getSesiTerapi() },
            modifier = Modifier.padding(innerPadding),
            onDetailClick = onDetailClick,
            onDeleteClick = {
                viewModel.deleteSesiTerapi(it.id_sesi)
                viewModel.getSesiTerapi()
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
    onDetailClick: (Int) -> Unit
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
                    onDeleteClick = { onDeleteClick(it) }
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
                onDeleteClick = { onDeleteClick(sesi) }
            )
        }
    }
}


@Composable
fun SesiTerapiCard(
    sesiTerapi: SesiTerapi,
    modifier: Modifier = Modifier,
    onDeleteClick: (SesiTerapi) -> Unit = {}
) {
    // Format tanggal sesi
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
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
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
                    text = "Sesi Terapi ID: ${sesiTerapi.id_sesi}",
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(Modifier.weight(1f))
                IconButton(onClick = { onDeleteClick(sesiTerapi) }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = null,
                    )
                }
            }

            Text(
                text = "Tanggal Sesi: $formattedTanggalSesi",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "Catatan: ${sesiTerapi.catatan_sesi ?: "Tidak ada catatan"}",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

