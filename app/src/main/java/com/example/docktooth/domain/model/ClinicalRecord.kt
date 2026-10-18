package com.example.docktooth.domain.model

data class ClinicalRecord(
    val id: String,
    val date: String,
    val type: String, // Evolución, Diagnóstico, Control
    val professional: String,
    val reason: String = "",
    val examination: String = "",
    val diagnosis: String = "",
    val procedure: String = "",
    val observations: String = "",
    val includePrescription: Boolean = false
)
