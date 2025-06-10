package com.example.trustapp.auth

import com.example.trustapp.model.Produtor


class CadastroProdutorManager {
    fun validarCampos(razao: String, fantasia: String, cnpj: String, email: String, senha: String): Boolean {
        return razao.isNotBlank() && fantasia.isNotBlank() && cnpj.isNotBlank() && email.isNotBlank() && senha.isNotBlank()
    }

    fun criarProdutor(razao: String, fantasia: String, cnpj: String, email: String): Produtor {
        return Produtor(razao, fantasia, cnpj, email)
    }
}