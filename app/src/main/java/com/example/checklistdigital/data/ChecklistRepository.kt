package com.example.checklistdigital.data

import kotlinx.coroutines.flow.Flow

/**
 * Interface que define o contrato para o repositório de checklist.
 * Contém as operações essenciais de inserção, atualização, exclusão
 * e consulta das entidades principais do sistema.
 *
 * Essa interface permite abstrair a implementação concreta
 * (ex.: [OfflineChecklistRepository]), garantindo flexibilidade
 * e testabilidade na aplicação.
 */
interface ChecklistRepository {

    // -----------------------------
    // Inserções
    // -----------------------------
    suspend fun insertClient(client: Client): Long // Insere um novo cliente e retorna o ID gerado
    suspend fun insertVehicleInfo(vehicleInfo: VehicleInfo) // Insere informações de veículo
    suspend fun insertAddress(address: Address) // Insere endereço vinculado ao cliente
    suspend fun insertVehicleStatus1(vehicleStatus1: VehicleStatus1) // Insere status de itens do veículo
    suspend fun insertVehicleStatus2(vehicleStatus2: VehicleStatus2) // Insere status de pneus/combustível
    suspend fun insertPhoto(photo: Photo): Long // Insere foto vinculada ao cliente e retorna o ID gerado

    // -----------------------------
    // Atualizações
    // -----------------------------
    suspend fun updateClient(client: Client) // Atualiza dados de cliente
    suspend fun updateVehicleInfo(vehicleInfo: VehicleInfo) // Atualiza informações de veículo
    suspend fun updateAddress(address: Address) // Atualiza endereço
    suspend fun updateVehicleStatus1(vehicleStatus1: VehicleStatus1) // Atualiza status de itens do veículo
    suspend fun updateVehicleStatus2(vehicleStatus2: VehicleStatus2) // Atualiza status de pneus/combustível

    // -----------------------------
    // Exclusões
    // -----------------------------
    suspend fun deleteClient(client: Client) // Remove cliente
    suspend fun deleteVehicleInfo(vehicleInfo: VehicleInfo) // Remove informações de veículo
    suspend fun deleteAddress(address: Address) // Remove endereço
    suspend fun deleteVehicleStatus1(vehicleStatus1: VehicleStatus1) // Remove status de itens do veículo
    suspend fun deleteVehicleStatus2(vehicleStatus2: VehicleStatus2) // Remove status de pneus/combustível
    suspend fun deletePhoto(photo: Photo) // Remove foto

    // -----------------------------
    // Consultas
    // -----------------------------
    fun getChecklist(): Flow<List<Client>> // Retorna todos os clientes (checklists)
    fun getClient(id: Int): Flow<Client> // Retorna cliente específico pelo ID
    fun getVehicleInfoByClientId(clientId: Int): Flow<VehicleInfo> // Retorna informações de veículo de um cliente
    fun getVehicleInfo(id: Int): Flow<VehicleInfo> // Retorna informações de veículo pelo ID
    fun getAddress(id: Int): Flow<Address> // Retorna endereço de um cliente
    fun getVehicleStatus1(id: Int): Flow<VehicleStatus1> // Retorna status de itens do veículo
    fun getVehicleStatus2(id: Int): Flow<VehicleStatus2> // Retorna status de pneus/combustível
    fun getPhotosByClientId(clientId: Int): Flow<List<Photo>> // Retorna fotos de um cliente
    fun getPhoto(photoId: Int): Flow<Photo> // Retorna foto específica pelo ID

}