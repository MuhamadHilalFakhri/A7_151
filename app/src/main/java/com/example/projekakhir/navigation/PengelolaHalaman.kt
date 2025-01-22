package com.example.projekakhir.navigation

import DetailTerapisView
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.projekakhir.ui.view.HalamanUtama
import com.example.projekakhir.ui.view.pasien.DestinasiDetail
import com.example.projekakhir.ui.view.pasien.DestinasiEntryPasien
import com.example.projekakhir.ui.view.pasien.DestinasiHome
import com.example.projekakhir.ui.view.pasien.DestinasiUpdate
import com.example.projekakhir.ui.view.pasien.DetailPasienView
import com.example.projekakhir.ui.view.pasien.EntryPasienScreen
import com.example.projekakhir.ui.view.pasien.HomeScreenPasien
import com.example.projekakhir.ui.view.pasien.UpdatePasienView
import com.example.projekakhir.ui.view.sesiterapi.DestinasiEntryTerapis
import com.example.projekakhir.ui.view.sesiterapi.DestinasiHomeTerapis
import com.example.projekakhir.ui.view.sesiterapi.DestinasiUpdateTerapis
import com.example.projekakhir.ui.view.sesiterapi.EntryTerapisScreen
import com.example.projekakhir.ui.view.sesiterapi.HomeScreenTerapis
import com.example.projekakhir.ui.view.sesiterapi.UpdateTerapisView
import com.example.projekakhir.ui.viewmodel.PenyediaViewModel

@Composable
fun PengelolaHalaman(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = HalamanUtama.route, // Set HalamanUtama sebagai halaman pertama
        modifier = Modifier,
    ) {
        composable(HalamanUtama.route) {
            HalamanUtama(navController = navController) // Menambahkan HalamanUtama di NavHost
        }
        composable(DestinasiHome.route) {
            HomeScreenPasien(
                navigateToItemEntry = { navController.navigate(DestinasiEntryPasien.route) },
                onDetailClick = { idPasien ->
                    navController.navigate("${DestinasiDetail.route}/$idPasien")
                }
            )
        }
        composable(DestinasiHomeTerapis.route) {
            HomeScreenTerapis(
                navigateToItemEntry = { navController.navigate(DestinasiEntryTerapis.route) },
                onDetailClick = { idTerapis ->
                    navController.navigate("${DestinasiDetailTerapis.route}/$idTerapis")
                }
            )
        }
        composable(DestinasiEntryPasien.route) {
            EntryPasienScreen(navigateBack = { navController.popBackStack() })
        }
        composable(DestinasiEntryTerapis.route) {
            EntryTerapisScreen(navigateBack = { navController.popBackStack() })
        }
        composable(
            DestinasiDetail.routeWithArg,
            arguments = listOf(navArgument(DestinasiDetail.idpasien) { type = NavType.IntType })
        ) { backStackEntry ->
            val idPasien = backStackEntry.arguments?.getInt(DestinasiDetail.idpasien) ?: -1
            DetailPasienView(
                idPasien = idPasien,
                navigateBack = { navController.popBackStack() },
                onEditClick = { id -> navController.navigate("${DestinasiUpdate.route}/$id") }
            )
        }
        composable(
            DestinasiDetailTerapis.routeWithArg,
            arguments = listOf(navArgument(DestinasiDetailTerapis.ID_TERAPIS) { type = NavType.IntType })
        ) { backStackEntry ->
            val idTerapis = backStackEntry.arguments?.getInt(DestinasiDetailTerapis.ID_TERAPIS) ?: -1
            DetailTerapisView(
                idTerapis = idTerapis,
                navigateBack = { navController.popBackStack() },
                onEditClick = { id ->
                    navController.navigate(DestinasiUpdateTerapis.routeWithArg.replace("{id_terapis}", id.toString()))
                }
            )
        }
        composable(
            DestinasiUpdateTerapis.routeWithArg,
            arguments = listOf(navArgument(DestinasiUpdateTerapis.ID_TERAPIS) { type = NavType.IntType })
        ) { backStackEntry ->
            val idTerapis = backStackEntry.arguments?.getInt(DestinasiUpdateTerapis.ID_TERAPIS) ?: -1
            UpdateTerapisView(
                navigateBack = { navController.popBackStack() },
                modifier = Modifier,
                viewModel = viewModel(factory = PenyediaViewModel.Factory)
            )
        }
    }

}
