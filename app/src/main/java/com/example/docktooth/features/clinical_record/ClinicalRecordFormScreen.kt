package com.example.docktooth.features.clinical_record

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.docktooth.core.component.DocToothTextField
import com.example.docktooth.core.component.DocToothTopBar
import com.example.docktooth.core.component.PrimaryButton
import com.example.docktooth.core.component.SecondaryButton
import com.example.docktooth.core.theme.PrimaryBlue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClinicalRecordFormScreen(
    onBackClick: () -> Unit,
    onRecordSaved: () -> Unit,
    viewModel: ClinicalRecordViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(state.isSaved) {
        if (state.isSaved) {
            onRecordSaved()
        }
    }

    val proceduresList = listOf(
        "Restauración con resina",
        "Limpieza dental / Profilaxis",
        "Endodoncia molar",
        "Extracción simple",
        "Sellantes de fotocurado",
        "Blanqueamiento dental"
    )

    Scaffold(
        topBar = {
            DocToothTopBar(
                title = "Registro clínico",
                onBackClick = onBackClick,
                showLogo = true
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFFF5F7FA))
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            DocToothTextField(
                value = state.reason,
                onValueChange = viewModel::onReasonChanged,
                label = "Motivo de consulta"
            )

            Spacer(modifier = Modifier.height(14.dp))

            DocToothTextField(
                value = state.examination,
                onValueChange = viewModel::onExaminationChanged,
                label = "Examen odontológico",
                singleLine = false,
                maxLines = 3
            )

            Spacer(modifier = Modifier.height(14.dp))

            DocToothTextField(
                value = state.diagnosis,
                onValueChange = viewModel::onDiagnosisChanged,
                label = "Diagnóstico"
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Procedimiento Exposed Dropdown
            var expandedProc by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = expandedProc,
                onExpandedChange = { expandedProc = !expandedProc }
            ) {
                OutlinedTextField(
                    value = state.procedure,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Procedimiento") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedProc) },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = Color(0xFFF8FAFC),
                        focusedContainerColor = Color.White
                    ),
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = expandedProc,
                    onDismissRequest = { expandedProc = false }
                ) {
                    proceduresList.forEach { proc ->
                        DropdownMenuItem(
                            text = { Text(proc) },
                            onClick = {
                                viewModel.onProcedureChanged(proc)
                                expandedProc = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            DocToothTextField(
                value = state.observations,
                onValueChange = viewModel::onObservationsChanged,
                label = "Observaciones",
                singleLine = false,
                maxLines = 3
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Checkbox(
                    checked = state.includePrescription,
                    onCheckedChange = viewModel::onPrescriptionChanged,
                    colors = CheckboxDefaults.colors(checkedColor = PrimaryBlue)
                )
                Text(
                    text = "Incluir prescripción médica",
                    fontSize = 14.sp,
                    color = Color.DarkGray
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            PrimaryButton(
                text = "Guardar y firmar evolución",
                onClick = viewModel::saveRecord
            )

            Spacer(modifier = Modifier.height(12.dp))

            SecondaryButton(
                text = "Cancelar",
                onClick = onBackClick
            )

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
