package com.example.docktooth.features.billing

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.docktooth.core.component.DocToothBottomBar
import com.example.docktooth.core.component.DocToothTopBar
import com.example.docktooth.core.component.PrimaryButton
import com.example.docktooth.core.theme.PrimaryBlue
import com.example.docktooth.core.util.InvoicePdfUtils
import com.example.docktooth.domain.model.UserRole
import com.example.docktooth.navigation.Screen
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BillingScreen(
    onBackClick: () -> Unit,
    onNavigateToInvoiceView: () -> Unit,
    onNavigateToDashboard: (() -> Unit)? = null,
    onNavigateToAgenda: (() -> Unit)? = null,
    onNavigateToClinicalHistory: (() -> Unit)? = null,
    viewModel: BillingViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val currencyFormat = NumberFormat.getCurrencyInstance(Locale("es", "CO")).apply {
        maximumFractionDigits = 0
    }

    val canEditProcedures = state.currentUserRole != UserRole.PACIENTE

    LaunchedEffect(state.isSaved) {
        if (state.isSaved) {
            viewModel.resetSaved()
            onNavigateToInvoiceView()
        }
    }

    Scaffold(
        topBar = {
            DocToothTopBar(
                title = if (canEditProcedures) "Facturación" else "Mis Facturas",
                onBackClick = onBackClick,
                showLogo = true
            )
        },
        bottomBar = {
            DocToothBottomBar(
                currentRoute = Screen.Billing.route,
                onNavigateToRoute = { route ->
                    when (route) {
                        Screen.Dashboard.route -> onNavigateToDashboard?.invoke() ?: onBackClick()
                        Screen.Agenda.route -> onNavigateToAgenda?.invoke()
                        Screen.ClinicalHistory.route -> onNavigateToClinicalHistory?.invoke()
                    }
                },
                userRole = state.currentUserRole
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFFF5F7FA))
                .padding(16.dp)
        ) {
            // Patient Banner
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = state.patientName,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryBlue
                        )
                        Text(
                            text = state.patientDoc,
                            fontSize = 13.sp,
                            color = Color.Gray
                        )
                    }
                    if (canEditProcedures) {
                        IconButton(onClick = { }) {
                            Icon(Icons.Default.Search, contentDescription = "Buscar", tint = PrimaryBlue)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Procedimientos Section
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Procedimientos",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E293B)
                )

                // Solo disponible para Recepcionista / Odontólogo / Especialista
                if (canEditProcedures) {
                    TextButton(onClick = { viewModel.setShowAddItemDialog(true) }) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Agregar", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = PrimaryBlue)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Table Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFE2E8F0), RoundedCornerShape(8.dp))
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Descripción", fontWeight = FontWeight.Bold, fontSize = 12.sp, modifier = Modifier.weight(2f))
                Text("Cant.", fontWeight = FontWeight.Bold, fontSize = 12.sp, textAlign = TextAlign.Center, modifier = Modifier.weight(0.6f))
                Text("Valor", fontWeight = FontWeight.Bold, fontSize = 12.sp, textAlign = TextAlign.End, modifier = Modifier.weight(1.2f))
                if (canEditProcedures) {
                    Spacer(modifier = Modifier.width(36.dp))
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Items List
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                items(state.items) { item ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(item.description, fontSize = 13.sp, modifier = Modifier.weight(2f))
                            Text("${item.quantity}", fontSize = 13.sp, textAlign = TextAlign.Center, modifier = Modifier.weight(0.6f))
                            Text(
                                currencyFormat.format(item.total),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.End,
                                modifier = Modifier.weight(1.2f)
                            )
                            if (canEditProcedures) {
                                IconButton(
                                    onClick = { viewModel.removeItem(item.id) },
                                    modifier = Modifier.size(36.dp)
                                ) {
                                    Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = Color.Gray)
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Totals Card
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFFEBF4F8)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Subtotal", fontSize = 13.sp, color = Color.Gray)
                        Text(currencyFormat.format(state.subtotal), fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Descuento (${state.discountPercent.toInt()}%)", fontSize = 13.sp, color = Color.Gray)
                        Text(currencyFormat.format(state.discountAmount), fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("IVA (${state.vatPercent.toInt()}%)", fontSize = 13.sp, color = Color.Gray)
                        Text(currencyFormat.format(state.vatAmount), fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    }
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = Color(0xFFCBD5E1))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Total", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = PrimaryBlue)
                        Text(currencyFormat.format(state.grandTotal), fontSize = 18.sp, fontWeight = FontWeight.Bold, color = PrimaryBlue)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Primary Action / Informative Banner
            if (canEditProcedures) {
                PrimaryButton(
                    text = "Guardar factura",
                    onClick = viewModel::saveInvoice
                )
            } else {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE2F1F8)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Info, contentDescription = null, tint = PrimaryBlue)
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "Modo lectura: Esta es tu factura detallada. Los procedimientos y montos son administrados por el personal de recepción / clínica.",
                            fontSize = 12.sp,
                            color = PrimaryBlue,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Quick Export Action Buttons (Available for all roles)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = {
                        val inv = viewModel.buildInvoiceObject()
                        InvoicePdfUtils.downloadInvoicePdf(context, inv)
                    },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Download, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("PDF", fontSize = 11.sp)
                }

                OutlinedButton(
                    onClick = {
                        val inv = viewModel.buildInvoiceObject()
                        InvoicePdfUtils.printInvoice(context, inv)
                    },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Print, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Imprimir", fontSize = 11.sp)
                }

                OutlinedButton(
                    onClick = {
                        val inv = viewModel.buildInvoiceObject()
                        InvoicePdfUtils.sendInvoiceByEmail(context, inv)
                    },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Email, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Correo", fontSize = 11.sp)
                }
            }
        }
    }

    // Add Item Dialog (Only accessible if canEditProcedures is true)
    if (state.showAddItemDialog && canEditProcedures) {
        var desc by remember { mutableStateOf("") }
        var qty by remember { mutableStateOf("1") }
        var price by remember { mutableStateOf("50000") }

        AlertDialog(
            onDismissRequest = { viewModel.setShowAddItemDialog(false) },
            title = { Text("Agregar Procedimiento") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = desc,
                        onValueChange = { desc = it },
                        label = { Text("Descripción del servicio") }
                    )
                    OutlinedTextField(
                        value = qty,
                        onValueChange = { qty = it },
                        label = { Text("Cantidad") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    OutlinedTextField(
                        value = price,
                        onValueChange = { price = it },
                        label = { Text("Precio Unitario ($)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val q = qty.toIntOrNull() ?: 1
                        val p = price.toDoubleOrNull() ?: 0.0
                        viewModel.addItem(desc, q, p)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
                ) {
                    Text("Agregar")
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.setShowAddItemDialog(false) }) {
                    Text("Cancelar")
                }
            }
        )
    }
}
