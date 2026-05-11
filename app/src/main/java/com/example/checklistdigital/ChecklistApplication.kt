package com.example.checklistdigital

import android.app.Application
import com.example.checklistdigital.data.AppContainer
import com.example.checklistdigital.data.AppDataContainer

class ChecklistApplication : Application() {

    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}