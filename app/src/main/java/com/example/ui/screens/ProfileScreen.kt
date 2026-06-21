package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.AppViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: AppViewModel,
    onNavigateToHistory: () -> Unit,
    onNavigateToDevices: () -> Unit
) {
    val user by viewModel.currentUserFlow.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Profile") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(Icons.Default.AccountCircle, contentDescription = null, modifier = Modifier.size(100.dp), tint = MaterialTheme.colorScheme.primary)
            Text(user?.fullName ?: "Name", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Text("@${user?.username ?: "username"}", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            
            Spacer(modifier = Modifier.height(16.dp))
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Account Details", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                    Text("Mobile: ${user?.mobile ?: "N/A"}")
                    Text("Address: ${user?.address ?: "N/A"}")
                    Text("Pin Code: ${user?.pinCode ?: "N/A"}")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Status: ${user?.status ?: "Unknown"}", fontWeight = FontWeight.Bold)
                    Text("Seat: ${user?.seatNo ?: "Unassigned"}", fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onNavigateToHistory, modifier = Modifier.fillMaxWidth()) { Text("Attendance History") }
            Button(onClick = onNavigateToDevices, modifier = Modifier.fillMaxWidth()) { Text("Active Devices") }
            OutlinedButton(onClick = { viewModel.logout() }, modifier = Modifier.fillMaxWidth()) { Text("Logout") }
        }
    }
}
