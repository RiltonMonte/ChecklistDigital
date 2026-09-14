package com.example.checklistdigital.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

/**
 * Entidade que representa o estado e a presença de itens do veículo
 * vinculados a um cliente. Cada campo indica se o item está presente
 * ou em conformidade (true) ou ausente/incorreto (false).
 *
 * A tabela é chamada "vehicleStatus1" e está relacionada à tabela "Client".
 * Caso o cliente seja removido, os registros de status do veículo também
 * serão excluídos (CASCADE).
 */
@Entity(
    tableName = "vehicleStatus1",
    foreignKeys = [
        ForeignKey(
            entity = Client::class,
            parentColumns = ["id"], // Coluna da tabela Client usada como referência
            childColumns = ["clientId"], // Coluna desta tabela que referencia o cliente
            onDelete = ForeignKey.CASCADE // Exclusão em cascata
        )
    ]
)
data class VehicleStatus1 (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0, // Identificador único do registro (gerado automaticamente)
    val clientId: Int, // ID do cliente ao qual o status do veículo está vinculado
    val documentos: Boolean, // Documentos do veículo
    val extintor: Boolean, // Extintor de incêndio
    val livreto: Boolean, // Manual/livreto do veículo
    val tapetes: Boolean, // Tapetes internos
    val radio: Boolean, // Rádio instalado
    val estepe: Boolean, // Estepe presente
    val cdPlayer: Boolean, // CD Player
    val acededorDeCigarro: Boolean, // Acendedor de cigarro
    val dvdPlayer: Boolean, // DVD Player
    val macaco: Boolean, // Macaco para troca de pneus
    val moduloAmplificador: Boolean, // Módulo amplificador de som
    val chaveDeRoda: Boolean, // Chave de roda
    val frenteCD: Boolean, // Frente destacável do CD Player
    val triangulo: Boolean, // Triângulo de sinalização
    val antena: Boolean, // Antena do veículo
    val bateria: Boolean, // Bateria
    val rodaLigaLeve: Boolean, // Rodas de liga leve
    val pintSujaDif: Boolean, // Pintura suja dificulta vistoria
)