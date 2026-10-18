package com.example.docktooth.features.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.docktooth.data.repository.AppointmentRepositoryImpl
import com.example.docktooth.data.repository.AuthRepositoryImpl
import com.example.docktooth.domain.model.UserRole
import com.example.docktooth.domain.repository.AppointmentRepository
import com.example.docktooth.domain.repository.AuthRepository
import kotlinx.coroutines.flow.*

class DashboardViewModel(
    private val authRepository: AuthRepository = AuthRepositoryImpl(),
    private val appointmentRepository: AppointmentRepository = AppointmentRepositoryImpl()
) : ViewModel() {

    val uiState: StateFlow<DashboardState> = combine(
        authRepository.currentUser,
        appointmentRepository.appointments
    ) { user, appointments ->
        val userAppointments = if (user.role == UserRole.PACIENTE) {
            val patientFirstName = user.fullName.split(" ").firstOrNull() ?: ""
            appointments.filter {
                it.patientName.contains(user.fullName, ignoreCase = true) ||
                it.patientName.contains(patientFirstName, ignoreCase = true)
            }.ifEmpty { appointments.take(1) }
        } else {
            appointments
        }

        DashboardState(
            currentUser = user,
            todayAppointmentsCount = userAppointments.size,
            totalPatientsCount = if (user.role == UserRole.PACIENTE) 1 else 124,
            estimatedIncomeToday = "$ 1.250.000",
            nextAppointmentTime = userAppointments.firstOrNull()?.time ?: "10:30 a.m.",
            nextAppointmentPatient = if (user.role == UserRole.PACIENTE) user.fullName else (userAppointments.firstOrNull()?.patientName ?: "Laura Torres"),
            upcomingAppointments = userAppointments.take(3)
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = DashboardState()
    )

    fun logout() {
        authRepository.logout()
    }
}
