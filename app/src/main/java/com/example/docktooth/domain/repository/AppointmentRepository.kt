package com.example.docktooth.domain.repository

import com.example.docktooth.domain.model.Appointment
import kotlinx.coroutines.flow.StateFlow

interface AppointmentRepository {
    val appointments: StateFlow<List<Appointment>>
    fun addAppointment(appointment: Appointment)
    fun updateAppointmentStatus(id: String, status: String)
}
