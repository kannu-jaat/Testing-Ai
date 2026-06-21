package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ui.AppViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttendanceScreen(viewModel: AppViewModel) {
    var scanningState by remember { mutableStateOf("IDLE") } // IDLE, SCANNING, VERIFYING_WIFI, CAPTURING_SELFIE, SUCCESS, FAILED
    var statusMessage by remember { mutableStateOf("") }

    LaunchedEffect(scanningState) {
        when(scanningState) {
            "SCANNING" -> {
                delay(1500)
                scanningState = "VERIFYING_WIFI"
            }
            "VERIFYING_WIFI" -> {
                delay(1500)
                scanningState = "CAPTURING_SELFIE"
            }
            "CAPTURING_SELFIE" -> {
                delay(1500)
                viewModel.markAttendance { success, msg ->
                    if (success) {
                        scanningState = "SUCCESS"
                        statusMessage = msg
                    } else {
                        scanningState = "FAILED"
                        statusMessage = msg
                    }
                }
            }
        }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Mark Attendance") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            when (scanningState) {
                "IDLE" -> {
                    Icon(Icons.Default.CameraAlt, contentDescription = null, modifier = Modifier.size(100.dp), tint = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.height(24.dp))
                    Text("Ready to scan library QR", style = MaterialTheme.typography.titleLarge)
                    Spacer(modifier = Modifier.height(32.dp))
                    Button(onClick = { scanningState = "SCANNING" }, modifier = Modifier.fillMaxWidth().height(56.dp)) {
                        Text("Scan Attendance QR")
                    }
                }
                "SCANNING" -> {
                    CircularProgressIndicator(modifier = Modifier.size(80.dp))
                    Spacer(modifier = Modifier.height(24.dp))
                    Text("Scanning QR Code...", style = MaterialTheme.typography.titleLarge)
                }
                "VERIFYING_WIFI" -> {
                    Icon(Icons.Default.Wifi, contentDescription = null, modifier = Modifier.size(80.dp), tint = MaterialTheme.colorScheme.secondary)
                    Spacer(modifier = Modifier.height(24.dp))
                    Text("Verifying Library WiFi...", style = MaterialTheme.typography.titleLarge)
                    Text("Connected to: Krishna_Lib_5G", style = MaterialTheme.typography.bodyMedium)
                }
                "CAPTURING_SELFIE" -> {
                    Box(modifier = Modifier.size(200.dp).background(Color.Gray)) {
                        Text("Camera Hidden Preview", modifier = Modifier.align(Alignment.Center), color = Color.White)
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                    Text("Capturing verification photo...", style = MaterialTheme.typography.titleLarge)
                }
                "SUCCESS" -> {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(100.dp), tint = Color(0xFF4CAF50))
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(statusMessage, style = MaterialTheme.typography.titleLarge, color = Color(0xFF4CAF50))
                    Spacer(modifier = Modifier.height(32.dp))
                    Button(onClick = { scanningState = "IDLE" }) { Text("Go Back") }
                }
                "FAILED" -> {
                    Text(statusMessage, style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.error)
                    Spacer(modifier = Modifier.height(32.dp))
                    Button(onClick = { scanningState = "IDLE" }) { Text("Try Again") }
                }
            }
        }
    }
}
