package com.example.checklistdigital.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

/**
 * Entidade que representa os endereços vinculados a um cliente.
 * Cada registro contém informações de origem e destino,
 * sendo associado a um cliente específico através da chave estrangeira.
 *
 * A tabela é chamada "Address" e está relacionada à tabela "Client".
 * Caso o cliente seja removido, os endereços associados também serão excluídos (CASCADE).
 */
@Entity(
    tableName = "Address",
    foreignKeys = [
        ForeignKey(
            entity = Client::class,
            parentColumns = ["id"], // Coluna da tabela Client usada como referência
            childColumns = ["clientId"], // Coluna desta tabela que referencia o cliente
            onDelete = ForeignKey.CASCADE // Exclusão em cascata
        )
    ]
)
data class Address (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0, // Identificador único do endereço (gerado automaticamente)
    val clientId: Int, // ID do cliente ao qual o endereço está vinculado
    // Campos referentes ao endereço de origem
    val originStreet: String, // Rua de origem
    val originNumber: String, // Número da residência/empresa de origem
    val originDistrict: String, // Bairro de origem
    val originCity: String, // Cidade de origem

    // Campos referentes ao endereço de destino
    val destinyStreet: String, // Rua de destino
    val destinyNumber: String, // Número da residência/empresa de destino
    val destinyDistrict: String, // Bairro de destino
    val destinyCity: String, // Cidade de destino
)