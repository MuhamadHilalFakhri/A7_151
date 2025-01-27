package com.example.projekakhir.navigation

import DestinasiInsertSesiTerapi
import DetailTerapisView
import InsertSesiTerapiScreen
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
//import com.example.projekakhir.ui.view.HalamanUtama
import com.example.projekakhir.ui.view.jenisterapi.DestinasiDetailJenisTerapi
import com.example.projekakhir.ui.view.jenisterapi.DestinasiEntryJenisTerapi
import com.example.projekakhir.ui.view.jenisterapi.DestinasiHomeJenisTerapi
import com.example.projekakhir.ui.view.jenisterapi.DestinasiUpdateJenisTerapi
import com.example.projekakhir.ui.view.jenisterapi.DetailJenisTerapiView
import com.example.projekakhir.ui.view.jenisterapi.EntryJenisTerapiScreen
import com.example.projekakhir.ui.view.jenisterapi.HomeScreenJenisTerapi
import com.example.projekakhir.ui.view.jenisterapi.UpdateJenisTerapiView
import com.example.projekakhir.ui.view.pasien.DestinasiDetail
import com.example.projekakhir.ui.view.pasien.DestinasiEntryPasien
import com.example.projekakhir.ui.view.pasien.DestinasiHomePasien
import com.example.projekakhir.ui.view.pasien.DestinasiUpdatePasien
import com.example.projekakhir.ui.view.pasien.DetailPasienView
import com.example.projekakhir.ui.view.pasien.EntryPasienScreen
import com.example.projekakhir.ui.view.pasien.HomeScreenPasien
import com.example.projekakhir.ui.view.pasien.UpdatePasienView
import com.example.projekakhir.ui.view.sesiterapi.DestinasiDetailSesi
import com.example.projekakhir.ui.view.sesiterapi.DestinasiHomeSesiTerapi
import com.example.projekakhir.ui.view.sesiterapi.DestinasiUpdateSesiTerapi
import com.example.projekakhir.ui.view.sesiterapi.DetailSesiTerapiView
import com.example.projekakhir.ui.view.sesiterapi.HomeSesiTerapi
import com.example.projekakhir.ui.view.sesiterapi.UpdateSesiTerapiView
import com.example.projekakhir.ui.view.terapis.DestinasiEntryTerapis
import com.example.projekakhir.ui.view.terapis.DestinasiHomeTerapis
import com.example.projekakhir.ui.view.terapis.DestinasiUpdateTerapis
import com.example.projekakhir.ui.view.terapis.EntryTerapisScreen
import com.example.projekakhir.ui.view.terapis.HomeScreenTerapis
import com.example.projekakhir.ui.view.terapis.UpdateTerapisView
import com.example.projekakhir.ui.viewmodel.PenyediaViewModel


@Composable
fun PengelolaHalaman(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController() // Use a single NavController
) {
    NavHost(
        navController = navController,
        startDestination = DestinasiHomePasien.route, // Set HalamanUtama as the start destination
        modifier = modifier,
    ) {
//        composable(DestinasiHomePasien.route) {
//            HalamanUtama(navController = navController)
//        }

        // Home Pasien
        composable(DestinasiHomePasien.route) {
            HomeScreenPasien(
                navigateToItemEntry = { navController.navigate(DestinasiEntryPasien.route) },
                onDetailClick = { idPasien -> navController.navigate("${DestinasiDetail.route}/$idPasien") },
                navController = navController
            )
        }

        // Home Terapis
        composable(DestinasiHomeTerapis.route) {
            HomeScreenTerapis(
                navigateToItemEntry = { navController.navigate(DestinasiEntryTerapis.route) },
                onDetailClick = { idTerapis -> navController.navigate("${DestinasiDetailTerapis.route}/$idTerapis") },
                navController = navController
            )
        }

        // Home Jenis Terapi
        composable(DestinasiHomeJenisTerapi.route) {
            HomeScreenJenisTerapi(
                navigateToItemEntry = { navController.navigate(DestinasiEntryJenisTerapi.route) },
                onDetailClick = { idJenisTerapi -> navController.navigate("${DestinasiDetailJenisTerapi.route}/$idJenisTerapi") },
                navController = navController
            )
        }

        // Home Sesi Terapi
        composable(DestinasiHomeSesiTerapi.route) {
            HomeSesiTerapi(
                navigateToItemEntry = { navController.navigate(DestinasiInsertSesiTerapi.route) },
                onDetailClick = { idJenisTerapi -> navController.navigate("${DestinasiDetailSesi.route}/$idJenisTerapi") },
                navController = navController
            )
        }

        // Entry screens
        composable(DestinasiEntryPasien.route) { EntryPasienScreen(navigateBack = { navController.popBackStack() }) }
        composable(DestinasiEntryTerapis.route) { EntryTerapisScreen(navigateBack = { navController.popBackStack() }) }
        composable(DestinasiEntryJenisTerapi.route) { EntryJenisTerapiScreen(navigateBack = { navController.popBackStack() }) }
        composable(DestinasiInsertSesiTerapi.route) { InsertSesiTerapiScreen(navigateBack = { navController.popBackStack() }) }

        // Detail Pasien
        composable(
            DestinasiDetail.routeWithArg,
            arguments = listOf(navArgument(DestinasiDetail.idpasien) { type = NavType.IntType })
        ) { backStackEntry ->
            val idPasien = backStackEntry.arguments?.getInt(DestinasiDetail.idpasien) ?: -1
            DetailPasienView(
                navController = navController,
                idPasien = idPasien,
                navigateBack = { navController.popBackStack() },
                onEditClick = { id -> navController.navigate("${DestinasiUpdatePasien.route}/$id") }
            )
        }

        // Detail Terapis
        composable(
            DestinasiDetailTerapis.routeWithArg,
            arguments = listOf(navArgument(DestinasiDetailTerapis.ID_TERAPIS) { type = NavType.IntType })
        ) { backStackEntry ->
            val idTerapis = backStackEntry.arguments?.getInt(DestinasiDetailTerapis.ID_TERAPIS) ?: -1
            DetailTerapisView(
                idTerapis = idTerapis,
                navigateBack = { navController.popBackStack() },
                onEditClick = { id -> navController.navigate(DestinasiUpdateTerapis.routeWithArg.replace("{id_terapis}", id.toString())) }
            )
        }

        // Detail Jenis Terapi
        composable(
            DestinasiDetailJenisTerapi.routeWithArg,
            arguments = listOf(navArgument(DestinasiDetailJenisTerapi.ID_JENIS_TERAPI) { type = NavType.IntType })
        ) { backStackEntry ->
            val idJenisTerapi = backStackEntry.arguments?.getInt(DestinasiDetailJenisTerapi.ID_JENIS_TERAPI) ?: -1
            DetailJenisTerapiView(
                idJenisTerapi = idJenisTerapi,
                navigateBack = { navController.popBackStack() },
                onEditClick = { id -> navController.navigate(DestinasiUpdateJenisTerapi.routeWithArg.replace("{${DestinasiUpdateJenisTerapi.ID_JENIS_TERAPI}}", id.toString())) }
            )
        }

        // Detail Sesi Terapi
        composable(
            DestinasiDetailSesi.routeWithArg,
            arguments = listOf(navArgument(DestinasiDetailSesi.idSesi) { type = NavType.IntType })
        ) { backStackEntry ->
            val idSesiTerapi = backStackEntry.arguments?.getInt(DestinasiDetailSesi.idSesi) ?: -1
            DetailSesiTerapiView(
                idSesi = idSesiTerapi,
                navigateBack = { navController.popBackStack() },
                onEditClick = { id -> navController.navigate(DestinasiUpdateSesiTerapi.routeWithArg.replace("{${DestinasiUpdateSesiTerapi.idsesi}}", id.toString())) }
            )
        }

        // Update Pasien
        composable(
            DestinasiUpdatePasien.routesWithArg,
            arguments = listOf(navArgument(DestinasiUpdatePasien.idpasien) { type = NavType.IntType })
        ) {
            UpdatePasienView(
                navigateBack = { navController.popBackStack() },
                modifier = Modifier,
                viewModel = viewModel(factory = PenyediaViewModel.Factory)
            )
        }

        // Update Terapis
        composable(
            DestinasiUpdateTerapis.routeWithArg,
            arguments = listOf(navArgument(DestinasiUpdateTerapis.ID_TERAPIS) { type = NavType.IntType })
        ) {
            UpdateTerapisView(
                navigateBack = { navController.popBackStack() },
                modifier = Modifier,
                viewModel = viewModel(factory = PenyediaViewModel.Factory)
            )
        }

        // Update Jenis Terapi
        composable(
            DestinasiUpdateJenisTerapi.routeWithArg,
            arguments = listOf(navArgument(DestinasiUpdateJenisTerapi.ID_JENIS_TERAPI) { type = NavType.IntType })
        ) {
            UpdateJenisTerapiView(
                navigateBack = { navController.popBackStack() },
                modifier = Modifier,
                viewModel = viewModel(factory = PenyediaViewModel.Factory)
            )
        }

        // Update Sesi Terapi
        composable(
            DestinasiUpdateSesiTerapi.routeWithArg,
            arguments = listOf(navArgument(DestinasiUpdateSesiTerapi.idsesi) { type = NavType.IntType })
        ) {
            UpdateSesiTerapiView(
                navigateBack = { navController.popBackStack() },
                modifier = Modifier,
                viewModel = viewModel(factory = PenyediaViewModel.Factory)
            )
        }
    }
}
