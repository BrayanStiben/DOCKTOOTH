package com.example.docktooth.features.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.docktooth.core.component.DocToothBottomBar
import com.example.docktooth.core.component.DocToothTopBar
import com.example.docktooth.core.component.RoleBadge
import com.example.docktooth.core.theme.PrimaryBlue
import com.example.docktooth.core.theme.StatusGreenBg
import com.example.docktooth.core.theme.StatusGreenText
import com.example.docktooth.core.theme.StatusOrangeBg
import com.example.docktooth.core.theme.StatusOrangeText
import com.example.docktooth.domain.model.AppointmentStatus
import com.example.docktooth.domain.model.UserRole
import com.example.docktooth.navigation.Screen

@Composable
fun DashboardScreen(
    onNavigateToAgenda: () -> Unit,
    onNavigateToClinicalHistory: () -> Unit,
    onNavigateToBilling: () -> Unit,
    onNavigateToTimePass: () -> Unit = {},
    onLogout: (() -> Unit)? = null,
    viewModel: DashboardViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val user = state.currentUser

    Scaffold(
        topBar = {
            DocToothTopBar(
                title = "Doc-Tooth",
                showLogo = true,
                showNotification = true,
                actions = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        RoleBadge(currentRole = user.role)

                        if (onLogout != null) {
                            IconButton(onClick = {
                                viewModel.logout()
                                onLogout()
                            }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                                    contentDescription = "Cerrar sesión",
                                    tint = Color.White
                                )
                            }
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            // "Pasar el tiempo" solo visible para el perfil de PACIENTE
            if (user.role == UserRole.PACIENTE) {
                ExtendedFloatingActionButton(
                    onClick = onNavigateToTimePass,
                    containerColor = PrimaryBlue,
                    contentColor = Color.White,
                    icon = { Icon(Icons.Default.Schedule, contentDescription = "Pasar el tiempo") },
                    text = { Text("Pasar el tiempo", fontWeight = FontWeight.Bold) },
                    shape = RoundedCornerShape(16.dp)
                )
            }
        },
        bottomBar = {
            DocToothBottomBar(
                currentRoute = Screen.Dashboard.route,
                onNavigateToRoute = { route ->
                    when (route) {
                        Screen.Agenda.route -> onNavigateToAgenda()
                        Screen.ClinicalHistory.route -> onNavigateToClinicalHistory()
                        Screen.Billing.route -> onNavigateToBilling()
                    }
                },
                userRole = user.role
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFFF5F7FA))
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Welcome Card
            Card(
                colors = CardDefaults.cardColors(containerColor = PrimaryBlue),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Hola, ${user.fullName}",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = when (user.role) {
                            UserRole.ODONTOLOGO -> "Odontología General y Diagnóstico"
                            UserRole.ESPECIALISTA -> "Ortodoncia, Especialidades y Cirugía"
                            UserRole.RECEPCIONISTA -> "Gestión de citas y atención al usuario"
                            UserRole.PACIENTE -> "Portal de atención y seguimiento dental"
                        },
                        color = Color.White.copy(alpha = 0.85f),
                        fontSize = 13.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Role Specific Metric Cards
            if (user.role == UserRole.PACIENTE) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    MetricCard(
                        title = "Mis citas agendadas",
                        value = "${state.todayAppointmentsCount}",
                        actionText = "Ver mis citas ->",
                        icon = Icons.Default.CalendarToday,
                        onClick = onNavigateToAgenda,
                        modifier = Modifier.weight(1f)
                    )

                    MetricCard(
                        title = "Próxima cita",
                        value = state.nextAppointmentTime,
                        subtitle = "Con odontólogo",
                        icon = Icons.Default.Schedule,
                        onClick = onNavigateToAgenda,
                        modifier = Modifier.weight(1f)
                    )
                }
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    MetricCard(
                        title = "Citas hoy",
                        value = "${state.todayAppointmentsCount}",
                        actionText = "Ver agenda ->",
                        icon = Icons.Default.CalendarToday,
                        onClick = onNavigateToAgenda,
                        modifier = Modifier.weight(1f)
                    )

                    MetricCard(
                        title = "Pacientes",
                        value = "${state.totalPatientsCount}",
                        subtitle = "Total registrados",
                        icon = Icons.Default.People,
                        onClick = onNavigateToClinicalHistory,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    MetricCard(
                        title = "Ingresos estimados",
                        value = state.estimatedIncomeToday,
                        subtitle = "Hoy",
                        icon = Icons.Default.AttachMoney,
                        onClick = onNavigateToBilling,
                        modifier = Modifier.weight(1f)
                    )

                    MetricCard(
                        title = "Próxima cita",
                        value = state.nextAppointmentTime,
                        subtitle = state.nextAppointmentPatient,
                        icon = Icons.Default.Schedule,
                        onClick = onNavigateToAgenda,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Próximas Citas Section
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (user.role == UserRole.PACIENTE) "Mis próximas citas" else "Próximas citas",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E293B)
                )
                Text(
                    text = "Ver todas ->",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryBlue,
                    modifier = Modifier.clickable { onNavigateToAgenda() }
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    state.upcomingAppointments.forEachIndexed { index, appt ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = appt.time,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = PrimaryBlue
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = appt.patientName,
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 14.sp
                                    )
                                    Text(
                                        text = appt.specialty,
                                        fontSize = 12.sp,
                                        color = Color.Gray
                                    )
                                }
                            }

                            // Status Badge
                            val isConfirmed = appt.status == AppointmentStatus.CONFIRMADA
                            Surface(
                                color = if (isConfirmed) StatusGreenBg else StatusOrangeBg,
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(
                                    text = if (isConfirmed) "Confirmado" else "Pendiente",
                                    color = if (isConfirmed) StatusGreenText else StatusOrangeText,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }

                        if (index < state.upcomingAppointments.size - 1) {
                            HorizontalDivider(color = Color(0xFFF1F5F9))
                        }
                    }
                }
            }

            // Quick Actions Section
            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Acciones rápidas",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E293B)
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                when (user.role) {
                    UserRole.PACIENTE -> {
                        QuickActionButton(
                            icon = Icons.Default.CalendarToday,
                            label = "Mis citas",
                            onClick = onNavigateToAgenda,
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        QuickActionButton(
                            icon = Icons.Default.Description,
                            label = "Mi historial",
                            onClick = onNavigateToClinicalHistory,
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        QuickActionButton(
                            icon = Icons.Default.Receipt,
                            label = "Mis facturas",
                            onClick = onNavigateToBilling,
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        QuickActionButton(
                            icon = Icons.Default.Schedule,
                            label = "Pasar tiempo",
                            onClick = onNavigateToTimePass,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    UserRole.ODONTOLOGO, UserRole.ESPECIALISTA -> {
                        QuickActionButton(
                            icon = Icons.Default.AddCircle,
                            label = "Nueva cita",
                            onClick = onNavigateToAgenda,
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        QuickActionButton(
                            icon = Icons.Default.Person,
                            label = "Pacientes",
                            onClick = onNavigateToClinicalHistory,
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        QuickActionButton(
                            icon = Icons.Default.Description,
                            label = "Historial",
                            onClick = onNavigateToClinicalHistory,
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        QuickActionButton(
                            icon = Icons.Default.Receipt,
                            label = "Facturación",
                            onClick = onNavigateToBilling,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    UserRole.RECEPCIONISTA -> {
                        QuickActionButton(
                            icon = Icons.Default.AddCircle,
                            label = "Agendar cita",
                            onClick = onNavigateToAgenda,
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        QuickActionButton(
                            icon = Icons.Default.Person,
                            label = "Pacientes",
                            onClick = onNavigateToClinicalHistory,
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        QuickActionButton(
                            icon = Icons.Default.Receipt,
                            label = "Facturación",
                            onClick = onNavigateToBilling,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MetricCard(
    title: String,
    value: String,
    subtitle: String? = null,
    actionText: String? = null,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier.clickable { onClick() }
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = title, fontSize = 12.sp, color = Color.Gray, fontWeight = FontWeight.Medium)
                Icon(imageVector = icon, contentDescription = null, tint = PrimaryBlue, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = value, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
            if (subtitle != null) {
                Text(text = subtitle, fontSize = 11.sp, color = Color.Gray)
            }
            if (actionText != null) {
                Text(text = actionText, fontSize = 11.sp, color = PrimaryBlue, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun QuickActionButton(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE2F1F8)),
        shape = RoundedCornerShape(12.dp),
        modifier = modifier.clickable { onClick() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = PrimaryBlue, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = label,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = PrimaryBlue
            )
        }
    }
}
