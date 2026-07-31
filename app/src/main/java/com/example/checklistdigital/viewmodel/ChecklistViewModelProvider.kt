package com.example.checklistdigital.viewmodel

import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import com.example.checklistdigital.ChecklistApplication

object ChecklistViewModelProvider {

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
    }
}

fun CreationExtras.checklistApplication(): ChecklistApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as ChecklistApplication)
