package com.example.myapplication10.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface EstudianteDao {

    @Query("SELECT * FROM estudiantes ORDER BY nombre COLLATE NOCASE")
    fun obtenerTodos(): Flow<List<Estudiante>>

    @Query("SELECT COUNT(*) FROM estudiantes")
    suspend fun contarEstudiantes(): Int

    @Insert
    suspend fun insertar(estudiante: Estudiante)

    @Insert
    suspend fun insertarTodos(estudiantes: List<Estudiante>)

    @Update
    suspend fun actualizar(estudiante: Estudiante)

    @Delete
    suspend fun eliminar(estudiante: Estudiante)

    @Query("DELETE FROM estudiantes")
    suspend fun borrarTodos()
}
