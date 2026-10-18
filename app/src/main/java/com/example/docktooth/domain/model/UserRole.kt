package com.example.docktooth.domain.model

enum class UserRole(val label: String) {
    ODONTOLOGO("Odontólogo General"),
    ESPECIALISTA("Odontólogo Especialista"),
    RECEPCIONISTA("Recepcionista"),
    PACIENTE("Paciente");

    companion object {
        fun fromString(value: String): UserRole {
            return entries.find { it.label.equals(value, ignoreCase = true) || it.name.equals(value, ignoreCase = true) }
                ?: ODONTOLOGO
        }
    }
}
