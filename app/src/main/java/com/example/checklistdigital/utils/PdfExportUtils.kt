package com.example.checklistdigital.utils

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Matrix
import androidx.exifinterface.media.ExifInterface
import com.example.checklistdigital.R
import com.example.checklistdigital.data.Address
import com.example.checklistdigital.data.Client
import com.example.checklistdigital.data.Photo
import com.example.checklistdigital.data.VehicleInfo
import com.example.checklistdigital.data.VehicleStatus1
import com.example.checklistdigital.data.VehicleStatus2
import com.itextpdf.io.image.ImageData
import com.itextpdf.io.image.ImageDataFactory
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Image
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Table
import com.itextpdf.layout.properties.TextAlignment
import com.itextpdf.layout.properties.UnitValue
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object PdfExportUtils {

    fun generateChecklistPdf(
        context: Context,
        client: Client,
        vehicleInfo: VehicleInfo,
        address: Address,
        vehicleStatus1: VehicleStatus1,
        vehicleStatus2: VehicleStatus2,
        photos: List<Photo>
    ): String {
        val fileName = "Checklist_${client.id}_${System.currentTimeMillis()}.pdf"
        val file = File(context.getExternalFilesDir(null), fileName)

        val writer = PdfWriter(file)
        val pdfDoc = PdfDocument(writer)
        val document = Document(pdfDoc)

        // Logo + Título
        val titleTable = Table(UnitValue.createPercentArray(floatArrayOf(1f, 4f)))
        titleTable.setWidth(UnitValue.createPercentValue(100f))

        // Adiciona logo na primeira célula

        // Carrega logo do drawable
        val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.logo_apg)
        val logoBytes = java.io.ByteArrayOutputStream().apply {
            bitmap.compress(android.graphics.Bitmap.CompressFormat.PNG, 100, this)
        }.toByteArray()

        val imageData: ImageData = ImageDataFactory.create(logoBytes)
        val logo = Image(imageData)
        logo.setAutoScale(true) // ajusta automaticamente
        logo.setHeight(40f)     // altura aproximada do texto
        titleTable.addCell(logo.setTextAlignment(TextAlignment.LEFT))


        // Adiciona título e informações da empresa na segunda célula
        val titleCell = com.itextpdf.layout.element.Cell()
        titleCell.add(
            Paragraph("Arraial Porto Guincho")
                .setFontSize(20f)
                .setBold()
                .setTextAlignment(TextAlignment.CENTER)
        )
        titleCell.add(
            Paragraph("Telefones: (73)9 9902-1212 / (73)9 8873-0892")
                .setFontSize(12f)
                .setItalic()
                .setTextAlignment(TextAlignment.CENTER)
        )
        titleCell.add(
            Paragraph("Email: arraialportoguincho24h@hotmail.com")
                .setFontSize(12f)
                .setItalic()
                .setTextAlignment(TextAlignment.CENTER)
        )
        titleCell.add(
            Paragraph("CNPJ: 17.136.660/0001-05")
                .setFontSize(12f)
                .setItalic()
                .setTextAlignment(TextAlignment.CENTER)
        )
        titleCell.add(
            Paragraph("Rua 26, Quadra 53, Lote 06, Porto Alegre 2, Porto Seguro - BA")
                .setFontSize(14f)
                .setItalic()
                .setTextAlignment(TextAlignment.CENTER)
        )

        titleTable.addCell(titleCell)

        document.add(titleTable)
        document.add(Paragraph(""))

        // Client Information
        document.add(
            Paragraph("CLIENTE").setBold()
        )

        val clientTable = Table(UnitValue.createPercentArray(floatArrayOf(1f, 2f, 1f, 2f)))
        clientTable.setWidth(UnitValue.createPercentValue(100f))

        addTableDoubleCell(clientTable, "Nome", client.clientName, "Seguradora", client.insurance)
        addTableDoubleCell(clientTable, "Data", client.serviceDate, "Sinistro", client.accident)
        addTableDoubleCell(clientTable, "Telefone", client.phone, "", "")

        document.add(clientTable)
        document.add(Paragraph(""))

        // Vehicle Information
        document.add(
            Paragraph("VEÍCULO").setBold()
        )

        val vehicleTable = Table(UnitValue.createPercentArray(floatArrayOf(1f, 2f, 1f, 2f)))
        vehicleTable.setWidth(UnitValue.createPercentValue(100f))

        addTableDoubleCell(vehicleTable, "Modelo", vehicleInfo.vehicle,  "Placa", vehicleInfo.plate)
        addTableDoubleCell(vehicleTable, "Cor", vehicleInfo.color, "Ano", vehicleInfo.year)

        document.add(vehicleTable)
        document.add(Paragraph(""))

        // Address Information

        document.add(
            Paragraph("ORIGEM").setBold()
        )
        val originTable = Table(UnitValue.createPercentArray(floatArrayOf(1f, 2f, 1f, 2f)))
        originTable.setWidth(UnitValue.createPercentValue(100f))
        addTableDoubleCell(originTable, "Rua/Avenida", address.originStreet, "Número", address.originNumber)
        addTableDoubleCell(originTable, "Bairro", address.originDistrict, "Cidade", address.originCity)
        document.add(originTable)

        document.add(Paragraph(""))

        document.add(
            Paragraph("DESTINO").setBold()
        )
        val destinyTable = Table(UnitValue.createPercentArray(floatArrayOf(1f, 2f, 1f, 2f)))
        destinyTable.setWidth(UnitValue.createPercentValue(100f))
        addTableDoubleCell(destinyTable, "Rua/Avenida", address.destinyStreet, "Número", address.destinyNumber)
        addTableDoubleCell(destinyTable, "Bairro", address.destinyDistrict, "Cidade", address.destinyCity)
        document.add(destinyTable)

        document.add(Paragraph(""))

        // Vehicle Status 1
        document.add(
            Paragraph("STATUS").setBold()
        )

        val status1Table = Table(UnitValue.createPercentArray(floatArrayOf(2f, 1f, 2f, 1f)))
        status1Table.setWidth(UnitValue.createPercentValue(100f))

        addStatusTableCell(status1Table, "Documentos", vehicleStatus1.documentos, "Extintor", vehicleStatus1.extintor)
        addStatusTableCell(status1Table, "Livreto", vehicleStatus1.livreto, "Tapetes", vehicleStatus1.tapetes)
        addStatusTableCell(status1Table, "Rádio", vehicleStatus1.radio, "Estepe", vehicleStatus1.estepe)
        addStatusTableCell(status1Table, "CD Player", vehicleStatus1.cdPlayer, "Acendedor de Cigarro", vehicleStatus1.acededorDeCigarro)
        addStatusTableCell(status1Table, "DVD Player", vehicleStatus1.dvdPlayer, "Macaco", vehicleStatus1.macaco)
        addStatusTableCell(status1Table, "Módulo/Amplificador", vehicleStatus1.moduloAmplificador, "Chave de Roda", vehicleStatus1.chaveDeRoda)
        addStatusTableCell(status1Table, "Frente CD", vehicleStatus1.frenteCD, "Triângulo", vehicleStatus1.triangulo)
        addStatusTableCell(status1Table, "Antena", vehicleStatus1.antena, "Bateria", vehicleStatus1.bateria)
        addStatusTableCell(status1Table, "Roda Liga Leve", vehicleStatus1.rodaLigaLeve, "Pintura Suja Dif. Vistoria", vehicleStatus1.pintSujaDif)


        document.add(status1Table)
        document.add(Paragraph(""))

        // Vehicle Status 2

        val status2Table = Table(UnitValue.createPercentArray(floatArrayOf(2f, 1f, 2f, 1f)))
        status2Table.setWidth(UnitValue.createPercentValue(100f))

        val pneusDianteiros = when (vehicleStatus2.pneusDianteiros) {
            0 -> "Novos"
            1 -> "Bons"
            else -> "Ruins"
        }
        val pneusTraseiros = when (vehicleStatus2.pneusTraseiros) {
            0 -> "Novos"
            1 -> "Bons"
            else -> "Ruins"
        }
        val estepe = when (vehicleStatus2.estepe) {
            0 -> "Novos"
            1 -> "Bons"
            else -> "Ruins"
        }

        addTableDoubleCell(status2Table, "Pneus Dianteiros", pneusDianteiros, "Pneus Traseiros", pneusTraseiros)
        addTableDoubleCell(status2Table, "Estepe", estepe, "Nível de Combustível", "${(vehicleStatus2.nivelCombustivel * 100).toInt()}%")

        document.add(status2Table)
        document.add(Paragraph(""))

        // Observations
        if (vehicleStatus2.observacoes.isNotEmpty()) {
            document.add(
                Paragraph("OBSERVAÇÕES")
                    .setFontSize(14f)
                    .setBold()
            )
            document.add(Paragraph(vehicleStatus2.observacoes))
            document.add(Paragraph(""))
        }

        // Photos
        if (photos.isNotEmpty()) {
            document.add(
                Paragraph("FOTOS")
                    .setFontSize(14f)
                    .setBold()
            )

            photos.forEach { photo ->
                try {
                    val photoFile = File(photo.photoPath)
                    if (photoFile.exists()) {
                        // Load and rotate image based on EXIF orientation
                        val rotatedBitmap = rotateImageIfNeeded(photo.photoPath)

                        val rotatedImageBytes = java.io.ByteArrayOutputStream().apply {
                            rotatedBitmap.compress(android.graphics.Bitmap.CompressFormat.JPEG, 95, this)
                        }.toByteArray()

                        val imageData: ImageData = ImageDataFactory.create(rotatedImageBytes)
                        val image = Image(imageData)
                        image.setWidth(UnitValue.createPercentValue(100f))
                        document.add(image)
                        document.add(Paragraph(""))
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        // Footer
        document.add(Paragraph(""))
        document.add(
            Paragraph("Gerado em: ${SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale("pt", "BR")).format(Date())}")
                .setFontSize(10f)
                .setTextAlignment(TextAlignment.CENTER)
        )

        document.close()
        return file.absolutePath
    }

    /**
     * Loads an image and rotates it based on EXIF orientation data
     */
    private fun rotateImageIfNeeded(imagePath: String): android.graphics.Bitmap {
        val bitmap = BitmapFactory.decodeFile(imagePath)

        return try {
            val exif = ExifInterface(imagePath)
            val orientation = exif.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL
            )

            val rotationDegrees = when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> 90
                ExifInterface.ORIENTATION_ROTATE_180 -> 180
                ExifInterface.ORIENTATION_ROTATE_270 -> 270
                else -> 0
            }

            if (rotationDegrees == 0) {
                bitmap
            } else {
                val matrix = Matrix()
                matrix.postRotate(rotationDegrees.toFloat())
                android.graphics.Bitmap.createBitmap(
                    bitmap,
                    0,
                    0,
                    bitmap.width,
                    bitmap.height,
                    matrix,
                    true
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            bitmap
        }
    }

    private fun addTableCell(table: Table, label: String, value: String) {
        table.addCell(Paragraph(label).setBold())
        table.addCell(Paragraph(value))
    }

    private fun addTableDoubleCell(table: Table, label1: String, value1: String, label2: String, value2: String) {
        table.addCell(Paragraph(label1).setBold())
        table.addCell(Paragraph(value1))

        table.addCell(Paragraph(label2).setBold())
        table.addCell(Paragraph(value2))
    }

    private fun addStatusTableCell(table: Table, label1: String, status1: Boolean, label2: String, status2: Boolean) {

        table.addCell(Paragraph(label1).setBold())
        table.addCell(Paragraph(if (status1) "X" else " ")
            .setFontSize(14f)
            .setTextAlignment(TextAlignment.CENTER)
        )

        table.addCell(Paragraph(label2).setBold())
        table.addCell(Paragraph(if (status2) "X" else " ")
            .setFontSize(14f)
            .setTextAlignment(TextAlignment.CENTER)
        )
    }
}