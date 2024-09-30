package com.example.crudproyecto

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.crudproyecto.ui.theme.CRUDApp
import com.example.crudproyecto.ui.theme.CRUDProyectoTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CRUDProyectoTheme {
                CRUDProyectoTheme {
                    // Usamos Scaffold para crear la estructura con TopAppBar
                    Scaffold(
                        topBar = {
                            TopAppBar(title = { Text("CRUD Proyecto") })
                        },
                        content = { innerPadding ->
                            // Llamamos a la función que contiene el CRUD
                            CRUDApp(modifier = Modifier.padding(innerPadding))
                        }
                    )

                }
            }
        }

    }
}