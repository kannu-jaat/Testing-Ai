package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ui.AppViewModel
import com.example.ui.navigation.Splash
import com.example.ui.navigation.Welcome
import com.example.ui.navigation.Login
import com.example.ui.navigation.Register
import com.example.ui.navigation.PendingApproval
import com.example.ui.navigation.Home
import com.example.ui.navigation.Attendance
import com.example.ui.navigation.Fees
import com.example.ui.navigation.Notices
import com.example.ui.navigation.Profile
import com.example.ui.navigation.EditProfile
import com.example.ui.navigation.TermsAndConditions
import com.example.ui.navigation.ForgotPassword
import com.example.ui.navigation.AttendanceHistory
import com.example.ui.navigation.ActiveDevices
import com.example.ui.screens.SplashScreen
import com.example.ui.screens.WelcomeScreen
import com.example.ui.theme.MyApplicationTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
  private val viewModel: AppViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    // Add mock user data for testing purposes
    viewModel.addMockUser()

    setContent {
      MyApplicationTheme {
        val navController = rememberNavController()
        val loggedInUserId by viewModel.loggedInUserId.collectAsStateWithLifecycle()

        NavHost(
          navController = navController,
          startDestination = Splash
        ) {
            composable<Splash> {
               SplashScreen(
                   onTimeout = {
                       if (loggedInUserId != null) {
                           navController.navigate(Home) {
                               popUpTo(Splash) { inclusive = true }
                           }
                       } else {
                           navController.navigate(Welcome) {
                               popUpTo(Splash) { inclusive = true }
                           }
                       }
                   }
               )
            }
            composable<Welcome> {
                WelcomeScreen(
                    onLoginClick = { navController.navigate(Login) },
                    onRegisterClick = { navController.navigate(TermsAndConditions) }
                )
            }
            composable<TermsAndConditions> {
               com.example.ui.screens.TermsScreen(
                   onAccept = { navController.navigate(Register) },
                   onDecline = { navController.popBackStack() }
               )
            }
            composable<Login> {
               com.example.ui.screens.LoginScreen(
                   viewModel = viewModel,
                   onLoginSuccess = { status ->
                       if (status == "Approved") {
                           navController.navigate(Home) {
                               popUpTo(Welcome) { inclusive = true }
                           }
                       } else {
                           navController.navigate(PendingApproval) {
                               popUpTo(Welcome) { inclusive = true }
                           }
                       }
                   },
                   onRegisterClick = { navController.navigate(TermsAndConditions) },
                   onForgotPasswordClick = { navController.navigate(ForgotPassword) }
               )
            }
            composable<Register> {
                com.example.ui.screens.RegisterScreen(
                    viewModel = viewModel,
                    onRegisterSuccess = {
                        navController.navigate(PendingApproval) {
                            popUpTo(Welcome) { inclusive = true }
                        }
                    }
                )
            }
            composable<PendingApproval> {
                com.example.ui.screens.PendingApprovalScreen(
                    onRefresh = { /* check status */ },
                    onContactAdmin = { /* contact admin */ }
                )
            }
            composable<Home> {
                com.example.ui.screens.MainDashboard(
                    viewModel = viewModel,
                    onNavigateToHistory = { navController.navigate(AttendanceHistory) },
                    onNavigateToDevices = { navController.navigate(ActiveDevices) }
                )
            }
            composable<AttendanceHistory> {
                com.example.ui.screens.AttendanceHistoryScreen(
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() }
                )
            }
            composable<ActiveDevices> {
                com.example.ui.screens.DevicesScreen(
                    onBack = { navController.popBackStack() }
                )
            }
            composable<ForgotPassword> {
                com.example.ui.screens.ForgotPasswordScreen(
                   onBack = { navController.popBackStack() }
                )
            }
        }
      }
    }
  }
}
