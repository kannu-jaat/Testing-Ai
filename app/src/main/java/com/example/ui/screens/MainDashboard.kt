package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.AppViewModel

@Composable
fun MainDashboard(
    viewModel: AppViewModel,
    onNavigateToHistory: () -> Unit,
    onNavigateToDevices: () -> Unit
) {
    var selectedItem by remember { mutableIntStateOf(0) }
    val items = listOf("Home", "Attendance", "Fees", "Notices", "Profile")
    val icons = listOf(Icons.Default.Home, Icons.Default.QrCodeScanner, Icons.Default.AccountBalanceWallet, Icons.Default.Description, Icons.Default.PersonOutline)

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                tonalElevation = 0.dp,
                modifier = Modifier.background(Color.White)
            ) {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = { Icon(icons[index], contentDescription = item) },
                        label = { Text(item, fontSize = 11.sp, fontWeight = if (selectedItem == index) FontWeight.Bold else FontWeight.Medium) },
                        selected = selectedItem == index,
                        onClick = { selectedItem = index },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            selectedTextColor = MaterialTheme.colorScheme.onBackground,
                            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            indicatorColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (selectedItem) {
                0 -> HomeScreen(
                    viewModel = viewModel,
                    onNavigateToAttendance = { selectedItem = 1 },
                    onNavigateToFees = { selectedItem = 2 },
                    onNavigateToNotices = { selectedItem = 3 }
                )
                1 -> AttendanceScreen(viewModel)
                2 -> FeesScreen(viewModel)
                3 -> NoticesScreen(viewModel)
                4 -> ProfileScreen(
                    viewModel = viewModel,
                    onNavigateToHistory = onNavigateToHistory,
                    onNavigateToDevices = onNavigateToDevices
                )
            }
        }
    }
}
