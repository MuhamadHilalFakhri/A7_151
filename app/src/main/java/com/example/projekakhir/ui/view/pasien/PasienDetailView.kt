package com.example.projekakhir.ui.view.pasien

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import com.example.projekakhir.model.Pasien
import com.example.projekakhir.navigation.DestinasiNavigasi

object DestinasiDetail : DestinasiNavigasi {
    override val route = "detail"
    const val idpasien = "idpasien"
    val routeWithArg = "$route/{$idpasien}"
    override  val titleRes = "Detail Pasien"
}



@Composable
fun ItemDetailPasien(pasien: Pasien) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            ComponentDetailPasien(judul = "ID Pasien", isinya = pasien.id_pasien.toString())
            Spacer(modifier = Modifier.padding(4.dp))
            ComponentDetailPasien(judul = "Nama", isinya = pasien.nama_pasien)
            Spacer(modifier = Modifier.padding(4.dp))
            ComponentDetailPasien(judul = "Alamat", isinya = pasien.alamat)
            Spacer(modifier = Modifier.padding(4.dp))
            ComponentDetailPasien(judul = "Nomor Telepon", isinya = pasien.nomor_telepon)
            Spacer(modifier = Modifier.padding(4.dp))
            ComponentDetailPasien(judul = "Tanggal Lahir", isinya = pasien.tanggal_lahir)
            Spacer(modifier = Modifier.padding(4.dp))
            ComponentDetailPasien(judul = "Riwayat Medikal", isinya = pasien.riwayat_medikal)
        }
    }
}

@Composable
fun ComponentDetailPasien(judul: String, isinya: String) {
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