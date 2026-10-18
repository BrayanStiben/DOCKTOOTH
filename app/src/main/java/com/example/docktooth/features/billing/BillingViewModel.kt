package com.example.docktooth.features.billing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.docktooth.data.repository.AuthRepositoryImpl
import com.example.docktooth.data.repository.InvoiceRepositoryImpl
import com.example.docktooth.domain.model.Invoice
import com.example.docktooth.domain.model.InvoiceItem
import com.example.docktooth.domain.model.UserRole
import com.example.docktooth.domain.repository.AuthRepository
import com.example.docktooth.domain.repository.InvoiceRepository
import kotlinx.coroutines.flow.*

data class BillingState(
    val patientName: String = "Laura Torres",
    val patientDoc: String = "CC 1.234.567.890",
    val items: List<InvoiceItem> = listOf(
        InvoiceItem("1", "Consulta odontológica", 1, 80000.0),
        InvoiceItem("2", "Limpieza dental", 1, 120000.0),
        InvoiceItem("3", "Resina compuesta", 1, 250000.0)
    ),
    val discountPercent: Double = 0.0,
    val vatPercent: Double = 19.0,
    val isSaved: Boolean = false,
    val showAddItemDialog: Boolean = false,
    val currentUserRole: UserRole = UserRole.RECEPCIONISTA
) {
    val subtotal: Double get() = items.sumOf { it.total }
    val discountAmount: Double get() = subtotal * (discountPercent / 100.0)
    val vatAmount: Double get() = (subtotal - discountAmount) * (vatPercent / 100.0)
    val grandTotal: Double get() = subtotal - discountAmount + vatAmount
}

class BillingViewModel(
    private val invoiceRepository: InvoiceRepository = InvoiceRepositoryImpl(),
    authRepository: AuthRepository = AuthRepositoryImpl()
) : ViewModel() {

    private val _items = MutableStateFlow(
        listOf(
            InvoiceItem("1", "Consulta odontológica", 1, 80000.0),
            InvoiceItem("2", "Limpieza dental", 1, 120000.0),
            InvoiceItem("3", "Resina compuesta", 1, 250000.0)
        )
    )
    private val _isSaved = MutableStateFlow(false)
    private val _showAddItemDialog = MutableStateFlow(false)

    val uiState: StateFlow<BillingState> = combine(
        authRepository.currentUser,
        _items,
        _isSaved,
        _showAddItemDialog
    ) { user, items, isSaved, showDlg ->
        BillingState(
            patientName = if (user.role == UserRole.PACIENTE) user.fullName else "Laura Torres",
            patientDoc = if (user.role == UserRole.PACIENTE) "CC ${user.documentId}" else "CC 1.234.567.890",
            items = items,
            isSaved = isSaved,
            showAddItemDialog = showDlg,
            currentUserRole = user.role
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = BillingState()
    )

    fun removeItem(id: String) {
        _items.update { list -> list.filterNot { it.id == id } }
    }

    fun setShowAddItemDialog(show: Boolean) {
        _showAddItemDialog.value = show
    }

    fun addItem(description: String, quantity: Int, unitPrice: Double) {
        if (description.isBlank()) return
        val newItem = InvoiceItem(
            id = System.currentTimeMillis().toString(),
            description = description,
            quantity = quantity,
            unitPrice = unitPrice
        )
        _items.update { it + newItem }
        _showAddItemDialog.value = false
    }

    fun buildInvoiceObject(): Invoice {
        val s = uiState.value
        return Invoice(
            id = System.currentTimeMillis().toString(),
            invoiceNumber = "#F-${(100000..999999).random()}",
            patientName = s.patientName,
            patientDoc = s.patientDoc,
            patientPhone = "300 123 4567",
            issueDate = "24/04/2025",
            dueDate = "24/05/2025",
            items = s.items,
            discountPercent = s.discountPercent,
            vatPercent = s.vatPercent
        )
    }

    fun saveInvoice() {
        val invoice = buildInvoiceObject()
        invoiceRepository.saveInvoice(invoice)
        _isSaved.value = true
    }

    fun resetSaved() {
        _isSaved.value = false
    }
}
