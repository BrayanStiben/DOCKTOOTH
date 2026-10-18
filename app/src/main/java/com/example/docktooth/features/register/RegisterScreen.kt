package com.example.docktooth.features.register

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.docktooth.core.component.DocToothTextField
import com.example.docktooth.core.component.DocToothTopBar
import com.example.docktooth.core.component.PrimaryButton
import com.example.docktooth.domain.model.UserRole

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onBackClick: () -> Unit,
    onRegisterSuccess: () -> Unit,
    viewModel: RegisterViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) {
            viewModel.resetSuccess()
            onRegisterSuccess()
        }
    }

    Scaffold(
        topBar = {
            DocToothTopBar(
                title = "Crear cuenta",
                onBackClick = onBackClick,
                showLogo = false
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFFF5F7FA))
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            DocToothTextField(
                value = state.fullName,
                onValueChange = viewModel::onFullNameChanged,
                label = "Nombre y apellidos",
                leadingIcon = Icons.Default.Person
            )

            Spacer(modifier = Modifier.height(12.dp))

            DocToothTextField(
                value = state.documentId,
                onValueChange = viewModel::onDocumentIdChanged,
                label = "Documento de identidad",
                leadingIcon = Icons.Default.Badge,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Spacer(modifier = Modifier.height(12.dp))

            DocToothTextField(
                value = state.email,
                onValueChange = viewModel::onEmailChanged,
                label = "Correo electrónico",
                leadingIcon = Icons.Default.Email,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )

            Spacer(modifier = Modifier.height(12.dp))

            DocToothTextField(
                value = state.phone,
                onValueChange = viewModel::onPhoneChanged,
                label = "Teléfono",
                leadingIcon = Icons.Default.Phone,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Role selector
            var expandedRole by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = expandedRole,
                onExpandedChange = { expandedRole = !expandedRole }
            ) {
                OutlinedTextField(
                    value = state.role.label,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Rol profesional / perfil") },
                    leadingIcon = { Icon(Icons.Default.Work, contentDescription = null) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedRole) },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(unfocusedContainerColor = Color.White),
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = expandedRole,
                    onDismissRequest = { expandedRole = false }
                ) {
                    UserRole.entries.forEach { role ->
                        DropdownMenuItem(
                            text = { Text(role.label) },
                            onClick = {
                                viewModel.onRoleChanged(role)
                                expandedRole = false
                            }
                        )
                    }
                }
            }

            if (state.role == UserRole.ODONTOLOGO) {
                Spacer(modifier = Modifier.height(12.dp))
                DocToothTextField(
                    value = state.specialty,
                    onValueChange = viewModel::onSpecialtyChanged,
                    label = "Especialidad",
                    leadingIcon = Icons.Default.LocalHospital
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            DocToothTextField(
                value = state.password,
                onValueChange = viewModel::onPasswordChanged,
                label = "Contraseña",
                leadingIcon = Icons.Default.Lock,
                visualTransformation = if (state.isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    IconButton(onClick = viewModel::togglePasswordVisibility) {
                        Icon(
                            imageVector = if (state.isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = "Ver contraseña"
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            DocToothTextField(
                value = state.confirmPassword,
                onValueChange = viewModel::onConfirmPasswordChanged,
                label = "Confirmar contraseña",
                leadingIcon = Icons.Default.Lock,
                visualTransformation = if (state.isConfirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    IconButton(onClick = viewModel::toggleConfirmPasswordVisibility) {
                        Icon(
                            imageVector = if (state.isConfirmPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = "Ver contraseña"
                        )
                    }
                }
            )

            if (state.errorMessage != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = state.errorMessage!!,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 13.sp
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            PrimaryButton(
                text = if (state.isLoading) "Guardando..." else "Crear cuenta",
                onClick = viewModel::register,
                enabled = !state.isLoading
            )

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
