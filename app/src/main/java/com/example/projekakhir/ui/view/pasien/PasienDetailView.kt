package com.example.projekakhir.ui.view.pasien

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowRight
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
import androidx.navigation.NavController
import com.example.projekakhir.model.Pasien
import com.example.projekakhir.navigation.DestinasiNavigasi
import com.example.projekakhir.ui.customwidget.CostumeTopAppBar
import com.example.projekakhir.ui.view.sesiterapi.DestinasiHomeSesiTerapi
import com.example.projekakhir.ui.viewmodel.PenyediaViewModel
import com.example.projekakhir.ui.viewmodel.pasien.DetailPasienViewModel
import com.example.projekakhir.ui.viewmodel.pasien.DetailUiState
import java.text.SimpleDateFormat
import java.util.Locale

object DestinasiDetail : DestinasiNavigasi {
    override val route = "detail pasien"
    const val idpasien = "idpasien"
    val routeWithArg = "$route/{$idpasien}"
    override val titleRes = "Detail Pasien"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailPasienView(
    idPasien: Int,
    navController: NavController, // Pass the NavController instance
    modifier: Modifier = Modifier,
    viewModel: DetailPasienViewModel = viewModel(factory = PenyediaViewModel.Factory),
    onEditClick: (Int) -> Unit = {}, // Function to navigate to update pasien
    onTambahSesiClick: (Int) -> Unit = {}, // Function to navigate to add therapy session
    navigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            CostumeTopAppBar(
                title = DestinasiDetail.titleRes,
                canNavigateBack = true,
                navigateUp = navigateBack,
                onRefresh = { viewModel.getDetailPasien() }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(DestinasiHomeSesiTerapi.route) // Navigate to therapy session page
                },
                containerColor = Color(0xFF003f5c),
                modifier = Modifier
                    .padding(16.dp), // Added padding around the FAB
                shape = MaterialTheme.shapes.medium // Rounded shape for the FAB
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = "Arrow End",
                    tint = Color.White
                )
            }
        }
    ) { innerPadding ->
        val detailUiState by viewModel.detailUiState.collectAsState()

        BodyDetailPasien(
            modifier = Modifier.padding(innerPadding),
            detailUiState = detailUiState,
            retryAction = { viewModel.getDetailPasien() },
            onEditClick = onEditClick,
            navController = navController // Pass the actual NavController instance
        )
    }
}



@Composable
fun BodyDetailPasien(
    modifier: Modifier = Modifier,
    detailUiState: DetailUiState,
    retryAction: () -> Unit = {},
    onEditClick: (Int) -> Unit = {},
    navController: NavController // Parameter navigasi
) {
    when (detailUiState) {
        is DetailUiState.Loading -> {
            OnLoading(modifier = modifier.fillMaxSize())
        }

        is DetailUiState.Success -> {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween // Mengatur agar elemen saling berjauhan secara vertikal
            ) {
                // Bagian Detail Data
                Column(modifier = Modifier.weight(1f)) {
                    ItemDetailPasien(
                        pasien = detailUiState.pasien,
                        onEditClick = onEditClick // Navigasi ke halaman update pasien
                    )
                }

                // Tombol berada di bawah container detail
//                FloatingActionButton(
//                    onClick = {
//                        navController.navigate(DestinasiHomeSesiTerapi.route) // Navigasi ke halaman sesi terapi
//                    },
//                    containerColor = Color(0xFF003f5c),
//                    modifier = Modifier
//                        .padding(16.dp)
//                        .align(Alignment.BottomEnd), // Menempatkan FAB di kanan bawah
//                    shape = MaterialTheme.shapes.medium // Bentuk bulat default FAB
//                ) {
//                    Icon(
//                        imageVector = Icons.Default.KeyboardArrowRight,
//                        contentDescription = "Arrow End",
//                        tint = Color.White
//                    )
//                }

            }
        }

        is DetailUiState.Error -> {
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
fun ItemDetailPasien(
    pasien: Pasien,
    onEditClick: (Int) -> Unit = {} // Navigasi ke halaman update pasien
) {
    val formattedTanggalLahir = remember(pasien.tanggal_lahir) {
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val parsedDate = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).parse(pasien.tanggal_lahir)
        parsedDate?.let { format.format(it) } ?: pasien.tanggal_lahir
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF003f5c), // Warna biru tua
                contentColor = Color.White // Konten berwarna putih
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
                ComponentDetailPasien(judul = "Tanggal Lahir", isinya = formattedTanggalLahir)
                Spacer(modifier = Modifier.padding(4.dp))
                ComponentDetailPasien(judul = "Riwayat Medikal", isinya = pasien.riwayat_medikal)
            }
        }

        Icon(
            imageVector = Icons.Default.Edit,
            contentDescription = "Edit Pasien",
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
                .clickable { onEditClick(pasien.id_pasien) }, // Navigasi ke halaman update pasien
            tint = Color.White
        )
    }
}

@Composable
fun ComponentDetailPasien(
    judul: String,
    isinya: String
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "$judul :",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFB0BEC5) // Warna abu-abu terang untuk judul
        )

        Text(
            text = isinya,
            fontSize = 18.sp,
            fontWeight = FontWeight.Normal,
            color = Color.White // Konten berwarna putih
        )
    }
}
