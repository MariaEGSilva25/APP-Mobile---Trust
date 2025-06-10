package com.example.trustapp.auth

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.trustapp.R
import com.example.trustapp.model.Produtor
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


class CadastroProdActivity : AppCompatActivity() {

    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance().reference
    private val cadastroManager = CadastroProdutorManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro_produtor)

        val back = findViewById<ImageView>(R.id.back)
        back.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        val btnCadastrar = findViewById<Button>(R.id.cadastrar)
        val razaoSocial = findViewById<EditText>(R.id.razaoSocial)
        val nomeFantasia = findViewById<EditText>(R.id.nomeFantasia)
        val doc = findViewById<EditText>(R.id.CNPJ)
        val email = findViewById<EditText>(R.id.email)
        val senha = findViewById<EditText>(R.id.senha)

        btnCadastrar.setOnClickListener {
            val razao = razaoSocial.text.toString().trim()
            val fantasia = nomeFantasia.text.toString().trim()
            val cnpj = doc.text.toString().trim()
            val mail = email.text.toString().trim()
            val password = senha.text.toString().trim()

            if (!cadastroManager.validarCampos(razao, fantasia, cnpj, mail, password)) {
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(mail, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val userId = auth.currentUser?.uid
                        val produtor = cadastroManager.criarProdutor(razao, fantasia, cnpj, mail)

                        userId?.let {
                            database.child("produtores").child(it).setValue(produtor)
                                .addOnSuccessListener {
                                    Toast.makeText(this, "Produtor cadastrado com sucesso!", Toast.LENGTH_SHORT).show()
                                    finish()
                                }
                        }
                    } else {
                        Toast.makeText(this, "Erro: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}