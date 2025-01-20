package com.example.projekakhir.ui.view


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.projekakhir.navigation.DestinasiNavigasi
import com.example.projekakhir.ui.view.pasien.DestinasiHome
import com.example.projekakhir.ui.view.sesiterapi.DestinasiHomeTerapis

object HalamanUtama : DestinasiNavigasi {
    override val route = "halaman utama"
    const val home = "id_terapis"
    val routeWithArg = "$route/{$home}"
    override val titleRes = "Halaman Utama"
}
@Composable
fun HalamanUtama(navController: NavController, modifier: Modifier = Modifier) {
    Scaffold(
        modifier = modifier,
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                Row(
                    modifier = Modifier
                        .padding(30.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(modifier = Modifier.padding(start = 20.dp))
                    Column {
                        Text(
                            text = "Universitas Muhammadiyah Yogyakarta",
                            color = Color.White,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Teknologi Informasi",
                            color = Color.White,
                            fontWeight = FontWeight.Light
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                color = Color.White.copy(alpha = 0.9f),
                                shape = RoundedCornerShape(
                                    topEnd = 15.dp,
                                    topStart = 15.dp
                                )
                            )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Text(
                                text = "Pengelolaan Terapis",
                                color = Color.Black,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )

                            // Tombol Pasien
                            Button(
                                onClick = {
                                    navController.navigate(DestinasiHome.route) // Navigasi ke halaman pasien
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF003f5c),
                                    contentColor = Color.White
                                ),
                                modifier = Modifier
                                    .padding(8.dp)
                                    .height(50.dp)
                                    .fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = "Pasien",
                                        fontSize = 18.sp,
                                        modifier = Modifier.weight(1f),
                                        textAlign = TextAlign.Start
                                    )
                                    Icon(
                                        imageVector = Icons.Default.KeyboardArrowRight,
                                        contentDescription = "Arrow End",
                                        tint = Color.White
                                    )
                                }
                            }

                            // Tombol Terapis
                            Button(
                                onClick = {
                                    navController.navigate(DestinasiHomeTerapis.route) // Navigasi ke halaman terapis
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF003f5c),
                                    contentColor = Color.White
                                ),
                                modifier = Modifier
                                    .padding(8.dp)
                                    .height(50.dp)
                                    .fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = "Terapis",
                                        fontSize = 18.sp,
                                        modifier = Modifier.weight(1f),
                                        textAlign = TextAlign.Start
                                    )
                                    Icon(
                                        imageVector = Icons.Default.KeyboardArrowRight,
                                        contentDescription = "Arrow End",
                                        tint = Color.White
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    )
}
