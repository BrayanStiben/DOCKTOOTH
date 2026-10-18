package com.example.docktooth.domain.model

data class InvoiceItem(
    val id: String,
    val description: String,
    val quantity: Int,
    val unitPrice: Double
) {
    val total: Double get() = quantity * unitPrice
}

data class Invoice(
    val id: String,
    val invoiceNumber: String,
    val patientName: String,
    val patientDoc: String,
    val patientPhone: String = "300 123 4567",
    val issueDate: String,
    val dueDate: String,
    val items: List<InvoiceItem>,
    val discountPercent: Double = 0.0,
    val vatPercent: Double = 19.0
) {
    val subtotal: Double get() = items.sumOf { it.total }
    val discountAmount: Double get() = subtotal * (discountPercent / 100.0)
    val vatAmount: Double get() = (subtotal - discountAmount) * (vatPercent / 100.0)
    val grandTotal: Double get() = subtotal - discountAmount + vatAmount
}
