package com.example.docktooth.features.clinical_history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.docktooth.data.repository.AuthRepositoryImpl
import com.example.docktooth.data.repository.ClinicalRecordRepositoryImpl
import com.example.docktooth.domain.repository.AuthRepository
import com.example.docktooth.domain.repository.ClinicalRecordRepository
import kotlinx.coroutines.flow.*

class ClinicalHistoryViewModel(
    private val clinicalRecordRepository: ClinicalRecordRepository = ClinicalRecordRepositoryImpl(),
    private val authRepository: AuthRepository = AuthRepositoryImpl()
) : ViewModel() {

    private val _selectedTab = MutableStateFlow(0)
    private val _showNotice = MutableStateFlow(false)

    val uiState: StateFlow<ClinicalHistoryState> = combine(
        clinicalRecordRepository.records,
        authRepository.currentUser,
        _selectedTab,
        _showNotice
    ) { records, user, tab, notice ->
        ClinicalHistoryState(
            patientName = "Laura Torres",
            patientDoc = "CC 1.234.567.890",
            patientAgeGender = "28 años - Femenino",
            selectedTab = tab,
            records = records,
            currentUserRole = user.role,
            showOdontogramNotice = notice
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ClinicalHistoryState()
    )

    fun onTabSelected(index: Int) {
        if (index == 1) { // Odontograma (screen 6 skipped)
            _showNotice.value = true
        } else {
            _showNotice.value = false
            _selectedTab.value = index
        }
    }

    fun dismissNotice() {
        _showNotice.value = false
    }
}
