package com.example.checklistdigital.data

import kotlinx.coroutines.flow.Flow

class OfflineChecklistRepository(private val checklistDao: ChecklistDao) : ChecklistRepository {

    override suspend fun insertClient(client: Client) = checklistDao.insertClient(client)
    override suspend fun insertVehicleInfo(vehicleInfo: VehicleInfo) = checklistDao.insertVehicleInfo(vehicleInfo)
    override suspend fun insertAddress(address: Address) = checklistDao.insertAddress(address)
    override suspend fun insertVehicleStatus1(vehicleStatus1: VehicleStatus1) = checklistDao.insertVehicleStatus1(vehicleStatus1)
    override suspend fun insertVehicleStatus2(vehicleStatus2: VehicleStatus2) = checklistDao.insertVehicleStatus2(vehicleStatus2)

    override suspend fun updateClient(client: Client) = checklistDao.updateClient(client)
    override suspend fun updateVehicleInfo(vehicleInfo: VehicleInfo) = checklistDao.updateVehicleInfo(vehicleInfo)
    override suspend fun updateAddress(address: Address) = checklistDao.updateAddress(address)
    override suspend fun updateVehicleStatus1(vehicleStatus1: VehicleStatus1) = checklistDao.updateVehicleStatus1(vehicleStatus1)
    override suspend fun updateVehicleStatus2(vehicleStatus2: VehicleStatus2) = checklistDao.updateVehicleStatus2(vehicleStatus2)

    override suspend fun deleteClient(client: Client) = checklistDao.deleteClient(client)
    override suspend fun deleteVehicleInfo(vehicleInfo: VehicleInfo) = checklistDao.deleteVehicleInfo(vehicleInfo)
    override suspend fun deleteAddress(address: Address) = checklistDao.deleteAddress(address)
    override suspend fun deleteVehicleStatus1(vehicleStatus1: VehicleStatus1) = checklistDao.deleteVehicleStatus1(vehicleStatus1)
    override suspend fun deleteVehicleStatus2(vehicleStatus2: VehicleStatus2) = checklistDao.deleteVehicleStatus2(vehicleStatus2)

//    override fun getChecklist(id: Int): Flow<Client> = checklistDao.getChecklist(id)
    override fun getClient(id: Int): Flow<Client> = checklistDao.getClient(id)
    override fun getVehicleInfo(id: Int): Flow<VehicleInfo> = checklistDao.getVehicleInfo(id)
    override fun getAddress(id: Int): Flow<Address> = checklistDao.getAddress(id)
    override fun getVehicleStatus1(id: Int): Flow<VehicleStatus1> = checklistDao.getVehicleStatus1(id)
    override fun getVehicleStatus2(id: Int): Flow<VehicleStatus2> = checklistDao.getVehicleStatus2(id)

}