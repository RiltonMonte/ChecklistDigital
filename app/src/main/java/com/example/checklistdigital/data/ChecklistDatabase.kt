package com.example.checklistdigital.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

//@Database(
//    entities = {
//        Users.class,
//        Passwords.class
//    },
//    version = VERSION
//)

@Database(entities = [Client::class, VehicleInfo::class, Address::class, VehicleStatus1::class, VehicleStatus2::class], version = 2, exportSchema = false)
abstract class ChecklistDatabase : RoomDatabase() {
    abstract fun checklistDao(): ChecklistDao

    companion object{

        private var Instance: ChecklistDatabase? = null

        fun getDatabase(context: Context): ChecklistDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, ChecklistDatabase::class.java, "checklist_database")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }

    }
}