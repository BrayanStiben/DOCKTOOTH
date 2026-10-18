package com.example.docktooth.core.util

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.os.Environment
import android.os.ParcelFileDescriptor
import android.print.PageRange
import android.print.PrintAttributes
import android.print.PrintDocumentAdapter
import android.print.PrintDocumentInfo
import android.print.PrintManager
import android.widget.Toast
import androidx.core.content.FileProvider
import com.example.docktooth.domain.model.Invoice
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.text.NumberFormat
import java.util.Locale

object InvoicePdfUtils {

    private val currencyFormat = NumberFormat.getCurrencyInstance(Locale("es", "CO")).apply {
        maximumFractionDigits = 0
    }

    /**
     * Generates a standard A4 PDF document for the given Invoice.
     * Returns the generated File.
     */
    fun createInvoicePdfFile(context: Context, invoice: Invoice): File {
        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create() // A4 standard size
        val page = pdfDocument.startPage(pageInfo)
        val canvas = page.canvas

        val paint = Paint().apply {
            isAntiAlias = true
        }

        // 1. Primary Blue Header Bar
        paint.color = Color.parseColor("#1A4050")
        canvas.drawRect(0f, 0f, 595f, 90f, paint)

        // Header Title
        paint.color = Color.WHITE
        paint.textSize = 24f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        canvas.drawText("Doc-Tooth", 30f, 45f, paint)

        paint.textSize = 12f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        canvas.drawText("Gestión Odontológica Integral", 30f, 65f, paint)

        // Header Business Info
        paint.textSize = 10f
        canvas.drawText("NIT 901.234.567-8", 430f, 35f, paint)
        canvas.drawText("Cra 12 # 45-67, Armenia", 430f, 50f, paint)
        canvas.drawText("Tel. 300 123 4567", 430f, 65f, paint)

        // 2. Invoice Title Banner
        paint.color = Color.parseColor("#1E293B")
        paint.textSize = 18f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        canvas.drawText("FACTURA DE VENTA N° ${invoice.invoiceNumber}", 30f, 125f, paint)

        paint.color = Color.parseColor("#64748B")
        paint.textSize = 11f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        canvas.drawText("Fecha Emisión: ${invoice.issueDate}   |   Vencimiento: ${invoice.dueDate}", 30f, 142f, paint)

        // 3. Patient Info Box
        paint.color = Color.parseColor("#F1F5F9")
        canvas.drawRoundRect(30f, 160f, 565f, 230f, 12f, 12f, paint)

        paint.color = Color.parseColor("#1A4050")
        paint.textSize = 11f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        canvas.drawText("DATOS DEL PACIENTE", 45f, 180f, paint)

        paint.color = Color.parseColor("#0F172A")
        paint.textSize = 12f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        canvas.drawText("Nombre: ${invoice.patientName}", 45f, 200f, paint)

        paint.textSize = 11f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        canvas.drawText("Documento: ${invoice.patientDoc}    |    Teléfono: ${invoice.patientPhone}", 45f, 218f, paint)

        // 4. Procedures Table Header
        val tableTop = 250f
        paint.color = Color.parseColor("#E2E8F0")
        canvas.drawRect(30f, tableTop, 565f, tableTop + 25f, paint)

        paint.color = Color.parseColor("#0F172A")
        paint.textSize = 11f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        canvas.drawText("Descripción del procedimiento", 40f, tableTop + 17f, paint)
        canvas.drawText("Cant.", 370f, tableTop + 17f, paint)
        canvas.drawText("Total ($)", 480f, tableTop + 17f, paint)

        // Table Items
        var currentY = tableTop + 45f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)

        invoice.items.forEach { item ->
            paint.color = Color.parseColor("#0F172A")
            canvas.drawText(item.description, 40f, currentY, paint)
            canvas.drawText("${item.quantity}", 380f, currentY, paint)
            canvas.drawText(currencyFormat.format(item.total), 480f, currentY, paint)

            paint.color = Color.parseColor("#CBD5E1")
            canvas.drawLine(30f, currentY + 10f, 565f, currentY + 10f, paint)

            currentY += 30f
        }

        // 5. Totals Section
        val totalsY = currentY + 15f
        paint.color = Color.parseColor("#F8FAFC")
        canvas.drawRoundRect(340f, totalsY, 565f, totalsY + 100f, 8f, 8f, paint)

        paint.color = Color.parseColor("#475569")
        paint.textSize = 11f
        canvas.drawText("Subtotal:", 355f, totalsY + 25f, paint)
        canvas.drawText(currencyFormat.format(invoice.subtotal), 470f, totalsY + 25f, paint)

        canvas.drawText("IVA (${invoice.vatPercent.toInt()}%):", 355f, totalsY + 45f, paint)
        canvas.drawText(currencyFormat.format(invoice.vatAmount), 470f, totalsY + 45f, paint)

        paint.color = Color.parseColor("#1A4050")
        paint.textSize = 13f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        canvas.drawText("TOTAL A PAGAR:", 355f, totalsY + 75f, paint)
        canvas.drawText(currencyFormat.format(invoice.grandTotal), 470f, totalsY + 75f, paint)

        // 6. Footer Note
        paint.color = Color.parseColor("#94A3B8")
        paint.textSize = 10f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.ITALIC)
        canvas.drawText("¡Gracias por confiar en Doc-Tooth! Factura generada electrónicamente.", 30f, 800f, paint)

        pdfDocument.finishPage(page)

        // Save PDF file
        val fileName = "Factura_${invoice.invoiceNumber.replace("#", "")}.pdf"
        val outputDir = File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) ?: context.cacheDir, "facturas").apply {
            mkdirs()
        }
        val pdfFile = File(outputDir, fileName)

        FileOutputStream(pdfFile).use { out ->
            pdfDocument.writeTo(out)
        }
        pdfDocument.close()

        return pdfFile
    }

    /**
     * Downloads (saves copy to public Downloads) and opens/previews PDF.
     */
    fun downloadInvoicePdf(context: Context, invoice: Invoice) {
        try {
            val pdfFile = createInvoicePdfFile(context, invoice)

            // Try to copy to public Downloads folder
            try {
                val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                if (downloadsDir.exists() || downloadsDir.mkdirs()) {
                    val destFile = File(downloadsDir, pdfFile.name)
                    pdfFile.copyTo(destFile, overwrite = true)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            val uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                pdfFile
            )

            Toast.makeText(
                context,
                "📄 PDF guardado exitosamente: ${pdfFile.name}",
                Toast.LENGTH_LONG
            ).show()

            val openIntent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(uri, "application/pdf")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

            val chooser = Intent.createChooser(openIntent, "Abrir Factura PDF")
            chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(chooser)
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Error al generar PDF: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Sends the invoice PDF as an attachment via email.
     */
    fun sendInvoiceByEmail(context: Context, invoice: Invoice) {
        try {
            val pdfFile = createInvoicePdfFile(context, invoice)
            val uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                pdfFile
            )

            val emailIntent = Intent(Intent.ACTION_SEND).apply {
                type = "application/pdf"
                putExtra(Intent.EXTRA_SUBJECT, "Factura de Venta ${invoice.invoiceNumber} - Doc-Tooth")
                putExtra(
                    Intent.EXTRA_TEXT,
                    """
                    Estimado(a) ${invoice.patientName},

                    Adjuntamos la factura de venta N° ${invoice.invoiceNumber} emitida el ${invoice.issueDate} por un valor total de ${currencyFormat.format(invoice.grandTotal)}.

                    Agradecemos su confianza en nuestros servicios odontológicos.

                    Atentamente,
                    Doc-Tooth Clínica Odontológica
                    Tel: 300 123 4567 | Armenia, Quindío
                    """.trimIndent()
                )
                putExtra(Intent.EXTRA_STREAM, uri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

            val chooser = Intent.createChooser(emailIntent, "Enviar Factura por Correo...")
            chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(chooser)
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Error al preparar correo: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Launches the native Android Print Framework dialog.
     */
    fun printInvoice(context: Context, invoice: Invoice) {
        try {
            val pdfFile = createInvoicePdfFile(context, invoice)
            val printManager = context.getSystemService(Context.PRINT_SERVICE) as? PrintManager

            if (printManager == null) {
                Toast.makeText(context, "El servicio de impresión no está disponible en este dispositivo", Toast.LENGTH_SHORT).show()
                return
            }

            val printAdapter = object : PrintDocumentAdapter() {
                override fun onLayout(
                    oldAttributes: PrintAttributes?,
                    newAttributes: PrintAttributes?,
                    cancellationSignal: android.os.CancellationSignal?,
                    callback: LayoutResultCallback?,
                    extras: android.os.Bundle?
                ) {
                    if (cancellationSignal?.isCanceled == true) {
                        callback?.onLayoutCancelled()
                        return
                    }

                    val pdi = PrintDocumentInfo.Builder("Factura_${invoice.invoiceNumber.replace("#", "")}.pdf")
                        .setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
                        .setPageCount(1)
                        .build()

                    callback?.onLayoutFinished(pdi, true)
                }

                override fun onWrite(
                    pages: Array<out PageRange>?,
                    destination: ParcelFileDescriptor?,
                    cancellationSignal: android.os.CancellationSignal?,
                    callback: WriteResultCallback?
                ) {
                    try {
                        FileInputStream(pdfFile).use { input ->
                            FileOutputStream(destination?.fileDescriptor).use { output ->
                                input.copyTo(output)
                            }
                        }
                        callback?.onWriteFinished(arrayOf(PageRange.ALL_PAGES))
                    } catch (e: Exception) {
                        e.printStackTrace()
                        callback?.onWriteFailed(e.message)
                    }
                }
            }

            printManager.print("Factura_${invoice.invoiceNumber}", printAdapter, PrintAttributes.Builder().build())
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Error al iniciar impresión: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
        }
    }
}
