package com.example.docktooth.data.local

import android.content.Context
import android.content.SharedPreferences
import com.example.docktooth.domain.model.*
import org.json.JSONArray
import org.json.JSONObject

object LocalPersistence {
    private const val PREF_NAME = "docktooth_prefs"
    private lateinit var prefs: SharedPreferences

    // Default Pre-created 4 Hardcoded Profiles
    val PRECREATED_USERS = listOf(
        User(
            id = "1",
            fullName = "Juan Sebastián Gonzalez Mancera",
            documentId = "1.098.765.401",
            email = "juans.gonzalezm@uqvirtual.edu.co",
            phone = "+57 300 111 2233",
            role = UserRole.ODONTOLOGO,
            specialty = "Odontología General"
        ),
        User(
            id = "2",
            fullName = "Brayan Stiben Meza Campiño",
            documentId = "1.098.765.402",
            email = "brayans.mezac@uqvirtual.edu.co",
            phone = "+57 300 222 3344",
            role = UserRole.ESPECIALISTA,
            specialty = "Ortodoncia y Cirugía"
        ),
        User(
            id = "3",
            fullName = "Daniel Esteban Cifuentes Blandón",
            documentId = "1.098.765.403",
            email = "daniele.cifuentesb@uqvirtual.edu.co",
            phone = "+57 300 333 4455",
            role = UserRole.RECEPCIONISTA,
            specialty = null
        ),
        User(
            id = "4",
            fullName = "Santiago Marín Serna",
            documentId = "1.098.765.404",
            email = "santiago.marins@uqvirtual.edu.co",
            phone = "+57 300 444 5566",
            role = UserRole.PACIENTE,
            specialty = null
        )
    )

    fun init(context: Context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    // Save Users
    fun saveUsers(users: List<User>) {
        val array = JSONArray()
        users.forEach { u ->
            val obj = JSONObject().apply {
                put("id", u.id)
                put("fullName", u.fullName)
                put("documentId", u.documentId)
                put("email", u.email)
                put("phone", u.phone)
                put("role", u.role.name)
                put("specialty", u.specialty ?: "")
            }
            array.put(obj)
        }
        prefs.edit().putString("users", array.toString()).apply()
    }

    fun loadUsers(): List<User> {
        val jsonStr = prefs.getString("users", null)
        val list = mutableListOf<User>()
        if (jsonStr != null) {
            try {
                val array = JSONArray(jsonStr)
                for (i in 0 until array.length()) {
                    val obj = array.getJSONObject(i)
                    list.add(
                        User(
                            id = obj.getString("id"),
                            fullName = obj.getString("fullName"),
                            documentId = obj.getString("documentId"),
                            email = obj.getString("email"),
                            phone = obj.getString("phone"),
                            role = UserRole.valueOf(obj.getString("role")),
                            specialty = obj.optString("specialty").takeIf { it.isNotBlank() }
                        )
                    )
                }
            } catch (e: Exception) {
                // If parsing fails, fall back to precreated users
            }
        }
        val existingIds = list.map { it.id }.toSet()
        PRECREATED_USERS.forEach { preUser ->
            if (!existingIds.contains(preUser.id)) {
                list.add(preUser)
            }
        }
        return list
    }

    // Current User
    fun saveCurrentUser(user: User) {
        val obj = JSONObject().apply {
            put("id", user.id)
            put("fullName", user.fullName)
            put("documentId", user.documentId)
            put("email", user.email)
            put("phone", user.phone)
            put("role", user.role.name)
            put("specialty", user.specialty ?: "")
        }
        prefs.edit().putString("current_user", obj.toString()).apply()
    }

    fun loadCurrentUser(): User {
        val jsonStr = prefs.getString("current_user", null) ?: return PRECREATED_USERS[0]
        return try {
            val obj = JSONObject(jsonStr)
            User(
                id = obj.getString("id"),
                fullName = obj.getString("fullName"),
                documentId = obj.getString("documentId"),
                email = obj.getString("email"),
                phone = obj.getString("phone"),
                role = UserRole.valueOf(obj.getString("role")),
                specialty = obj.optString("specialty").takeIf { it.isNotBlank() }
            )
        } catch (e: Exception) {
            PRECREATED_USERS[0]
        }
    }

    // Save Appointments
    fun saveAppointments(appointments: List<Appointment>) {
        val array = JSONArray()
        appointments.forEach { a ->
            val obj = JSONObject().apply {
                put("id", a.id)
                put("time", a.time)
                put("patientName", a.patientName)
                put("patientDoc", a.patientDoc)
                put("specialty", a.specialty)
                put("date", a.date)
                put("status", a.status.name)
                put("doctorName", a.doctorName)
            }
            array.put(obj)
        }
        prefs.edit().putString("appointments", array.toString()).apply()
    }

    fun loadAppointments(defaultList: List<Appointment>): List<Appointment> {
        val jsonStr = prefs.getString("appointments", null) ?: return defaultList
        val list = mutableListOf<Appointment>()
        try {
            val array = JSONArray(jsonStr)
            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                list.add(
                    Appointment(
                        id = obj.getString("id"),
                        time = obj.getString("time"),
                        patientName = obj.getString("patientName"),
                        patientDoc = obj.optString("patientDoc", ""),
                        specialty = obj.getString("specialty"),
                        date = obj.getString("date"),
                        status = AppointmentStatus.valueOf(obj.getString("status")),
                        doctorName = obj.optString("doctorName", "Dr. Carlos Ramírez")
                    )
                )
            }
        } catch (e: Exception) {
            return defaultList
        }
        return list
    }

    // Save Clinical Records
    fun saveClinicalRecords(records: List<ClinicalRecord>) {
        val array = JSONArray()
        records.forEach { r ->
            val obj = JSONObject().apply {
                put("id", r.id)
                put("date", r.date)
                put("type", r.type)
                put("professional", r.professional)
                put("reason", r.reason)
                put("examination", r.examination)
                put("diagnosis", r.diagnosis)
                put("procedure", r.procedure)
                put("observations", r.observations)
                put("includePrescription", r.includePrescription)
            }
            array.put(obj)
        }
        prefs.edit().putString("clinical_records", array.toString()).apply()
    }

    fun loadClinicalRecords(defaultList: List<ClinicalRecord>): List<ClinicalRecord> {
        val jsonStr = prefs.getString("clinical_records", null) ?: return defaultList
        val list = mutableListOf<ClinicalRecord>()
        try {
            val array = JSONArray(jsonStr)
            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                list.add(
                    ClinicalRecord(
                        id = obj.getString("id"),
                        date = obj.getString("date"),
                        type = obj.getString("type"),
                        professional = obj.getString("professional"),
                        reason = obj.optString("reason", ""),
                        examination = obj.optString("examination", ""),
                        diagnosis = obj.optString("diagnosis", ""),
                        procedure = obj.optString("procedure", ""),
                        observations = obj.optString("observations", ""),
                        includePrescription = obj.optBoolean("includePrescription", false)
                    )
                )
            }
        } catch (e: Exception) {
            return defaultList
        }
        return list
    }

    // Save Invoices
    fun saveInvoices(invoices: List<Invoice>) {
        val array = JSONArray()
        invoices.forEach { inv ->
            val invObj = JSONObject().apply {
                put("id", inv.id)
                put("invoiceNumber", inv.invoiceNumber)
                put("patientName", inv.patientName)
                put("patientDoc", inv.patientDoc)
                put("patientPhone", inv.patientPhone)
                put("issueDate", inv.issueDate)
                put("dueDate", inv.dueDate)
                put("discountPercent", inv.discountPercent)
                put("vatPercent", inv.vatPercent)

                val itemsArray = JSONArray()
                inv.items.forEach { item ->
                    val itemObj = JSONObject().apply {
                        put("id", item.id)
                        put("description", item.description)
                        put("quantity", item.quantity)
                        put("unitPrice", item.unitPrice)
                    }
                    itemsArray.put(itemObj)
                }
                put("items", itemsArray)
            }
            array.put(invObj)
        }
        prefs.edit().putString("invoices", array.toString()).apply()
    }

    fun loadInvoices(defaultList: List<Invoice>): List<Invoice> {
        val jsonStr = prefs.getString("invoices", null) ?: return defaultList
        val list = mutableListOf<Invoice>()
        try {
            val array = JSONArray(jsonStr)
            for (i in 0 until array.length()) {
                val invObj = array.getJSONObject(i)
                val itemsArray = invObj.getJSONArray("items")
                val items = mutableListOf<InvoiceItem>()
                for (j in 0 until itemsArray.length()) {
                    val itemObj = itemsArray.getJSONObject(j)
                    items.add(
                        InvoiceItem(
                            id = itemObj.getString("id"),
                            description = itemObj.getString("description"),
                            quantity = itemObj.getInt("quantity"),
                            unitPrice = itemObj.getDouble("unitPrice")
                        )
                    )
                }
                list.add(
                    Invoice(
                        id = invObj.getString("id"),
                        invoiceNumber = invObj.getString("invoiceNumber"),
                        patientName = invObj.getString("patientName"),
                        patientDoc = invObj.getString("patientDoc"),
                        patientPhone = invObj.optString("patientPhone", "300 123 4567"),
                        issueDate = invObj.getString("issueDate"),
                        dueDate = invObj.getString("dueDate"),
                        items = items,
                        discountPercent = invObj.optDouble("discountPercent", 0.0),
                        vatPercent = invObj.optDouble("vatPercent", 19.0)
                    )
                )
            }
        } catch (e: Exception) {
            return defaultList
        }
        return list
    }
}
