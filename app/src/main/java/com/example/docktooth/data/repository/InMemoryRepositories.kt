package com.example.docktooth.data.repository

import com.example.docktooth.data.local.LocalPersistence
import com.example.docktooth.domain.model.*
import com.example.docktooth.domain.repository.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object AppDataStore {
    private var isInitialized = false

    // Initial default seed
    private val defaultAppointments = listOf(
        Appointment("1", "08:00 a.m.", "Santiago Marín Serna", "1.098.765.404", "Odontología general", "24/04/2025", AppointmentStatus.CONFIRMADA, "Dr. Juan Sebastián Gonzalez"),
        Appointment("2", "09:30 a.m.", "María López", "1.002.345.678", "Limpieza dental", "24/04/2025", AppointmentStatus.CONFIRMADA, "Dr. Brayan Stiben Meza"),
        Appointment("3", "11:00 a.m.", "Ana García", "1.004.567.890", "Endodoncia", "25/04/2025", AppointmentStatus.PENDIENTE, "Dr. Juan Sebastián Gonzalez"),
        Appointment("4", "02:00 p.m.", "Santiago Marín Serna", "1.098.765.404", "Ortodoncia y valoración", "26/04/2025", AppointmentStatus.CONFIRMADA, "Dr. Brayan Stiben Meza"),
        Appointment("5", "04:30 p.m.", "Valentina Rojas", "1.006.789.012", "Revisión general", "27/04/2025", AppointmentStatus.PENDIENTE, "Dr. Juan Sebastián Gonzalez")
    )

    private val defaultRecords = listOf(
        ClinicalRecord("1", "24/04/2025", "Evolución", "Dr. Carlos Ramírez", "Dolor en molar inferior derecho", "Se observa caries profunda en 46, sin signos de absceso.", "Caries dental profunda - 46", "Restauración con resina", "Paciente refiere sensibilidad al frío y calor.", true),
        ClinicalRecord("2", "17/04/2025", "Diagnóstico", "Dra. Ana Gómez", "Chequeo de rutina", "Higiene oral regular, cálculo supragingival.", "Gingivitis marginal", "Profilaxis dental", "Programar revisión en 6 meses", false),
        ClinicalRecord("3", "10/04/2025", "Control", "Dr. Carlos Ramírez", "Post tratamiento de endodoncia", "Evolución favorable, sin sintomatología.", "Control asintomático", "Evaluación clínica", "Puntos retirados", false),
        ClinicalRecord("4", "03/04/2025", "Evolución", "Dra. Ana Gómez", "Valoración inicial", "Caries superficial en piezas 14 y 15.", "Caries de esmalte", "Sellantes de fotocurado", "Uso de hilo dental recomendado", false)
    )

    private val defaultSampleItems = listOf(
        InvoiceItem("1", "Consulta odontológica", 1, 80000.0),
        InvoiceItem("2", "Limpieza dental", 1, 120000.0),
        InvoiceItem("3", "Resina compuesta", 1, 250000.0)
    )

    private val defaultInvoice = Invoice(
        id = "INV-000124",
        invoiceNumber = "#F-000124",
        patientName = "Laura Torres",
        patientDoc = "CC 1.234.567.890",
        patientPhone = "Tel. 300 123 4567",
        issueDate = "24/04/2025",
        dueDate = "24/04/2025",
        items = defaultSampleItems,
        discountPercent = 0.0,
        vatPercent = 19.0
    )

    val usersList = MutableStateFlow(LocalPersistence.PRECREATED_USERS)
    val currentUser = MutableStateFlow(LocalPersistence.PRECREATED_USERS[0])
    val appointmentsList = MutableStateFlow(defaultAppointments)
    val clinicalRecordsList = MutableStateFlow(defaultRecords)
    val invoicesList = MutableStateFlow(listOf(defaultInvoice))
    val currentInvoice = MutableStateFlow<Invoice?>(defaultInvoice)

    fun initialize() {
        if (isInitialized) return
        isInitialized = true

        val loadedUsers = LocalPersistence.loadUsers()
        usersList.value = loadedUsers
        currentUser.value = LocalPersistence.loadCurrentUser()

        val loadedAppointments = LocalPersistence.loadAppointments(defaultAppointments)
        appointmentsList.value = loadedAppointments

        val loadedRecords = LocalPersistence.loadClinicalRecords(defaultRecords)
        clinicalRecordsList.value = loadedRecords

        val loadedInvoices = LocalPersistence.loadInvoices(listOf(defaultInvoice))
        invoicesList.value = loadedInvoices
        currentInvoice.value = loadedInvoices.firstOrNull()
    }
}

class AuthRepositoryImpl : AuthRepository {
    override val currentUser: StateFlow<User> = AppDataStore.currentUser.asStateFlow()

    override fun login(emailOrUser: String, password: String): Boolean {
        val users = AppDataStore.usersList.value
        val input = emailOrUser.trim()

        // Match user by email, full name or ID
        val matchedUser = users.find {
            it.email.equals(input, ignoreCase = true) || it.fullName.equals(input, ignoreCase = true)
        } ?: LocalPersistence.PRECREATED_USERS.find {
            it.email.equals(input, ignoreCase = true) || it.fullName.equals(input, ignoreCase = true)
        }

        val userToLogin = matchedUser ?: User(
            id = System.currentTimeMillis().toString(),
            fullName = if (input.contains("@")) input.substringBefore("@") else input,
            documentId = "1.234.567.890",
            email = if (input.isBlank()) "usuario@doctooth.com" else input,
            phone = "+57 300 123 4567",
            role = UserRole.PACIENTE,
            specialty = null
        )

        AppDataStore.currentUser.value = userToLogin
        LocalPersistence.saveCurrentUser(userToLogin)
        return true
    }

    override fun register(user: User): Boolean {
        val currentUsers = AppDataStore.usersList.value.toMutableList()
        currentUsers.add(0, user)
        AppDataStore.usersList.value = currentUsers
        AppDataStore.currentUser.value = user

        LocalPersistence.saveUsers(currentUsers)
        LocalPersistence.saveCurrentUser(user)
        return true
    }

    override fun logout() {
        val defaultUser = LocalPersistence.PRECREATED_USERS[0]
        AppDataStore.currentUser.value = defaultUser
        LocalPersistence.saveCurrentUser(defaultUser)
    }
}

class AppointmentRepositoryImpl : AppointmentRepository {
    override val appointments: StateFlow<List<Appointment>> = AppDataStore.appointmentsList.asStateFlow()

    override fun addAppointment(appointment: Appointment) {
        val current = AppDataStore.appointmentsList.value.toMutableList()
        current.add(0, appointment)
        AppDataStore.appointmentsList.value = current
        LocalPersistence.saveAppointments(current)
    }

    override fun updateAppointmentStatus(id: String, status: String) {
        val current = AppDataStore.appointmentsList.value.map {
            if (it.id == id) {
                val newStatus = when(status.lowercase()) {
                    "confirmada" -> AppointmentStatus.CONFIRMADA
                    else -> AppointmentStatus.PENDIENTE
                }
                it.copy(status = newStatus)
            } else it
        }
        AppDataStore.appointmentsList.value = current
        LocalPersistence.saveAppointments(current)
    }
}

class ClinicalRecordRepositoryImpl : ClinicalRecordRepository {
    override val records: StateFlow<List<ClinicalRecord>> = AppDataStore.clinicalRecordsList.asStateFlow()

    override fun addRecord(record: ClinicalRecord) {
        val current = AppDataStore.clinicalRecordsList.value.toMutableList()
        current.add(0, record)
        AppDataStore.clinicalRecordsList.value = current
        LocalPersistence.saveClinicalRecords(current)
    }
}

class InvoiceRepositoryImpl : InvoiceRepository {
    override val invoices: StateFlow<List<Invoice>> = AppDataStore.invoicesList.asStateFlow()
    override val currentInvoice: StateFlow<Invoice?> = AppDataStore.currentInvoice.asStateFlow()

    override fun saveInvoice(invoice: Invoice) {
        val current = AppDataStore.invoicesList.value.toMutableList()
        val index = current.indexOfFirst { it.id == invoice.id }
        if (index >= 0) {
            current[index] = invoice
        } else {
            current.add(0, invoice)
        }
        AppDataStore.invoicesList.value = current
        AppDataStore.currentInvoice.value = invoice
        LocalPersistence.saveInvoices(current)
    }

    override fun setCurrentInvoice(invoice: Invoice) {
        AppDataStore.currentInvoice.value = invoice
    }
}
