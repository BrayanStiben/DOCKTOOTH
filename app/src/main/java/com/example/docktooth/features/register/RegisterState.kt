package com.example.docktooth.features.register

import com.example.docktooth.domain.model.UserRole

data class RegisterState(
    val fullName: String = "",
    val documentId: String = "",
    val email: String = "",
    val phone: String = "",
    val role: UserRole = UserRole.ODONTOLOGO,
    val specialty: String = "Odontología general",
    val password: String = "",
    val confirmPassword: String = "",
    val isPasswordVisible: Boolean = false,
    val isConfirmPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isSuccess: Boolean = false
)
