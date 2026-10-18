package com.example.docktooth.features.register

import androidx.lifecycle.ViewModel
import com.example.docktooth.data.repository.AuthRepositoryImpl
import com.example.docktooth.domain.model.User
import com.example.docktooth.domain.model.UserRole
import com.example.docktooth.domain.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class RegisterViewModel(
    private val authRepository: AuthRepository = AuthRepositoryImpl()
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterState())
    val uiState: StateFlow<RegisterState> = _uiState.asStateFlow()

    fun onFullNameChanged(value: String) { _uiState.update { it.copy(fullName = value, errorMessage = null) } }
    fun onDocumentIdChanged(value: String) { _uiState.update { it.copy(documentId = value, errorMessage = null) } }
    fun onEmailChanged(value: String) { _uiState.update { it.copy(email = value, errorMessage = null) } }
    fun onPhoneChanged(value: String) { _uiState.update { it.copy(phone = value, errorMessage = null) } }
    fun onRoleChanged(role: UserRole) { _uiState.update { it.copy(role = role) } }
    fun onSpecialtyChanged(value: String) { _uiState.update { it.copy(specialty = value) } }
    fun onPasswordChanged(value: String) { _uiState.update { it.copy(password = value, errorMessage = null) } }
    fun onConfirmPasswordChanged(value: String) { _uiState.update { it.copy(confirmPassword = value, errorMessage = null) } }

    fun togglePasswordVisibility() { _uiState.update { it.copy(isPasswordVisible = !it.isPasswordVisible) } }
    fun toggleConfirmPasswordVisibility() { _uiState.update { it.copy(isConfirmPasswordVisible = !it.isConfirmPasswordVisible) } }

    fun register() {
        val s = _uiState.value
        if (s.fullName.isBlank() || s.documentId.isBlank() || s.email.isBlank() || s.password.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Por favor complete todos los campos obligatorios") }
            return
        }
        if (s.password != s.confirmPassword) {
            _uiState.update { it.copy(errorMessage = "Las contraseñas no coinciden") }
            return
        }

        _uiState.update { it.copy(isLoading = true) }
        val user = User(
            id = System.currentTimeMillis().toString(),
            fullName = s.fullName,
            documentId = s.documentId,
            email = s.email,
            phone = s.phone,
            role = s.role,
            specialty = if (s.role == UserRole.ODONTOLOGO) s.specialty else null
        )
        val ok = authRepository.register(user)
        if (ok) {
            _uiState.update { it.copy(isLoading = false, isSuccess = true) }
        } else {
            _uiState.update { it.copy(isLoading = false, errorMessage = "Error al crear la cuenta") }
        }
    }

    fun resetSuccess() {
        _uiState.update { it.copy(isSuccess = false) }
    }
}
