package com.example.trustapp.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.trustapp.main.InitialActivity
import com.example.trustapp.R
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var loginManager: LoginManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()
        loginManager = LoginManager(auth)

        val email = findViewById<EditText>(R.id.email)
        val senha = findViewById<EditText>(R.id.senha)
        val btnLogin = findViewById<Button>(R.id.loginButton)
        val user = findViewById<TextView>(R.id.user)
        val prod = findViewById<TextView>(R.id.prod)

        user.setOnClickListener {
            startActivity(Intent(this, CadastroUserActivity::class.java))
        }

        prod.setOnClickListener {
            startActivity(Intent(this, CadastroProdActivity::class.java))
        }

        btnLogin.setOnClickListener {
            val emailText = email.text.toString()
            val senhaText = senha.text.toString()

            if (!loginManager.validarCampos(emailText, senhaText)) {
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            loginManager.login(
                emailText,
                senhaText,
                onSuccess = {
                    Toast.makeText(this, "Login realizado com sucesso", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, InitialActivity::class.java))
                    finish()
                },
                onFailure = { erro ->
                    Toast.makeText(this, "Erro: $erro", Toast.LENGTH_SHORT).show()
                }
            )
        }
    }
}
