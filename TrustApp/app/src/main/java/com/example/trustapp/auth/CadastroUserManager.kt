package com.example.trustapp.auth

import com.example.trustapp.model.User

class CadastroUserManager {

    fun validarCampos(nome: String, email: String, dataNascimento: String, endereco: String, senha: String): Boolean {
        return nome.isNotEmpty() && email.isNotEmpty() && dataNascimento.isNotEmpty() && endereco.isNotEmpty() && senha.isNotEmpty()
    }

    fun criarUsuario(nome: String, email: String, dataNascimento: String, endereco: String): User {
        return User(nomeCompleto = nome, email = email, dataDeNascimento = dataNascimento, endereco = endereco)
    }
}