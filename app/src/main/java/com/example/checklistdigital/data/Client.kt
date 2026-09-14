package com.example.checklistdigital.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entidade que representa um cliente dentro do sistema.
 * Cada cliente possui informações básicas de identificação,
 * dados de contato e detalhes relacionados ao serviço.
 *
 * A tabela é chamada "client" e serve como base para relacionamentos
 * com outras entidades, como endereços e checklists.
 */
@Entity (tableName = "client")
data class Client (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0, // Identificador único do cliente (gerado automaticamente)
    val serviceDate: String, // Data do serviço associado ao cliente
    val clientName: String, // Nome do cliente
    val insurance: String, // Nome da seguradora vinculada ao cliente
    val accident: String, // Informações sobre o acidente relacionado
    val phone: String, // Telefone de contato do cliente
)