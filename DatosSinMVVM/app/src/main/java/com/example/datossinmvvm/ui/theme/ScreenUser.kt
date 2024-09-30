package com.example.datossinmvvm.ui.theme

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.room.Room
import android.content.Context
import android.util.Log

import kotlinx.coroutines.launch
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*

import com.example.datossinmvvm.User
import com.example.datossinmvvm.UserDao
import com.example.datossinmvvm.UserDatabase


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenUser() {
    val context = LocalContext.current
    var db: UserDatabase
    var id by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var dataUser = remember { mutableStateOf("") }

    db = crearDatabase(context)

    val dao = db.userDao()
    val coroutineScope = rememberCoroutineScope()


    // Estructura del Scaffold con TopAppBar
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Gestión de Usuarios") },
                actions = {
                    // Botón para agregar usuario
                    IconButton(onClick = {
                        val user = User(0, firstName, lastName)
                        coroutineScope.launch {
                            AgregarUsuario(user, dao)
                        }
                        firstName = ""
                        lastName = ""
                    }) {
                        Icon(Icons.Default.Add, contentDescription = "Agregar Usuario")
                    }

                    // Botón para listar usuarios
                    IconButton(onClick = {
                        coroutineScope.launch {
                            val data = getUsers(dao)
                            dataUser.value = data
                        }
                    }) {
                        Icon(Icons.Default.List, contentDescription = "Listar Usuarios")
                    }

                    // Botón para eliminar el último usuario
                    IconButton(onClick = {
                        coroutineScope.launch {
                            eliminarUltimoUsuario(dao)
                        }
                    }) {
                        Icon(Icons.Default.Delete, contentDescription = "Eliminar Último Usuario")
                    }
                }
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                Spacer(Modifier.height(50.dp))
                TextField(
                    value = firstName,
                    onValueChange = { firstName = it },
                    label = { Text("First Name: ") },
                    singleLine = true
                )
                TextField(
                    value = lastName,
                    onValueChange = { lastName = it },
                    label = { Text("Last Name:") },
                    singleLine = true
                )
                Spacer(Modifier.height(16.dp))
                Text(
                    text = dataUser.value,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(top = 20.dp)
                )
            }
        }
    )
}

@Composable
fun crearDatabase(context: Context): UserDatabase {
    return Room.databaseBuilder(
        context,
        UserDatabase::class.java,
        "user_db"
    ).build()
}

suspend fun getUsers(dao: UserDao): String {
    var rpta = ""
    val users = dao.getAll()
    users.forEach { user ->
        rpta += "${user.firstName} - ${user.lastName}\n"
    }
    return rpta
}

suspend fun AgregarUsuario(user: User, dao: UserDao) {
    try {
        dao.insert(user)
    } catch (e: Exception) {
        Log.e("User", "Error: insert: ${e.message}")
    }
}

// Función para eliminar el último usuario agregado

suspend fun eliminarUltimoUsuario(dao: UserDao) {
    try {
        dao.deleteLastUser()
    } catch (e: Exception) {
        Log.e("User", "Error: deleteLastUser: ${e.message}")
    }
}