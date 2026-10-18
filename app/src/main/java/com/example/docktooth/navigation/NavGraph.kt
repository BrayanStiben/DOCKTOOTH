package com.example.docktooth.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.docktooth.features.appointments.AgendaScreen
import com.example.docktooth.features.billing.BillingScreen
import com.example.docktooth.features.clinical_history.ClinicalHistoryScreen
import com.example.docktooth.features.clinical_record.ClinicalRecordFormScreen
import com.example.docktooth.features.home.DashboardScreen
import com.example.docktooth.features.invoice_view.InvoiceViewScreen
import com.example.docktooth.features.login.LoginScreen
import com.example.docktooth.features.register.RegisterScreen
import com.example.docktooth.features.splash.SplashScreen
import com.example.docktooth.features.time_pass.TimePassScreen

@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        // 0. Pantalla de carga / Splash Screen
        composable(Screen.Splash.route) {
            SplashScreen(
                onLoadingComplete = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        // 1. Iniciar sesión (Pantalla Principal Inicial)
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route)
                }
            )
        }

        // 2. Crear cuenta
        composable(Screen.Register.route) {
            RegisterScreen(
                onBackClick = { navController.popBackStack() },
                onRegisterSuccess = {
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Register.route) { inclusive = true }
                    }
                }
            )
        }

        // 3. Panel principal / Dashboard
        composable(Screen.Dashboard.route) {
            DashboardScreen(
                onNavigateToAgenda = { navController.navigate(Screen.Agenda.route) },
                onNavigateToClinicalHistory = { navController.navigate(Screen.ClinicalHistory.route) },
                onNavigateToBilling = { navController.navigate(Screen.Billing.route) },
                onNavigateToTimePass = { navController.navigate(Screen.TimePass.route) },
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        // 4. Agenda y citas
        composable(Screen.Agenda.route) {
            AgendaScreen(
                onBackClick = { navController.popBackStack() },
                onNavigateToDashboard = { navController.navigate(Screen.Dashboard.route) { popUpTo(Screen.Dashboard.route) { inclusive = true } } },
                onNavigateToClinicalHistory = { navController.navigate(Screen.ClinicalHistory.route) },
                onNavigateToBilling = { navController.navigate(Screen.Billing.route) }
            )
        }

        // 5. Historia clínica digital
        composable(Screen.ClinicalHistory.route) {
            ClinicalHistoryScreen(
                onBackClick = { navController.popBackStack() },
                onNavigateToNewRecord = { navController.navigate(Screen.ClinicalRecordForm.route) },
                onNavigateToDashboard = { navController.navigate(Screen.Dashboard.route) { popUpTo(Screen.Dashboard.route) { inclusive = true } } },
                onNavigateToAgenda = { navController.navigate(Screen.Agenda.route) },
                onNavigateToBilling = { navController.navigate(Screen.Billing.route) }
            )
        }

        // 6. Odontograma -> Saltado según la instrucción ("ojo la numero 6 no la hagas")

        // 7. Registro clínico
        composable(Screen.ClinicalRecordForm.route) {
            ClinicalRecordFormScreen(
                onBackClick = { navController.popBackStack() },
                onRecordSaved = { navController.popBackStack() }
            )
        }

        // 8. Facturación
        composable(Screen.Billing.route) {
            BillingScreen(
                onBackClick = { navController.popBackStack() },
                onNavigateToInvoiceView = { navController.navigate(Screen.InvoiceView.route) },
                onNavigateToDashboard = { navController.navigate(Screen.Dashboard.route) { popUpTo(Screen.Dashboard.route) { inclusive = true } } },
                onNavigateToAgenda = { navController.navigate(Screen.Agenda.route) },
                onNavigateToClinicalHistory = { navController.navigate(Screen.ClinicalHistory.route) }
            )
        }

        // 9. Vista de factura
        composable(Screen.InvoiceView.route) {
            InvoiceViewScreen(
                onBackClick = { navController.popBackStack() }
            )
        }

        // 10. Pasar el tiempo (Juegos Dentales)
        composable(Screen.TimePass.route) {
            TimePassScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
