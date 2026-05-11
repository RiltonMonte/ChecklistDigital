package com.example.checklistdigital

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.checklistdigital.ui.navigation.ChecklistNavGraph
import com.example.checklistdigital.ui.theme.ChecklistDigitalTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ChecklistDigitalTheme {
                ChecklistNavGraph()
            }
        }
    }
}


