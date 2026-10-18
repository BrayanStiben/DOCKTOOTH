package com.example.docktooth.features.invoice_view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Print
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.docktooth.R
import com.example.docktooth.core.component.DocToothTopBar
import com.example.docktooth.core.theme.PrimaryBlue
import com.example.docktooth.core.util.InvoicePdfUtils
import java.text.NumberFormat
import java.util.Locale

@Composable
fun InvoiceViewScreen(
    onBackClick: () -> Unit,
    viewModel: InvoiceViewViewModel = viewModel()
) {
    val invoice by viewModel.currentInvoice.collectAsState()
    val context = LocalContext.current
    val currencyFormat = NumberFormat.getCurrencyInstance(Locale("es", "CO")).apply {
        maximumFractionDigits = 0
    }

    val inv = invoice ?: return

    Scaffold(
        topBar = {
            DocToothTopBar(
                title = "Factura ${inv.invoiceNumber}",
                onBackClick = onBackClick,
                showLogo = false,
                actions = {
                    IconButton(onClick = { InvoicePdfUtils.printInvoice(context, inv) }) {
                        Icon(Icons.Default.Print, contentDescription = "Imprimir", tint = Color.White)
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFFEBF4F8))
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Receipt Paper Card
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color(0xFFCBD5E1), RoundedCornerShape(12.dp))
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    // Receipt Header with Logo & Business Info
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(Color.White, RoundedCornerShape(18.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.logo),
                                    contentDescription = "Logo",
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "Doc-Tooth",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = PrimaryBlue
                                )
                                Text(
                                    text = "Gestión Odontológica Integral",
                                    fontSize = 10.sp,
                                    color = Color.Gray
                                )
                            }
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text("NIT 901.234.567-8", fontSize = 10.sp, color = Color.DarkGray)
                            Text("Cra 12 # 45-67", fontSize = 10.sp, color = Color.DarkGray)
                            Text("Armenia, Quindío", fontSize = 10.sp, color = Color.DarkGray)
                        }
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color(0xFFE2E8F0))

                    // Patient and Invoice Meta
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Datos del paciente", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = PrimaryBlue)
                            Text(inv.patientName, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                            Text(inv.patientDoc, fontSize = 12.sp, color = Color.DarkGray)
                            Text(inv.patientPhone, fontSize = 12.sp, color = Color.DarkGray)
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text("Fecha emisión", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = PrimaryBlue)
                            Text(inv.issueDate, fontSize = 12.sp, color = Color.DarkGray)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Vencimiento", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = PrimaryBlue)
                            Text(inv.dueDate, fontSize = 12.sp, color = Color.DarkGray)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Items Header
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFF1F5F9), RoundedCornerShape(6.dp))
                            .padding(horizontal = 8.dp, vertical = 6.dp)
                    ) {
                        Text("Descripción", fontWeight = FontWeight.Bold, fontSize = 11.sp, modifier = Modifier.weight(2f))
                        Text("Cant.", fontWeight = FontWeight.Bold, fontSize = 11.sp, textAlign = TextAlign.Center, modifier = Modifier.weight(0.6f))
                        Text("Valor", fontWeight = FontWeight.Bold, fontSize = 11.sp, textAlign = TextAlign.End, modifier = Modifier.weight(1.2f))
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    inv.items.forEach { item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(item.description, fontSize = 12.sp, modifier = Modifier.weight(2f))
                            Text("${item.quantity}", fontSize = 12.sp, textAlign = TextAlign.Center, modifier = Modifier.weight(0.6f))
                            Text(currencyFormat.format(item.total), fontSize = 12.sp, textAlign = TextAlign.End, modifier = Modifier.weight(1.2f))
                        }
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color(0xFFE2E8F0))

                    // Calculations
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.End
                    ) {
                        Row(modifier = Modifier.width(180.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Subtotal", fontSize = 12.sp, color = Color.Gray)
                            Text(currencyFormat.format(inv.subtotal), fontSize = 12.sp)
                        }
                        Row(modifier = Modifier.width(180.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Descuento (0%)", fontSize = 12.sp, color = Color.Gray)
                            Text(currencyFormat.format(inv.discountAmount), fontSize = 12.sp)
                        }
                        Row(modifier = Modifier.width(180.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("IVA (19%)", fontSize = 12.sp, color = Color.Gray)
                            Text(currencyFormat.format(inv.vatAmount), fontSize = 12.sp)
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(modifier = Modifier.width(180.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Total", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = PrimaryBlue)
                            Text(currencyFormat.format(inv.grandTotal), fontSize = 14.sp, fontWeight = FontWeight.Bold, color = PrimaryBlue)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { InvoicePdfUtils.downloadInvoicePdf(context, inv) },
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Download, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Descargar PDF", fontSize = 11.sp)
                }

                Button(
                    onClick = { InvoicePdfUtils.printInvoice(context, inv) },
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Print, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Imprimir", fontSize = 11.sp)
                }

                Button(
                    onClick = { InvoicePdfUtils.sendInvoiceByEmail(context, inv) },
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Email, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Enviar por correo", fontSize = 11.sp)
                }
            }
        }
    }
}
