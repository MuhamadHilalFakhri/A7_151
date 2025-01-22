package com.example.projekakhir.ui.view.jenisterapi

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projekakhir.model.JenisTerapi
import com.example.projekakhir.navigation.DestinasiNavigasi
import com.example.projekakhir.ui.viewmodel.sesiterapi.DetailJenisTerapiUiState

object DestinasiDetailJenisTerapi : DestinasiNavigasi {
    override val route = "detail_jenis_terapi"
    const val ID_JENIS_TERAPI = "id_jenis_terapi"
    val routeWithArg = "$route/{$ID_JENIS_TERAPI}"
    override val titleRes = "Detail Jenis Terapi"
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
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
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
            color = Color.Gray
        )

        Text(
            text = isinya,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
