package com.example.crudproyecto.ui.theme

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.room.Room
import com.example.crudproyecto.AppDatabase
import com.example.crudproyecto.Producto
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CRUDApp()
        }
    }
}

@Composable
fun CRUDApp() {
    val db = Room.databaseBuilder(
        LocalContext.current,
        AppDatabase::class.java, "productos-db"
    ).build()

    val productoDao = db.productoDao()

    var nombre by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var productos by remember { mutableStateOf(listOf<Producto>()) }

    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("CRUD de Productos") })
        },
        content = {
            Column(modifier = Modifier.padding(16.dp)) {
                // Campo de texto para nombre del producto
                TextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre del Producto") }
                )

                // Campo de texto para precio del producto
                TextField(
                    value = precio,
                    onValueChange = { precio = it },
                    label = { Text("Precio") },
                    keyboardType = KeyboardType.Number
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Botón para agregar producto
                Button(onClick = {
                    val nuevoProducto = Producto(nombre = nombre, precio = precio.toDoubleOrNull() ?: 0.0)
                    coroutineScope.launch {
                        productoDao.insert(nuevoProducto)
                        productos = productoDao.getAll()
                    }
                }) {
                    Text("Agregar Producto")
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Listar productos
                productos.forEach { producto ->
                    Text("${producto.id}: ${producto.nombre} - $${producto.precio}")
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Botón para actualizar producto
                Button(onClick = {
                    if (productos.isNotEmpty()) {
                        val productoActualizado = productos.last().copy(precio = (precio.toDoubleOrNull() ?: 0.0))
                        coroutineScope.launch {
                            productoDao.update(productoActualizado)
                            productos = productoDao.getAll()
                        }
                    }
                }) {
                    Text("Actualizar Último Producto")
                }

                // Botón para eliminar producto
                Button(onClick = {
                    if (productos.isNotEmpty()) {
                        coroutineScope.launch {
                            productoDao.delete(productos.last())
                            productos = productoDao.getAll()
                        }
                    }
                }) {
                    Text("Eliminar Último Producto")
                }
            }
        }
    )
}

