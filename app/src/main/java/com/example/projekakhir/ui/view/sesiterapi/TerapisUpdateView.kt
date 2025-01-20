package com.example.projekakhir.ui.view.sesiterapi

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projekakhir.navigation.DestinasiNavigasi
import com.example.projekakhir.ui.CostumeTopAppBar
import com.example.projekakhir.ui.viewmodel.PenyediaViewModel
import com.example.projekakhir.ui.viewmodel.terapis.UpdateTerapisViewModel
import com.example.projekakhir.ui.viewmodel.terapis.toTerapis
import kotlinx.coroutines.launch

object DestinasiUpdateTerapis : DestinasiNavigasi {
    override val route = "update_terapis"
    const val ID_TERAPIS = "id_terapis"
    val routeWithArg = "$route/{$ID_TERAPIS}"
    override val titleRes = "Update Terapis"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateTerapisView(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: UpdateTerapisViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    val uiState = viewModel.uiState.value

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CostumeTopAppBar(
                title = DestinasiUpdateTerapis.titleRes,
                canNavigateBack = true,
                scrollBehavior = scrollBehavior,
                navigateUp = navigateBack
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            // Terapis entry form body
            EntryBodyTerapis(
                insertUiState = uiState,
                onTerapisValueChange = { updateValue ->
                    viewModel.updateTerapisState(updateValue)
                },
                onSaveClick = {
                    uiState.insertUiEvent?.let { insertUiEvent ->
                        coroutineScope.launch {
                            viewModel.updateTerapis(
                                idTerapis = viewModel.idTerapis,
                                terapis = insertUiEvent.toTerapis()
                            )
                            navigateBack() // Navigate back after saving
                        }
                    }
                }
            )
        }
    }
}
