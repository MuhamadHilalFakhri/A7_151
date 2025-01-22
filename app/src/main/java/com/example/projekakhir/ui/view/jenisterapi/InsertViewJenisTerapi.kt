package com.example.projekakhir.ui.view.jenisterapi

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.projekakhir.navigation.DestinasiNavigasi
import com.example.projekakhir.ui.viewmodel.sesiterapi.InsertJenisTerapiUiEvent
import com.example.projekakhir.ui.viewmodel.sesiterapi.InsertJenisTerapiUiState

object DestinasiEntryJenisTerapi : DestinasiNavigasi {
    override val route = "item_entry_jenis_terapi"
    override val titleRes = "Entry Jenis Terapi"
}



@Composable
fun EntryBodyJenisTerapi(
    insertUiState: InsertJenisTerapiUiState,
    onJenisTerapiValueChange: (InsertJenisTerapiUiEvent) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(18.dp),
        modifier = modifier.padding(12.dp)
    ) {
        FormInputJenisTerapi(
            insertUiEvent = insertUiState.insertUiEvent,
            onValueChange = onJenisTerapiValueChange,
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = onSaveClick,
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Simpan")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormInputJenisTerapi(
    insertUiEvent: InsertJenisTerapiUiEvent,
    modifier: Modifier = Modifier,
    onValueChange: (InsertJenisTerapiUiEvent) -> Unit = {},
    enabled: Boolean = true
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(
            value = insertUiEvent.namaJenisTerapi,
            onValueChange = { onValueChange(insertUiEvent.copy(namaJenisTerapi = it)) },
            label = { Text("Nama Jenis Terapi") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )

        OutlinedTextField(
            value = insertUiEvent.deskripsiTerapi,
            onValueChange = { onValueChange(insertUiEvent.copy(deskripsiTerapi = it)) },
            label = { Text("Deskripsi Terapi") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )

        if (enabled) {
            Text(
                text = "Isi Semua Data!",
                modifier = Modifier.padding(12.dp)
            )
        }

        Divider(
            thickness = 8.dp,
            modifier = Modifier.padding(12.dp)
        )
    }
}
