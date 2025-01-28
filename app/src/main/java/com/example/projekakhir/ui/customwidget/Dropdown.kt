package com.example.projekakhir.ui.customwidget

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.zIndex



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Dropdown(
    label: String,
    items: List<String>,
    currentItem: String,
    onItemSelected: (String) -> Unit,
    enabled: Boolean = true
) {
    var expanded by remember { mutableStateOf(false) } // State untuk mengatur apakah dropdown terbuka
    var currentText by remember { mutableStateOf(currentItem) } // State untuk teks terpilih


    // Memastikan selectedText diperbarui dengan benar
    if (currentText != currentItem) {
        currentText = currentItem
    }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded } // Mengatur status expand
    ) {
        OutlinedTextField(
            value = currentText,
            onValueChange = {}, // Tidak perlu perubahan pada input ini
            label = { Text(label) },
            modifier = Modifier
                .menuAnchor() // Anchor untuk dropdown menu agar posisinya benar
                .fillMaxWidth(),
            enabled = enabled,
            readOnly = true, // Agar text tidak dapat diedit langsung
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            singleLine = true
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }, // Menutup dropdown ketika klik di luar
            modifier = Modifier
                .zIndex(1f) // Memberi prioritas z-index lebih tinggi agar dropdown muncul di atas elemen lain
                .fillMaxWidth() // Pastikan dropdown cukup lebar
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    text = { Text(text = item) },
                    onClick = {
                        currentText = item // Memperbarui teks yang dipilih
                        onItemSelected(item) // Callback ke komponen pemanggil
                        expanded = false // Menutup dropdown setelah item dipilih
                    }
                )
            }
        }
    }
}
