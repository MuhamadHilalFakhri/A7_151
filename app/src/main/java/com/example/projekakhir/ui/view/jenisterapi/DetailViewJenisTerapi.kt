package com.example.projekakhir.ui.view.jenisterapi

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.projekakhir.navigation.DestinasiNavigasi

object DestinasiDetailJenisTerapi : DestinasiNavigasi {
    override val route = "detail_jenis_terapi"
    const val ID_JENIS_TERAPI = "id_jenis_terapi"
    val routeWithArg = "$route/{$ID_JENIS_TERAPI}"
    override val titleRes = "Detail Jenis Terapi"
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
