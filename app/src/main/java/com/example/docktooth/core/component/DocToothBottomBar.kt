package com.example.docktooth.core.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.docktooth.core.theme.PrimaryBlue
import com.example.docktooth.domain.model.UserRole
import com.example.docktooth.navigation.Screen

@Composable
fun DocToothBottomBar(
    currentRoute: String,
    onNavigateToRoute: (String) -> Unit,
    userRole: UserRole = UserRole.ODONTOLOGO
) {
    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 8.dp
    ) {
        NavigationBarItem(
            selected = currentRoute == Screen.Dashboard.route,
            onClick = { onNavigateToRoute(Screen.Dashboard.route) },
            icon = { Icon(Icons.Default.Home, contentDescription = "Inicio") },
            label = { Text("Inicio", fontSize = 11.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = PrimaryBlue,
                selectedTextColor = PrimaryBlue,
                indicatorColor = Color(0xFFE2F1F8)
            )
        )
        NavigationBarItem(
            selected = currentRoute == Screen.Agenda.route,
            onClick = { onNavigateToRoute(Screen.Agenda.route) },
            icon = { Icon(Icons.Default.CalendarToday, contentDescription = "Agenda") },
            label = { Text(if (userRole == UserRole.PACIENTE) "Mis citas" else "Agenda", fontSize = 11.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = PrimaryBlue,
                selectedTextColor = PrimaryBlue,
                indicatorColor = Color(0xFFE2F1F8)
            )
        )
        NavigationBarItem(
            selected = currentRoute == Screen.ClinicalHistory.route,
            onClick = { onNavigateToRoute(Screen.ClinicalHistory.route) },
            icon = { Icon(Icons.Default.Description, contentDescription = "Historias") },
            label = { Text(if (userRole == UserRole.PACIENTE) "Mi historial" else "Historias", fontSize = 11.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = PrimaryBlue,
                selectedTextColor = PrimaryBlue,
                indicatorColor = Color(0xFFE2F1F8)
            )
        )
        NavigationBarItem(
            selected = currentRoute == Screen.Billing.route,
            onClick = { onNavigateToRoute(Screen.Billing.route) },
            icon = { Icon(Icons.Default.Receipt, contentDescription = "Facturación") },
            label = { Text(if (userRole == UserRole.PACIENTE) "Mis facturas" else "Facturación", fontSize = 11.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = PrimaryBlue,
                selectedTextColor = PrimaryBlue,
                indicatorColor = Color(0xFFE2F1F8)
            )
        )
    }
}
