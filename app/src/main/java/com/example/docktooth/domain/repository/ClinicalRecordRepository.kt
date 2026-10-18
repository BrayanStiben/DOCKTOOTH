package com.example.docktooth.domain.repository

import com.example.docktooth.domain.model.ClinicalRecord
import kotlinx.coroutines.flow.StateFlow

interface ClinicalRecordRepository {
    val records: StateFlow<List<ClinicalRecord>>
    fun addRecord(record: ClinicalRecord)
}
