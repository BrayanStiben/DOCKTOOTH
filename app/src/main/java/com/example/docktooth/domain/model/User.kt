package com.example.docktooth.domain.model

data class User(
    val id: String,
    val fullName: String,
    val documentId: String,
    val email: String,
    val phone: String,
    val role: UserRole,
    val specialty: String? = null
)
