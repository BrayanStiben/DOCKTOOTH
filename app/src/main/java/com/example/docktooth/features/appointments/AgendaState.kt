package com.example.docktooth.features.appointments

import com.example.docktooth.domain.model.Appointment
import com.example.docktooth.domain.model.UserRole

data class AgendaState(
    val selectedDay: Int = 24,
    val selectedMonth: Int = 4,
    val selectedYear: Int = 2025,
    val searchQuery: String = "",
    val appointments: List<Appointment> = emptyList(),
    val showAddDialog: Boolean = false,
    val newPatientName: String = "",
    val newSpecialty: String = "Odontología general",
    val newTime: String = "09:00 a.m.",
    val newDateDay: Int = 24,
    val newDateMonth: Int = 4,
    val newDateYear: Int = 2025,
    val newDoctorName: String = "Dr. Juan Sebastián Gonzalez",
    val currentUserRole: UserRole = UserRole.ODONTOLOGO,
    val currentUserName: String = ""
)
