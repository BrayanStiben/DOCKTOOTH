package com.example.docktooth.domain.repository

import com.example.docktooth.domain.model.Invoice
import kotlinx.coroutines.flow.StateFlow

interface InvoiceRepository {
    val invoices: StateFlow<List<Invoice>>
    val currentInvoice: StateFlow<Invoice?>
    fun saveInvoice(invoice: Invoice)
    fun setCurrentInvoice(invoice: Invoice)
}
