package com.example.projekakhir.ui.view.sesiterapi

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.projekakhir.model.SesiTerapi
import com.example.projekakhir.navigation.DestinasiNavigasi
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

object DestinasiHomeSesi : DestinasiNavigasi {
    override val route = "home sesi terapi"
    override val titleRes = "Home Sesi Terapi"
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

