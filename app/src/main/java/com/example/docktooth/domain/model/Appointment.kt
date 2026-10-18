package com.example.docktooth.domain.model

enum class AppointmentStatus(val label: String) {
    CONFIRMADA("Confirmada"),
    PENDIENTE("Pendiente"),
    CANCELADA("Cancelada")
}

data class Appointment(
    val id: String,
    val time: String,
    val patientName: String,
    val patientDoc: String = "",
    val specialty: String,
    val date: String,
    val status: AppointmentStatus,
    val doctorName: String = "Dr. Carlos Ramírez"
)
