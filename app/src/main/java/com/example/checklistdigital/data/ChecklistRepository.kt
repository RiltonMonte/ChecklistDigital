package com.example.checklistdigital.data

import kotlinx.coroutines.flow.Flow

interface ChecklistRepository {
    // Inserts
    suspend fun insertClient(client: Client): Long
    suspend fun insertVehicleInfo(vehicleInfo: VehicleInfo)
    suspend fun insertAddress(address: Address)
    suspend fun insertVehicleStatus1(vehicleStatus1: VehicleStatus1)
    suspend fun insertVehicleStatus2(vehicleStatus2: VehicleStatus2)

    // Updates
    suspend fun updateClient(client: Client)
    suspend fun updateVehicleInfo(vehicleInfo: VehicleInfo)
    suspend fun updateAddress(address: Address)
    suspend fun updateVehicleStatus1(vehicleStatus1: VehicleStatus1)
    suspend fun updateVehicleStatus2(vehicleStatus2: VehicleStatus2)

    // Deletes
    suspend fun deleteClient(client: Client)
    suspend fun deleteVehicleInfo(vehicleInfo: VehicleInfo)
    suspend fun deleteAddress(address: Address)
    suspend fun deleteVehicleStatus1(vehicleStatus1: VehicleStatus1)
    suspend fun deleteVehicleStatus2(vehicleStatus2: VehicleStatus2)

    // Queries
    fun getChecklist(): Flow<List<Client>>
    fun getClient(id: Int): Flow<Client>
    fun getVehicleInfoByClientId(clientId: Int): Flow<VehicleInfo>
    fun getVehicleInfo(id: Int): Flow<VehicleInfo>
    fun getAddress(id: Int): Flow<Address>
    fun getVehicleStatus1(id: Int): Flow<VehicleStatus1>
    fun getVehicleStatus2(id: Int): Flow<VehicleStatus2>

}