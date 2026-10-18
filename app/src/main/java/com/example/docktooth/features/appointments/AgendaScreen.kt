package com.example.docktooth.features.appointments

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.docktooth.core.component.DocToothBottomBar
import com.example.docktooth.core.component.DocToothTopBar
import com.example.docktooth.core.theme.PrimaryBlue
import com.example.docktooth.core.theme.StatusGreenBg
import com.example.docktooth.core.theme.StatusGreenText
import com.example.docktooth.core.theme.StatusOrangeBg
import com.example.docktooth.core.theme.StatusOrangeText
import com.example.docktooth.domain.model.AppointmentStatus
import com.example.docktooth.navigation.Screen
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

private val monthsList = listOf(
    "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
    "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre",
)

private val yearsList = (2024..2030).toList()

private fun getDaysInMonth(month: Int, year: Int): Int {
    return try {
        YearMonth.of(year, month).lengthOfMonth()
    } catch (_: Exception) {
        30
    }
}

private fun getDayOfWeekShort(day: Int, month: Int, year: Int): String {
    return try {
        val date = LocalDate.of(year, month, day)
        val name = date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale("es", "ES"))
        name.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale("es", "ES")) else it.toString() }.take(3)
    } catch (_: Exception) {
        ""
    }
}

private fun getDayOfWeekFull(day: Int, month: Int, year: Int): String {
    return try {
        val date = LocalDate.of(year, month, day)
        val name = date.dayOfWeek.getDisplayName(TextStyle.FULL, Locale("es", "ES"))
        name.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale("es", "ES")) else it.toString() }
    } catch (_: Exception) {
        ""
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgendaScreen(
    onBackClick: () -> Unit,
    onNavigateToDashboard: (() -> Unit)? = null,
    onNavigateToClinicalHistory: (() -> Unit)? = null,
    onNavigateToBilling: (() -> Unit)? = null,
    viewModel: AgendaViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()

    var expandedHeaderMonth by remember { mutableStateOf(false) }
    var expandedHeaderYear by remember { mutableStateOf(false) }

    val daysInSelectedMonth = (1..getDaysInMonth(state.selectedMonth, state.selectedYear)).toList()

    Scaffold(
        topBar = {
            DocToothTopBar(
                title = "Agenda y Citas",
                onBackClick = onBackClick,
                showLogo = true
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { viewModel.onShowAddDialog(true) },
                containerColor = PrimaryBlue,
                contentColor = Color.White,
                icon = { Icon(Icons.Default.Add, contentDescription = "Agendar cita") },
                text = { Text("Agendar cita", fontWeight = FontWeight.Bold) },
                shape = RoundedCornerShape(16.dp)
            )
        },
        bottomBar = {
            DocToothBottomBar(
                currentRoute = Screen.Agenda.route,
                onNavigateToRoute = { route ->
                    when (route) {
                        Screen.Dashboard.route -> onNavigateToDashboard?.invoke() ?: onBackClick()
                        Screen.ClinicalHistory.route -> onNavigateToClinicalHistory?.invoke()
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

            // Interactive Month & Year Selector Header Navigation
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = viewModel::onPrevMonth,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Mes anterior",
                        tint = PrimaryBlue
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CalendarToday,
                        contentDescription = null,
                        tint = PrimaryBlue,
                        modifier = Modifier.size(20.dp)
                    )

                    // Month Selector Dropdown
                    Box {
                        Surface(
                            color = PrimaryBlue.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.clickable { expandedHeaderMonth = true }
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = monthsList.getOrElse(state.selectedMonth - 1) { "Abril" },
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = PrimaryBlue
                                )
                                Icon(
                                    imageVector = Icons.Default.ArrowDropDown,
                                    contentDescription = null,
                                    tint = PrimaryBlue
                                )
                            }
                        }
                        DropdownMenu(
                            expanded = expandedHeaderMonth,
                            onDismissRequest = { expandedHeaderMonth = false }
                        ) {
                            monthsList.forEachIndexed { idx, mName ->
                                DropdownMenuItem(
                                    text = { Text(mName, fontWeight = if (idx + 1 == state.selectedMonth) FontWeight.Bold else FontWeight.Normal) },
                                    onClick = {
                                        viewModel.onMonthSelected(idx + 1)
                                        expandedHeaderMonth = false
                                    }
                                )
                            }
                        }
                    }

                    // Year Selector Dropdown
                    Box {
                        Surface(
                            color = PrimaryBlue.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.clickable { expandedHeaderYear = true }
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = "${state.selectedYear}",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = PrimaryBlue
                                )
                                Icon(
                                    imageVector = Icons.Default.ArrowDropDown,
                                    contentDescription = null,
                                    tint = PrimaryBlue
                                )
                            }
                        }
                        DropdownMenu(
                            expanded = expandedHeaderYear,
                            onDismissRequest = { expandedHeaderYear = false }
                        ) {
                            yearsList.forEach { yNum ->
                                DropdownMenuItem(
                                    text = { Text("$yNum", fontWeight = if (yNum == state.selectedYear) FontWeight.Bold else FontWeight.Normal) },
                                    onClick = {
                                        viewModel.onYearSelected(yNum)
                                        expandedHeaderYear = false
                                    }
                                )
                            }
                        }
                    }
                }

                IconButton(
                    onClick = viewModel::onNextMonth,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Siguiente mes",
                        tint = PrimaryBlue
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Scrollable Day Selector Carousel for Selected Month & Year
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(daysInSelectedMonth) { dayNum ->
                    val isSelected = state.selectedDay == dayNum
                    val dayName = getDayOfWeekShort(dayNum, state.selectedMonth, state.selectedYear)

                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) PrimaryBlue else Color.White
                        ),
                        shape = RoundedCornerShape(14.dp),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = if (isSelected) 4.dp else 1.dp
                        ),
                        modifier = Modifier
                            .clickable { viewModel.onDateSelected(dayNum) }
                            .width(54.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(vertical = 10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = dayName,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                color = if (isSelected) Color.White.copy(alpha = 0.85f) else Color.Gray
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "$dayNum",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) Color.White else Color(0xFF1E293B)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Search Bar
            OutlinedTextField(
                value = state.searchQuery,
                onValueChange = viewModel::onSearchQueryChanged,
                placeholder = { Text("Buscar paciente, doctor o tratamiento...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color.Gray) },
                shape = RoundedCornerShape(24.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White,
                    unfocusedBorderColor = Color(0xFFCBD5E1)
                ),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Current Selected Day Header Visualizer
            val fullDayName = getDayOfWeekFull(state.selectedDay, state.selectedMonth, state.selectedYear)
            val monthName = monthsList.getOrElse(state.selectedMonth - 1) { "" }

            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE2F1F8)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Event,
                            contentDescription = null,
                            tint = PrimaryBlue,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "$fullDayName, ${state.selectedDay} de $monthName de ${state.selectedYear}",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryBlue
                        )
                    }

                    Text(
                        text = "${state.appointments.size} cita(s)",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryBlue
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Appointments List or Empty State
            if (state.appointments.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.EventAvailable,
                            contentDescription = null,
                            tint = Color.Gray,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "No hay citas programadas para el día ${state.selectedDay} de $monthName de ${state.selectedYear}",
                            fontSize = 14.sp,
                            color = Color.Gray,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { viewModel.onShowAddDialog(true) },
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Agendar cita para este día")
                        }
                    }
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    items(state.appointments) { appt ->
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            shape = RoundedCornerShape(14.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { viewModel.toggleAppointmentStatus(appt.id, appt.status) }
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.Schedule,
                                            contentDescription = null,
                                            tint = PrimaryBlue,
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = appt.time,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp,
                                            color = PrimaryBlue
                                        )
                                    }

                                    val isConfirmed = appt.status == AppointmentStatus.CONFIRMADA
                                    Surface(
                                        color = if (isConfirmed) StatusGreenBg else StatusOrangeBg,
                                        shape = RoundedCornerShape(12.dp)
                                    ) {
                                        Text(
                                            text = if (isConfirmed) "Confirmada" else "Pendiente",
                                            color = if (isConfirmed) StatusGreenText else StatusOrangeText,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = appt.patientName,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = Color(0xFF1E293B)
                                )

                                Spacer(modifier = Modifier.height(4.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "Tratamiento: ${appt.specialty}",
                                        fontSize = 12.sp,
                                        color = Color.Gray
                                    )
                                    Text(
                                        text = appt.doctorName,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = PrimaryBlue
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Add Appointment Dialog with Date Visualizer, Month/Year and Day Selector
    if (state.showAddDialog) {
        var expandedDoctor by remember { mutableStateOf(false) }
        val doctorsList = listOf("Dr. Juan Sebastián Gonzalez", "Dr. Brayan Stiben Meza")

        var expandedTime by remember { mutableStateOf(false) }
        val timesList = listOf("08:00 a.m.", "09:30 a.m.", "11:00 a.m.", "02:00 p.m.", "03:30 p.m.", "05:00 p.m.")

        var expandedDialogMonth by remember { mutableStateOf(false) }
        var expandedDialogYear by remember { mutableStateOf(false) }

        val newMonthName = monthsList.getOrElse(state.newDateMonth - 1) { "" }
        val newDayName = getDayOfWeekFull(state.newDateDay, state.newDateMonth, state.newDateYear)
        val daysInNewMonth = (1..getDaysInMonth(state.newDateMonth, state.newDateYear)).toList()

        AlertDialog(
            onDismissRequest = { viewModel.onShowAddDialog(false) },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Event, contentDescription = null, tint = PrimaryBlue)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Agendar Nueva Cita", fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                ) {
                    // Visualizer Banner for Selected Date in Dialog
                    Surface(
                        color = PrimaryBlue.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(
                                text = "📅 Cita para el día:",
                                fontSize = 11.sp,
                                color = Color.Gray
                            )
                            Text(
                                text = "$newDayName, ${state.newDateDay} de $newMonthName de ${state.newDateYear}",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = PrimaryBlue
                            )
                        }
                    }

                    // Selectores de Mes y Año en el Diálogo
                    Text(text = "Seleccionar Mes y Año:", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Dropdown Mes
                        ExposedDropdownMenuBox(
                            expanded = expandedDialogMonth,
                            onExpandedChange = { expandedDialogMonth = !expandedDialogMonth },
                            modifier = Modifier.weight(1.2f)
                        ) {
                            OutlinedTextField(
                                value = newMonthName,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Mes") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedDialogMonth) },
                                modifier = Modifier
                                    .menuAnchor()
                                    .fillMaxWidth(),
                                colors = OutlinedTextFieldDefaults.colors(
                                    unfocusedContainerColor = Color.White,
                                    focusedContainerColor = Color.White
                                )
                            )
                            ExposedDropdownMenu(
                                expanded = expandedDialogMonth,
                                onDismissRequest = { expandedDialogMonth = false }
                            ) {
                                monthsList.forEachIndexed { idx, mName ->
                                    DropdownMenuItem(
                                        text = { Text(mName) },
                                        onClick = {
                                            viewModel.onNewDateMonthChanged(idx + 1)
                                            expandedDialogMonth = false
                                        }
                                    )
                                }
                            }
                        }

                        // Dropdown Año
                        ExposedDropdownMenuBox(
                            expanded = expandedDialogYear,
                            onExpandedChange = { expandedDialogYear = !expandedDialogYear },
                            modifier = Modifier.weight(1f)
                        ) {
                            OutlinedTextField(
                                value = "${state.newDateYear}",
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Año") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedDialogYear) },
                                modifier = Modifier
                                    .menuAnchor()
                                    .fillMaxWidth(),
                                colors = OutlinedTextFieldDefaults.colors(
                                    unfocusedContainerColor = Color.White,
                                    focusedContainerColor = Color.White
                                )
                            )
                            ExposedDropdownMenu(
                                expanded = expandedDialogYear,
                                onDismissRequest = { expandedDialogYear = false }
                            ) {
                                yearsList.forEach { yNum ->
                                    DropdownMenuItem(
                                        text = { Text("$yNum") },
                                        onClick = {
                                            viewModel.onNewDateYearChanged(yNum)
                                            expandedDialogYear = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    // Selector de Día del Mes Seleccionado
                    Text(text = "Seleccionar día del mes ($newMonthName):", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(daysInNewMonth) { dayNum ->
                            val isSel = state.newDateDay == dayNum
                            Surface(
                                color = if (isSel) PrimaryBlue else Color(0xFFF1F5F9),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier
                                    .clickable { viewModel.onNewDateDayChanged(dayNum) }
                            ) {
                                Text(
                                    text = "$dayNum",
                                    color = if (isSel) Color.White else Color.Black,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }

                    // Nombre del paciente
                    OutlinedTextField(
                        value = state.newPatientName,
                        onValueChange = viewModel::onNewPatientChanged,
                        label = { Text("Nombre del paciente") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Odontólogo / Profesional Dropdown
                    ExposedDropdownMenuBox(
                        expanded = expandedDoctor,
                        onExpandedChange = { expandedDoctor = !expandedDoctor }
                    ) {
                        OutlinedTextField(
                            value = state.newDoctorName,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Atendido por") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedDoctor) },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expanded = expandedDoctor,
                            onDismissRequest = { expandedDoctor = false }
                        ) {
                            doctorsList.forEach { doc ->
                                DropdownMenuItem(
                                    text = { Text(doc) },
                                    onClick = {
                                        viewModel.onNewDoctorNameChanged(doc)
                                        expandedDoctor = false
                                    }
                                )
                            }
                        }
                    }

                    // Hora de la cita Dropdown
                    ExposedDropdownMenuBox(
                        expanded = expandedTime,
                        onExpandedChange = { expandedTime = !expandedTime }
                    ) {
                        OutlinedTextField(
                            value = state.newTime,
                            onValueChange = viewModel::onNewTimeChanged,
                            label = { Text("Hora de la cita") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedTime) },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expanded = expandedTime,
                            onDismissRequest = { expandedTime = false }
                        ) {
                            timesList.forEach { t ->
                                DropdownMenuItem(
                                    text = { Text(t) },
                                    onClick = {
                                        viewModel.onNewTimeChanged(t)
                                        expandedTime = false
                                    }
                                )
                            }
                        }
                    }

                    // Especialidad / Motivo
                    OutlinedTextField(
                        value = state.newSpecialty,
                        onValueChange = viewModel::onNewSpecialtyChanged,
                        label = { Text("Especialidad / Motivo") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = viewModel::createAppointment,
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
                ) {
                    Text("Confirmar y agendar")
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.onShowAddDialog(false) }) {
                    Text("Cancelar")
                }
            }
        )
    }
}
