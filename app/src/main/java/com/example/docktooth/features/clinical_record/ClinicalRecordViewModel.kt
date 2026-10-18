package com.example.docktooth.features.clinical_record

import androidx.lifecycle.ViewModel
import com.example.docktooth.data.repository.ClinicalRecordRepositoryImpl
import com.example.docktooth.domain.model.ClinicalRecord
import com.example.docktooth.domain.repository.ClinicalRecordRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class ClinicalRecordState(
    val reason: String = "Dolor en molar inferior derecho",
    val examination: String = "Se observan caries profunda en 46, sin signos de absceso.",
    val diagnosis: String = "Caries dental profunda - 46",
    val procedure: String = "Restauración con resina",
    val observations: String = "Paciente refiere sensibilidad al frío y calor.",
    val includePrescription: Boolean = true,
    val isSaved: Boolean = false
)

class ClinicalRecordViewModel(
    private val clinicalRecordRepository: ClinicalRecordRepository = ClinicalRecordRepositoryImpl()
) : ViewModel() {

    private val _uiState = MutableStateFlow(ClinicalRecordState())
    val uiState: StateFlow<ClinicalRecordState> = _uiState.asStateFlow()

    fun onReasonChanged(v: String) { _uiState.update { it.copy(reason = v) } }
    fun onExaminationChanged(v: String) { _uiState.update { it.copy(examination = v) } }
    fun onDiagnosisChanged(v: String) { _uiState.update { it.copy(diagnosis = v) } }
    fun onProcedureChanged(v: String) { _uiState.update { it.copy(procedure = v) } }
    fun onObservationsChanged(v: String) { _uiState.update { it.copy(observations = v) } }
    fun onPrescriptionChanged(checked: Boolean) { _uiState.update { it.copy(includePrescription = checked) } }

    fun saveRecord() {
        val s = _uiState.value
        val newRecord = ClinicalRecord(
            id = System.currentTimeMillis().toString(),
            date = "24/04/2025",
            type = "Evolución",
            professional = "Dr. Carlos Ramírez",
            reason = s.reason,
            examination = s.examination,
            diagnosis = s.diagnosis,
            procedure = s.procedure,
            observations = s.observations,
            includePrescription = s.includePrescription
        )
        clinicalRecordRepository.addRecord(newRecord)
        _uiState.update { it.copy(isSaved = true) }
    }
}
