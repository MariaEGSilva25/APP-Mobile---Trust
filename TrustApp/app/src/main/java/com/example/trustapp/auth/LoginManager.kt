package com.example.trustapp.auth

import com.google.firebase.auth.FirebaseAuth

class LoginManager(private val auth: FirebaseAuth) {

    fun validarCampos(email: String, senha: String): Boolean {
        return email.isNotBlank() && senha.isNotBlank()
    }

    fun login(
        email: String,
        senha: String,
        onSuccess: () -> Unit,
        onFailure: (String?) -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, senha)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onSuccess()
                } else {
                    onFailure("Credenciais incorretas")
                }
            }
    }
}
