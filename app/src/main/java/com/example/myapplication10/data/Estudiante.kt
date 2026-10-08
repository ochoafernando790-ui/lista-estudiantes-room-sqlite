package com.example.myapplication10.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "estudiantes")
data class Estudiante(
    @PrimaryKey val id: String,
    val nombre: String,
    val edad: String,
    val sexo: String,
    val carrera: String,
    val telefono: String,
    val correo: String,
    val fotoUri: String? = null
)
