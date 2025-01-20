package com.example.projekakhir.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.projekakhir.ui.view.pasien.DestinasiDetail
import com.example.projekakhir.ui.view.pasien.DestinasiEntryPasien
import com.example.projekakhir.ui.view.pasien.DestinasiHome
import com.example.projekakhir.ui.view.pasien.DestinasiUpdate
import com.example.projekakhir.ui.view.pasien.DetailPasienView
import com.example.projekakhir.ui.view.pasien.EntryPasienScreen
import com.example.projekakhir.ui.view.pasien.HomeScreenPasien
import com.example.projekakhir.ui.view.pasien.UpdatePasienView

@Composable
fun PengelolaHalaman(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = DestinasiHome.route,
        modifier = Modifier,
    ) {
        composable(DestinasiHome.route) {
            HomeScreenPasien(
                navigateToItemEntry = { navController.navigate(DestinasiEntryPasien.route) },
                onDetailClick = { idPasien ->
                    navController.navigate("${DestinasiDetail.route}/$idPasien")
                }
            )
        }
        composable(DestinasiEntryPasien.route) {
            EntryPasienScreen(navigateBack = {
                navController.popBackStack()
            })
        }
        composable(
            DestinasiDetail.routeWithArg,
            arguments = listOf(navArgument(DestinasiDetail.idpasien) { type = NavType.IntType })
        ) { backStackEntry ->
            val idPasien = backStackEntry.arguments?.getInt(DestinasiDetail.idpasien) ?: -1
            DetailPasienView(
                idPasien = idPasien,
                navigateBack = { navController.popBackStack() },
                onEditClick = { id ->
                    navController.navigate("${DestinasiUpdate.route}/$id")
                }
            )
        }
        composable(
            DestinasiUpdate.routesWithArg,
            arguments = listOf(
                navArgument(DestinasiUpdate.idpasien) { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val idPasien = backStackEntry.arguments?.getInt(DestinasiUpdate.idpasien)
            idPasien?.let {
                UpdatePasienView(
                    navigateBack = { navController.popBackStack() },
                    modifier = modifier
                )
            }
        }
    }
}
