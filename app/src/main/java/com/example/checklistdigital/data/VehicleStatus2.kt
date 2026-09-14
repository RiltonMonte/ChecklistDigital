package com.example.checklistdigital.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

/**
 * Entidade que representa o estado dos pneus e outras condições gerais
 * do veículo vinculadas a um cliente. Permite registrar medidas de desgaste,
 * nível de combustível e observações adicionais.
 *
 * A tabela é chamada "vehicleStatus2" e está relacionada à tabela "Client".
 * Caso o cliente seja removido, os registros de status também serão excluídos (CASCADE).
 */
@Entity(
    tableName = "vehicleStatus2",
    foreignKeys = [
        ForeignKey(
            entity = Client::class,
            parentColumns = ["id"], // Coluna da tabela Client usada como referência
            childColumns = ["clientId"], // Coluna desta tabela que referencia o cliente
            onDelete = ForeignKey.CASCADE // Exclusão em cascata
        )
    ]
)
data class VehicleStatus2 (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0, // Identificador único do registro (gerado automaticamente)
    val clientId: Int, // ID do cliente ao qual o status do veículo está vinculado
    val pneusDianteiros: Int, // Estado dos pneus dianteiros (ex.: nível de desgaste)
    val pneusTraseiros: Int, // Estado dos pneus traseiros
    val estepe: Int, // Estado do pneu estepe
    val nivelCombustivel: Float, // Nível de combustível (em porcentagem ou litros)
    val observacoes: String, // Observações adicionais sobre o veículo
)