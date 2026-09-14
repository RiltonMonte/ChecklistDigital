package com.example.checklistdigital

import android.app.Application
import com.example.checklistdigital.data.AppContainer
import com.example.checklistdigital.data.AppDataContainer

/**
 * Classe principal da aplicação Android.
 * Responsável por inicializar o container de dependências
 * que fornece acesso ao [ChecklistRepository] e outros
 * componentes necessários para os ViewModels.
 */
class ChecklistApplication : Application() {

    /**
     * Container de dependências da aplicação.
     * É inicializado no método [onCreate] e disponibilizado
     * para toda a aplicação.
     */
    lateinit var container: AppContainer

    /**
     * Método chamado quando a aplicação é criada.
     * Inicializa o [AppDataContainer], que implementa [AppContainer],
     * e fornece acesso ao repositório e demais dependências.
     */
    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}