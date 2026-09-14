package com.example.checklistdigital.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

/**
 * Entidade que representa uma foto vinculada a um cliente.
 * Cada registro contém o caminho da foto e o momento em que foi capturada.
 *
 * A tabela é chamada "photo" e está relacionada à tabela "Client".
 * Caso o cliente seja removido, todas as fotos associadas também serão excluídas (CASCADE).
 */
@Entity(
    tableName = "photo",
    foreignKeys = [
        ForeignKey(
            entity = Client::class,
            parentColumns = ["id"], // Coluna da tabela Client usada como referência
            childColumns = ["clientId"], // Coluna desta tabela que referencia o cliente
            onDelete = ForeignKey.CASCADE // Exclusão em cascata
        )
    ]
)
data class Photo(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0, // Identificador único da foto (gerado automaticamente)
    val clientId: Int, // ID do cliente ao qual a foto está vinculada
    val photoPath: String, // Caminho do arquivo da foto armazenada no dispositivo
    val timestamp: Long = System.currentTimeMillis() // Momento em que a foto foi registrada
)