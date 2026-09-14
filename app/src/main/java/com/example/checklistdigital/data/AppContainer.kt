package com.example.checklistdigital.data

import android.content.Context

/**
 * Interface que define o contêiner de dependências do aplicativo.
 * Fornece acesso ao repositório principal de checklist.
 */
interface AppContainer {
    val checklistRepository: ChecklistRepository
}

/**
 * Implementação concreta de [AppContainer].
 * Responsável por inicializar e fornecer instâncias dos repositórios
 * e demais dependências necessárias para o funcionamento do app.
 *
 * @param context Contexto da aplicação, usado para inicializar o banco de dados Room.
 */
class AppDataContainer(private val context: Context) : AppContainer {

    /**
     * Repositório de checklist utilizado pelo aplicativo.
     * É inicializado de forma preguiçosa (lazy), garantindo que só será criado
     * quando realmente necessário. Utiliza o banco de dados local (Room).
     */
    override val checklistRepository: ChecklistRepository by lazy {
        OfflineChecklistRepository(ChecklistDatabase.getDatabase(context).checklistDao())
    }
}