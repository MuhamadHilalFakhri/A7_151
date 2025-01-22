package com.example.projekakhir.ui.view.jenisterapi

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
import com.example.projekakhir.ui.custom.CostumeTopAppBar
import com.example.projekakhir.ui.viewmodel.PenyediaViewModel
import com.example.projekakhir.ui.viewmodel.jenisterapi.UpdateJenisTerapiViewModel
import com.example.projekakhir.ui.viewmodel.jenisterapi.toJenisTerapi
import kotlinx.coroutines.launch

object DestinasiUpdateJenisTerapi : DestinasiNavigasi {
    override val route = "update_jenis_terapi"
    const val ID_JENIS_TERAPI = "id_jenis_terapi"
    val routeWithArg = "$route/{$ID_JENIS_TERAPI}"
    override val titleRes = "Update Jenis Terapi"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateJenisTerapiView(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: UpdateJenisTerapiViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    val uiState = viewModel.uiState.value

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CostumeTopAppBar(
                title = DestinasiUpdateJenisTerapi.titleRes,
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
            // Jenis Terapi entry form body
            EntryBodyJenisTerapi(
                insertUiState = uiState,
                onJenisTerapiValueChange = { updateValue ->
                    viewModel.updateJenisTerapiState(updateValue)
                },
                onSaveClick = {
                    uiState.insertUiEvent?.let { insertUiEvent ->
                        coroutineScope.launch {
                            viewModel.updateJenisTerapi(
                                idJenisTerapi = viewModel.idJenisTerapi,
                                jenisTerapi = insertUiEvent.toJenisTerapi()
                            )
                            navigateBack() // Navigate back after saving
                        }
                    }
                }
            )
        }
    }
}
