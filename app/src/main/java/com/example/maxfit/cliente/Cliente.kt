package com.example.maxfit.cliente

data class Cliente(
    val matricula: Int,
    val nombre: String,
    val apellidos: String,
    val edad: Int,
    val genero: String,
    val altura: String,
    val peso: String,
    val correo: String,
    val contrasena: String,
    val fechaInicio: String,
    val fechaFin: String,
    val observacion: String
)
