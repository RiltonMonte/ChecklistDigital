package com.example.checklistdigital.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) responsável por definir as operações
 * de acesso ao banco de dados relacionadas ao checklist.
 *
 * Contém métodos para inserção, atualização, exclusão e consulta
 * das entidades principais do sistema.
 */
@Dao
interface ChecklistDao {
    // -----------------------------
    // Inserções
    // -----------------------------
    @Insert
    suspend fun insertClient(client: Client): Long // Insere um novo cliente e retorna o ID gerado

    @Insert
    suspend fun insertVehicleInfo(vehicleInfo: VehicleInfo) // Insere informações de veículo

    @Insert
    suspend fun insertAddress(address: Address) // Insere endereço vinculado ao cliente

    @Insert
    suspend fun insertVehicleStatus1(vehicleStatus1: VehicleStatus1) // Insere status de itens do veículo

    @Insert
    suspend fun insertVehicleStatus2(vehicleStatus2: VehicleStatus2) // Insere status de pneus e combustível


    @Insert
    suspend fun insertPhoto(photo: Photo): Long // Insere foto vinculada ao cliente e retorna o ID gerado


    // -----------------------------
    // Atualizações
    // -----------------------------
    @Update
    suspend fun updateClient(client: Client) // Atualiza dados de cliente

    @Update
    suspend fun updateVehicleInfo(vehicleInfo: VehicleInfo) // Atualiza informações de veículo

    @Update
    suspend fun updateAddress(address: Address) // Atualiza endereço

    @Update
    suspend fun updateVehicleStatus1(vehicleStatus1: VehicleStatus1) // Atualiza status de itens do veículo

    @Update
    suspend fun updateVehicleStatus2(vehicleStatus2: VehicleStatus2) // Atualiza status de pneus e combustível


    // -----------------------------
    // Exclusões
    // -----------------------------
    @Delete
    suspend fun deleteClient(client: Client) // Remove cliente

    @Delete
    suspend fun deleteVehicleInfo(vehicleInfo: VehicleInfo) // Remove informações de veículo

    @Delete
    suspend fun deleteAddress(address: Address) // Remove endereço

    @Delete
    suspend fun deleteVehicleStatus1(vehicleStatus1: VehicleStatus1) // Remove status de itens do veículo

    @Delete
    suspend fun deleteVehicleStatus2(vehicleStatus2: VehicleStatus2) // Remove status de pneus e combustível

    @Delete
    suspend fun deletePhoto(photo: Photo) // Remove foto

    // -----------------------------
    // Consultas
    // -----------------------------
    @Query("SELECT * FROM client")
    fun getChecklist(): Flow<List<Client>>
    // Retorna todos os clientes (checklists)

    @Query("SELECT * FROM client WHERE id = :id")
    fun getClient(id: Int): Flow<Client>
    // Retorna cliente específico pelo ID

    @Query("SELECT * FROM vehicleInfo WHERE id = :id")
    fun getVehicleInfo(id: Int): Flow<VehicleInfo>
    // Retorna informações de veículo pelo ID

    @Query("SELECT * FROM address WHERE clientId = :clientId")
    fun getAddress(clientId: Int): Flow<Address>
    // Retorna endereço vinculado a um cliente

    @Query("SELECT * FROM vehicleStatus1 WHERE clientId = :clientId")
    fun getVehicleStatus1(clientId: Int): Flow<VehicleStatus1>
    // Retorna status de itens do veículo de um cliente

    @Query("SELECT * FROM vehicleStatus2 WHERE clientId = :clientId")
    fun getVehicleStatus2(clientId: Int): Flow<VehicleStatus2>
    // Retorna status de pneus/combustível de um cliente

    @Query("SELECT * FROM vehicleInfo WHERE clientId = :clientId")
    fun getVehicleInfoByClientId(clientId: Int): Flow<VehicleInfo>
    // Retorna informações de veículo vinculadas a um cliente

    @Query("SELECT * FROM photo WHERE clientId = :clientId ORDER BY timestamp DESC")
    fun getPhotosByClientId(clientId: Int): Flow<List<Photo>>
    // Retorna todas as fotos de um cliente, ordenadas pela data (mais recentes primeiro)

    @Query("SELECT * FROM photo WHERE id = :photoId")
    fun getPhoto(photoId: Int): Flow<Photo>
    // Retorna uma foto específica pelo ID

}