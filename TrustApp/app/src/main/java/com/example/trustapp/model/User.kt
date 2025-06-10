package com.example.trustapp.model

data class User(
    val nomeCompleto:String = "",
    val email:String = "",
    val dataDeNascimento:String = "",
    val endereco:String = "",
    val tipo: String = "usuario"
)
