package com.example.crudproyecto

import androidx.room.*

@Dao
interface ProductoDao {

    // Insertar un producto, devuelve el ID del producto insertado
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(producto: Producto): Long

    // Actualizar un producto, devuelve el número de filas afectadas
    @Update
    suspend fun update(producto: Producto): Int

    // Eliminar un producto, devuelve el número de filas eliminadas
    @Delete
    suspend fun delete(producto: Producto): Int

    // Obtener todos los productos
    @Query("SELECT * FROM Producto")
    suspend fun getAll(): List<Producto>
}


