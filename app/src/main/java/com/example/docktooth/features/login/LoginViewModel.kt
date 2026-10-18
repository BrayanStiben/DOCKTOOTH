package com.example.docktooth.features.login

import androidx.lifecycle.ViewModel
import com.example.docktooth.data.repository.AuthRepositoryImpl
import com.example.docktooth.domain.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class LoginViewModel(
    private val authRepository: AuthRepository = AuthRepositoryImpl()
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginState())
    val uiState: StateFlow<LoginState> = _uiState.asStateFlow()

    fun onEmailOrUserChanged(value: String) {
        _uiState.update { it.copy(emailOrUser = value, errorMessage = null) }
    }

    fun onPasswordChanged(value: String) {
        _uiState.update { it.copy(password = value, errorMessage = null) }
    }

    fun togglePasswordVisibility() {
        _uiState.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
    }

    fun onRememberMeChanged(checked: Boolean) {
        _uiState.update { it.copy(rememberMe = checked) }
    }

    fun login() {
        val state = _uiState.value
        if (state.emailOrUser.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Ingrese su correo o usuario") }
            return
        }
        if (state.password.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Ingrese su contraseña") }
            return
        }

        _uiState.update { it.copy(isLoading = true) }
        val success = authRepository.login(state.emailOrUser, state.password)
        if (success) {
            _uiState.update { it.copy(isLoading = false, isSuccess = true) }
        } else {
            _uiState.update { it.copy(isLoading = false, errorMessage = "Credenciales incorrectas") }
        }
    }

    fun selectPrecreatedUser(user: com.example.docktooth.domain.model.User) {
        _uiState.update {
            it.copy(
                emailOrUser = user.email,
                password = "123456",
                errorMessage = null
            )
        }
    }

    fun loginAsUser(user: com.example.docktooth.domain.model.User) {
        selectPrecreatedUser(user)
        login()
    }

    fun resetSuccess() {
        _uiState.update { it.copy(isSuccess = false) }
    }
}
