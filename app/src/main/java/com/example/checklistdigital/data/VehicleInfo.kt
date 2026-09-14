package com.example.checklistdigital.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey


/**
 * Entidade que representa informações do veículo vinculadas a um cliente.
 * Cada registro contém dados básicos de identificação do veículo.
 *
 * A tabela é chamada "vehicleInfo" e está relacionada à tabela "Client".
 * Caso o cliente seja removido, os veículos associados também serão excluídos (CASCADE).
 */
@Entity(
    tableName = "vehicleInfo",
    foreignKeys = [
        ForeignKey(
            entity = Client::class,
            parentColumns = ["id"], // Coluna da tabela Client usada como referência
            childColumns = ["clientId"], // Coluna desta tabela que referencia o cliente
            onDelete = ForeignKey.CASCADE // Exclusão em cascata
        )
    ]
)
data class VehicleInfo (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0, // Identificador único do veículo (gerado automaticamente)
    val clientId: Int, // ID do cliente ao qual o veículo está vinculado
    val vehicle: String, // Modelo ou tipo do veículo
    val plate: String, // Placa de identificação do veículo
    val color: String, // Cor do veículo
    val year: String, // Ano de fabricação ou modelo do veículo
)

