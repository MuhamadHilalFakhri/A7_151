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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.projekakhir.R
import com.example.projekakhir.model.JenisTerapi
import com.example.projekakhir.navigation.DestinasiNavigasi
import com.example.projekakhir.ui.viewmodel.sesiterapi.HomeUiStateJenisTerapi

object DestinasiHomeJenisTerapi : DestinasiNavigasi {
    override val route = "homejenis"
    override val titleRes = "Home Jenis Terapi"
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
                    text = jenisTerapi.nama_jenis_terapi,
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(Modifier.weight(1f))
                IconButton(onClick = { onDeleteClick(jenisTerapi) }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = null,
                    )
                }
            }

            Text(
                text = "Deskripsi: ${jenisTerapi.deskripsi_terapi}",
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}
