package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.AppViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeesScreen(viewModel: AppViewModel) {
    val fees by viewModel.currentUserFeesFlow.collectAsStateWithLifecycle()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Fees") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
            ) {
                 Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                     Text("Current Due", style = MaterialTheme.typography.titleMedium)
                     Spacer(modifier = Modifier.height(8.dp))
                     Text("₹500", style = MaterialTheme.typography.displayMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.error)
                     Spacer(modifier = Modifier.height(8.dp))
                     Text("Due Date: 2024-06-30", style = MaterialTheme.typography.bodyMedium)
                 }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Box(
                 modifier = Modifier.fillMaxWidth(),
                 contentAlignment = Alignment.Center
            ) {
                 Column(horizontalAlignment = Alignment.CenterHorizontally) {
                     Icon(Icons.Default.QrCode, contentDescription = "UPI QR", modifier = Modifier.size(150.dp))
                     Text("Scan to Pay", fontWeight = FontWeight.Bold)
                     Text("admin@upi", style = MaterialTheme.typography.bodyMedium)
                 }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            Text("Payment History", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(fees) { fee ->
                    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                        Row(
                            modifier = Modifier.padding(16.dp).fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("Amount: ₹${fee.amount}", fontWeight = FontWeight.Bold)
                                Text("Due: ${fee.dueDate}", style = MaterialTheme.typography.bodySmall)
                            }
                            Text(fee.status, color = if (fee.status == "Paid") Color(0xFF4CAF50) else MaterialTheme.colorScheme.error, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
