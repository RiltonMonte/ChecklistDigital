package com.example.checklistdigital.viewmodel

import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import com.example.checklistdigital.ChecklistApplication

/**
 * Objeto responsável por fornecer instâncias dos ViewModels
 * utilizados na aplicação. Usa o padrão de fábrica (Factory)
 * para inicializar cada ViewModel com o [ChecklistRepository].
 *
 * Isso garante que todos os ViewModels compartilhem a mesma
 * instância de repositório, mantendo consistência nos dados.
 */
object ChecklistViewModelProvider {

    /**
     * Fábrica de ViewModels que inicializa cada um com o
     * [ChecklistRepository] obtido do container da aplicação.
     *
     * Inclui:
     * - [ChecklistHomeViewModel] → Tela inicial de checklists
     * - [ClientViewModel] → Dados do cliente
     * - [VehicleInfoViewModel] → Informações do veículo
     * - [AddressViewModel] → Endereços de origem/destino
     * - [VehicleStatus1ViewModel] → Status de itens do veículo
     * - [VehicleStatus2ViewModel] → Status de pneus/combustível
     * - [PhotoViewModel] → Fotos vinculadas ao checklist
     * - [PdfExportViewModel] → Exportação de checklist em PDF
     */
    val Factory = viewModelFactory {
        initializer {
            ChecklistHomeViewModel(checklistApplication().container.checklistRepository)
        }
        initializer {
            ClientViewModel(checklistApplication().container.checklistRepository)
        }
        initializer {
            VehicleInfoViewModel(checklistApplication().container.checklistRepository)
        }
        initializer {
            AddressViewModel(checklistApplication().container.checklistRepository)
        }
        initializer {
            VehicleStatus1ViewModel(checklistApplication().container.checklistRepository)
        }
        initializer {
            VehicleStatus2ViewModel(checklistApplication().container.checklistRepository)
        }
        initializer {
            PhotoViewModel(checklistApplication().container.checklistRepository)
        }
        initializer {
            PdfExportViewModel(checklistApplication().container.checklistRepository)
        }
    }
}

/**
 * Função de extensão para recuperar a instância da aplicação
 * [ChecklistApplication] a partir de [CreationExtras].
 *
 * Necessária para acessar o container de dependências e obter
 * o [ChecklistRepository] usado pelos ViewModels.
 */
fun CreationExtras.checklistApplication(): ChecklistApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as ChecklistApplication)
