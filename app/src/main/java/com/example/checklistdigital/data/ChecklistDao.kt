package com.example.checklistdigital.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ChecklistDao {
    //Inserts
    @Insert
    suspend fun insertClient(client: Client): Long

    @Insert
    suspend fun insertVehicleInfo(vehicleInfo: VehicleInfo)

    @Insert
    suspend fun insertAddress(address: Address)

    @Insert
    suspend fun insertVehicleStatus1(vehicleStatus1: VehicleStatus1)

    @Insert
    suspend fun insertVehicleStatus2(vehicleStatus2: VehicleStatus2)

    //Updates
    @Update
    suspend fun updateClient(client: Client)

    @Update
    suspend fun updateVehicleInfo(vehicleInfo: VehicleInfo)

    @Update
    suspend fun updateAddress(address: Address)

    @Update
    suspend fun updateVehicleStatus1(vehicleStatus1: VehicleStatus1)

    @Update
    suspend fun updateVehicleStatus2(vehicleStatus2: VehicleStatus2)

    //Deletes
    @Delete
    suspend fun deleteClient(client: Client)

    @Delete
    suspend fun deleteVehicleInfo(vehicleInfo: VehicleInfo)

    @Delete
    suspend fun deleteAddress(address: Address)

    @Delete
    suspend fun deleteVehicleStatus1(vehicleStatus1: VehicleStatus1)

    @Delete
    suspend fun deleteVehicleStatus2(vehicleStatus2: VehicleStatus2)

    //Queries
    //Chama todos os Checklists
    @Query("SELECT * FROM client")
    fun getChecklist(): Flow<List<Client>>

    @Query("SELECT * FROM client WHERE id = :id")
    fun getClient(id: Int): Flow<Client>

    @Query("SELECT * FROM vehicleInfo WHERE id = :id")
    fun getVehicleInfo(id: Int): Flow<VehicleInfo>

    @Query("SELECT * FROM address WHERE id = :id")
    fun getAddress(id: Int): Flow<Address>

    @Query("SELECT * FROM vehicleStatus1 WHERE id = :id")
    fun getVehicleStatus1(id: Int): Flow<VehicleStatus1>

    @Query("SELECT * FROM vehicleStatus2 WHERE id = :id")
    fun getVehicleStatus2(id: Int): Flow<VehicleStatus2>

    @Query("SELECT * FROM vehicleInfo WHERE clientId = :clientId")
    fun getVehicleInfoByClientId(clientId: Int): Flow<VehicleInfo>

}