package com.example.docktooth.features.clinical_history

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.docktooth.core.component.DocToothBottomBar
import com.example.docktooth.core.component.DocToothTopBar
import com.example.docktooth.core.theme.PrimaryBlue
import com.example.docktooth.domain.model.ClinicalRecord
import com.example.docktooth.domain.model.UserRole
import com.example.docktooth.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClinicalHistoryScreen(
    onBackClick: () -> Unit,
    onNavigateToNewRecord: () -> Unit,
    onNavigateToDashboard: (() -> Unit)? = null,
    onNavigateToAgenda: (() -> Unit)? = null,
    onNavigateToBilling: (() -> Unit)? = null,
    viewModel: ClinicalHistoryViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()
    var selectedRecordDetail by remember { mutableStateOf<ClinicalRecord?>(null) }

    val isDoctor = state.currentUserRole == UserRole.ODONTOLOGO || state.currentUserRole == UserRole.ESPECIALISTA
    val isPatient = state.currentUserRole == UserRole.PACIENTE
    val isReceptionist = state.currentUserRole == UserRole.RECEPCIONISTA

    Scaffold(
        topBar = {
            DocToothTopBar(
                title = "Historia clínica digital",
                onBackClick = onBackClick,
                showLogo = true
            )
        },
        floatingActionButton = {
            // Solo el Odontólogo / Especialista puede crear nuevos registros clínicos
            if (isDoctor) {
                ExtendedFloatingActionButton(
                    onClick = onNavigateToNewRecord,
                    containerColor = PrimaryBlue,
                    contentColor = Color.White,
                    icon = { Icon(Icons.Default.Add, contentDescription = "Nuevo registro") },
                    text = { Text("Nuevo registro", fontWeight = FontWeight.Bold) },
                    shape = RoundedCornerShape(16.dp)
                )
            }
        },
        bottomBar = {
            DocToothBottomBar(
                currentRoute = Screen.ClinicalHistory.route,
                onNavigateToRoute = { route ->
                    when (route) {
                        Screen.Dashboard.route -> onNavigateToDashboard?.invoke() ?: onBackClick()
                        Screen.Agenda.route -> onNavigateToAgenda?.invoke()
                        Screen.Billing.route -> onNavigateToBilling?.invoke()
                    }
                },
                userRole = state.currentUserRole
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFFF5F7FA))
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            // Patient Card Banner
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFE2F1F8)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = PrimaryBlue,
                            modifier = Modifier.size(30.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column {
                        Text(
                            text = state.patientName,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryBlue
                        )
                        Text(
                            text = state.patientDoc,
                            fontSize = 13.sp,
                            color = Color.Gray
                        )
                        Text(
                            text = state.patientAgeGender,
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Informative Banner depending on Role
            if (isPatient) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE2F1F8)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            tint = PrimaryBlue
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "Modo lectura: Esta es tu historia clínica digital. La evolución, exámen y diagnóstico son gestionados por tu odontólogo.",
                            fontSize = 12.sp,
                            color = PrimaryBlue,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            } else if (isReceptionist) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF2F2)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = null,
                            tint = Color(0xFFDC2626)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "Acceso restringido: Solo el odontólogo profesional tiene permisos para registrar evoluciones clínicas.",
                            fontSize = 12.sp,
                            color = Color(0xFF991B1B)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Tabs Bar
            TabRow(
                selectedTabIndex = state.selectedTab,
                containerColor = Color.Transparent,
                contentColor = PrimaryBlue
            ) {
                Tab(
                    selected = state.selectedTab == 0,
                    onClick = { viewModel.onTabSelected(0) },
                    text = { Text("Evolución clínica", fontSize = 12.sp, fontWeight = FontWeight.Bold) }
                )
                Tab(
                    selected = state.selectedTab == 1,
                    onClick = { viewModel.onTabSelected(1) },
                    text = { Text("Odontograma", fontSize = 12.sp, fontWeight = FontWeight.Bold) }
                )
                Tab(
                    selected = state.selectedTab == 2,
                    onClick = { viewModel.onTabSelected(2) },
                    text = { Text("Anamnesis", fontSize = 12.sp, fontWeight = FontWeight.Bold) }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Header Section
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Registros clínicos",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E293B)
                )

                // Botón "Nuevo registro" visible únicamente para Odontólogo / Especialista
                if (isDoctor) {
                    TextButton(onClick = onNavigateToNewRecord) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Nuevo registro", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = PrimaryBlue)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Table Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFE2E8F0), RoundedCornerShape(8.dp))
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Fecha", fontWeight = FontWeight.Bold, fontSize = 12.sp, modifier = Modifier.weight(1f))
                Text("Tipo", fontWeight = FontWeight.Bold, fontSize = 12.sp, modifier = Modifier.weight(1f))
                Text("Profesional", fontWeight = FontWeight.Bold, fontSize = 12.sp, modifier = Modifier.weight(1.5f))
                Spacer(modifier = Modifier.width(20.dp))
            }

            Spacer(modifier = Modifier.height(8.dp))

            // List of Records
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                items(state.records) { record ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(10.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                if (isDoctor) {
                                    onNavigateToNewRecord()
                                } else {
                                    selectedRecordDetail = record
                                }
                            }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(record.date, fontSize = 13.sp, color = PrimaryBlue, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                            Text(record.type, fontSize = 13.sp, modifier = Modifier.weight(1f))
                            Text(record.professional, fontSize = 12.sp, color = Color.DarkGray, modifier = Modifier.weight(1.5f))
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                                contentDescription = null,
                                tint = Color.Gray,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                }
            }
        }
    }

    // Detail Dialog for Patient / Receptionist
    if (selectedRecordDetail != null) {
        val r = selectedRecordDetail!!
        AlertDialog(
            onDismissRequest = { selectedRecordDetail = null },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.MedicalServices, contentDescription = null, tint = PrimaryBlue)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Detalle del Registro Clínico", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                }
            },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                ) {
                    Surface(
                        color = PrimaryBlue.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("📅 Fecha: ${r.date}", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = PrimaryBlue)
                            Text("Tipo: ${r.type}", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = PrimaryBlue)
                        }
                    }

                    Text("Atendido por:", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color.Gray)
                    Text(r.professional, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = PrimaryBlue)

                    Text("Motivo de consulta:", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color.Gray)
                    Text(r.reason.ifBlank { "Sin especificar" }, fontSize = 13.sp, color = Color(0xFF1E293B))

                    Text("Examen odontológico:", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color.Gray)
                    Text(r.examination.ifBlank { "Sin observaciones" }, fontSize = 13.sp, color = Color(0xFF1E293B))

                    Text("Diagnóstico:", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color.Gray)
                    Text(r.diagnosis.ifBlank { "Sin diagnóstico registrado" }, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = PrimaryBlue)

                    Text("Procedimiento realizado:", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color.Gray)
                    Text(r.procedure.ifBlank { "Ninguno" }, fontSize = 13.sp, color = Color(0xFF1E293B))

                    Text("Observaciones:", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color.Gray)
                    Text(r.observations.ifBlank { "Sin observaciones adicionales" }, fontSize = 13.sp, color = Color(0xFF1E293B))

                    if (r.includePrescription) {
                        Surface(
                            color = Color(0xFFDCFCE7),
                            shape = RoundedCornerShape(6.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "💊 Incluye prescripción médica firmada",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF15803D),
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = { selectedRecordDetail = null },
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
                ) {
                    Text("Cerrar")
                }
            }
        )
    }

    // Odontogram Notice Dialog
    if (state.showOdontogramNotice) {
        AlertDialog(
            onDismissRequest = viewModel::dismissNotice,
            title = { Text("Módulo de Odontograma") },
            text = { Text("El mapa interactivo del Odontograma no está disponible en esta versión según los requerimientos indicados.") },
            confirmButton = {
                Button(onClick = viewModel::dismissNotice, colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)) {
                    Text("Entendido")
                }
            }
        )
    }
}
