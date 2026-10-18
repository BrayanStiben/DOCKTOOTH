package com.example.docktooth.features.invoice_view

import androidx.lifecycle.ViewModel
import com.example.docktooth.data.repository.InvoiceRepositoryImpl
import com.example.docktooth.domain.model.Invoice
import com.example.docktooth.domain.repository.InvoiceRepository
import kotlinx.coroutines.flow.StateFlow

class InvoiceViewViewModel(
    private val invoiceRepository: InvoiceRepository = InvoiceRepositoryImpl()
) : ViewModel() {

    val currentInvoice: StateFlow<Invoice?> = invoiceRepository.currentInvoice
}
