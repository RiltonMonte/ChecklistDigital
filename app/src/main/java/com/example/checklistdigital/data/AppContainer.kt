package com.example.checklistdigital.data

import android.content.Context


interface AppContainer {
    val checklistRepository: ChecklistRepository
}

class AppDataContainer(private val context: Context) : AppContainer {

    override val checklistRepository: ChecklistRepository by lazy {
        OfflineChecklistRepository(ChecklistDatabase.getDatabase(context).checklistDao())
    }
}