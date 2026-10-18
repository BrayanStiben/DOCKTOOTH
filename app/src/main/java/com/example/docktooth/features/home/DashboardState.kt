package com.example.docktooth.features.home

import com.example.docktooth.domain.model.Appointment
import com.example.docktooth.domain.model.User
import com.example.docktooth.domain.model.UserRole

data class DashboardState(
    val currentUser: User = User(
        id = "1",
        fullName = "Dr. Carlos Ramírez",
        documentId = "1.098.765.432",
        email = "carlos.ramirez@doctooth.com",
        phone = "+57 300 123 4567",
        role = UserRole.ODONTOLOGO,
        specialty = "Odontología general"
    ),
    val todayAppointmentsCount: Int = 5,
    val totalPatientsCount: Int = 124,
    val estimatedIncomeToday: String = "$ 1.250.000",
    val nextAppointmentTime: String = "10:30 a.m.",
    val nextAppointmentPatient: String = "Laura Torres",
    val upcomingAppointments: List<Appointment> = emptyList()
)
