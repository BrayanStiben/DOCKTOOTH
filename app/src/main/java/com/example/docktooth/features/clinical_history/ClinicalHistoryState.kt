package com.example.docktooth.features.clinical_history

import com.example.docktooth.domain.model.ClinicalRecord
import com.example.docktooth.domain.model.UserRole

data class ClinicalHistoryState(
    val patientName: String = "Laura Torres",
    val patientDoc: String = "CC 1.234.567.890",
    val patientAgeGender: String = "28 años - Femenino",
    val selectedTab: Int = 0, // 0: Evolución clínica, 1: Odontograma, 2: Anamnesis
    val records: List<ClinicalRecord> = emptyList(),
    val currentUserRole: UserRole = UserRole.ODONTOLOGO,
    val showOdontogramNotice: Boolean = false
)
