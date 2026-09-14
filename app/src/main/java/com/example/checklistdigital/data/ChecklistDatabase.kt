package com.example.checklistdigital.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Classe que representa o banco de dados principal da aplicação,
 * configurado com Room. Define todas as entidades utilizadas e
 * fornece acesso ao DAO [ChecklistDao].
 *
 * @Database:
 * - entities: Lista de todas as tabelas do banco (Client, VehicleInfo, Address, VehicleStatus1, VehicleStatus2, Photo).
 * - version: Versão atual do banco de dados (3).
 * - exportSchema: false (não exporta o esquema para arquivos).
 */
@Database(entities = [Client::class, VehicleInfo::class, Address::class, VehicleStatus1::class, VehicleStatus2::class, Photo::class], version = 3, exportSchema = false)
abstract class ChecklistDatabase : RoomDatabase() {

    /**
     * Fornece acesso ao DAO responsável pelas operações
     * de inserção, atualização, exclusão e consulta.
     */
    abstract fun checklistDao(): ChecklistDao

    companion object{

        // Instância única do banco de dados (Singleton)
        private var Instance: ChecklistDatabase? = null

        /**
         * Retorna a instância do banco de dados.
         * Caso ainda não exista, cria uma nova utilizando Room.
         *
         * @param context Contexto da aplicação, necessário para inicializar o banco.
         * @return Instância única de [ChecklistDatabase].
         */
        fun getDatabase(context: Context): ChecklistDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context,
                    ChecklistDatabase::class.java,
                    "checklist_database" // Nome do arquivo do banco
                    )
                    .fallbackToDestructiveMigration() // Apaga e recria o banco em caso de incompatibilidade de versão
                    .build()
                    .also { Instance = it }
            }
        }

    }
}