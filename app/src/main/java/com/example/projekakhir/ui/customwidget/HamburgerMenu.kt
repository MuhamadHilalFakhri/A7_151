package com.example.projekakhir.ui.customwidget

import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.mutableStateOf
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HamburgerMenu(
    title: String,
    canNavigateBack: Boolean,
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    navigateUp: () -> Unit = {},
    onRefresh: () -> Unit = {},
    showRefreshIcon: Boolean = true,
    navController: NavController,
    currentPage: String // Parameter tambahan untuk menentukan halaman yang aktif
) {
    // State for DropdownMenu visibility
    val expanded = remember { mutableStateOf(false) }

    // Logic for menu items based on current page
    val menuItems = when (currentPage) {
        "home sesi terapi" -> {
            // When in Home Sesi Terapis, show Pasien, Terapis, Jenis Terapi
            listOf("Pasien", "Terapis", "Jenis Terapi")
        }
        "home pasien" -> {
            // When in Home Pasien, show Terapis, Jenis Terapi, Sesi Terapi
            listOf("Terapis", "Jenis Terapi", "Sesi Terapi")
        }
        "homejenis" -> {
            // When in Home Pasien, show Terapis, Jenis Terapi, Sesi Terapi
            listOf("Pasien","Terapis", "Sesi Terapi")
        }
        "hometrps" -> {
            // When in Home Pasien, show Terapis, Jenis Terapi, Sesi Terapi
            listOf("Pasien","Jenis Terapi", "Sesi Terapi")
        }
        else -> {
            // Default menu items for other pages
            listOf("Terapis", "Pasien", "Jenis Terapi")
        }
    }

    CenterAlignedTopAppBar(
        title = { Text(title, color = Color.White) },
        actions = {
            // Refresh Icon
            if (showRefreshIcon) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Refresh",
                    modifier = Modifier
                        .padding(end = 8.dp) // Optional padding
                        .clickable { onRefresh() },
                    tint = Color.White
                )
            }

            // Hamburger Menu Icon
            IconButton(onClick = { expanded.value = true }) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Hamburger Menu",
                    tint = Color.White
                )
            }

            // Dropdown Menu for Hamburger
            DropdownMenu(
                expanded = expanded.value,
                onDismissRequest = { expanded.value = false }
            ) {
                // Loop through the menu items based on the current page
                menuItems.forEach { menuItem ->
                    DropdownMenuItem(
                        onClick = {
                            // Navigate to the corresponding route based on menu item
                            when (menuItem) {
                                "Pasien" -> navController.navigate("home pasien")
                                "Terapis" -> navController.navigate("hometrps")
                                "Jenis Terapi" -> navController.navigate("homejenis")
                                "Sesi Terapi" -> navController.navigate("home sesi terapi")
                            }
                            expanded.value = false // Close menu after item click
                        },
                        text = { Text(menuItem) }
                    )
                }
            }
        },
        modifier = modifier
            .windowInsetsPadding(
                WindowInsets.systemBars.only(WindowInsetsSides.Top)
            ),
        scrollBehavior = scrollBehavior,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color(0xFF003f5c)
        )
    )
}
