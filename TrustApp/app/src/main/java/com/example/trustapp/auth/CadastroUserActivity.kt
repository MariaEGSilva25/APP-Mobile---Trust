package com.example.trustapp.auth

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.trustapp.R
import com.example.trustapp.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.util.Calendar

class CadastroUserActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private val database = FirebaseDatabase.getInstance().reference
    private val cadastroUserManager = CadastroUserManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro_usuario)

        val back = findViewById<ImageView>(R.id.back)
        back.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        auth = FirebaseAuth.getInstance()

        val nomeCompleto = findViewById<EditText>(R.id.nomeCompleto)
        val email = findViewById<EditText>(R.id.email)
        val dataNascimento = findViewById<EditText>(R.id.dataNascimento)
        val endereco = findViewById<EditText>(R.id.endereco)
        val senha = findViewById<EditText>(R.id.senha)
        val btnSalvar = findViewById<Button>(R.id.btnSalvar)

        dataNascimento.setOnClickListener {
            val calendar = Calendar.getInstance()
            val ano = calendar.get(Calendar.YEAR)
            val mes = calendar.get(Calendar.MONTH)
            val dia = calendar.get(Calendar.DAY_OF_MONTH)

            val datePicker = DatePickerDialog(this, { _, year, month, dayOfMonth ->
                val dataSelecionada = String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year)
                dataNascimento.setText(dataSelecionada)
            }, ano, mes, dia)
            datePicker.show()
        }

        btnSalvar.setOnClickListener {
            val nome = nomeCompleto.text.toString().trim()
            val mail = email.text.toString().trim()
            val data = dataNascimento.text.toString().trim()
            val enderecoTxt = endereco.text.toString().trim()
            val senhaTxt = senha.text.toString().trim()

            if (!cadastroUserManager.validarCampos(nome, mail, data, enderecoTxt, senhaTxt)) {
                Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(mail, senhaTxt)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val userId = auth.currentUser?.uid

                        val usuario = cadastroUserManager.criarUsuario(nome, mail, data, enderecoTxt)

                        if (userId != null) {
                            database.child("usuarios").child(userId).setValue(usuario)
                                .addOnSuccessListener {
                                    Toast.makeText(this, "Usuário cadastrado com sucesso!", Toast.LENGTH_SHORT).show()
                                    finish()
                                }
                                .addOnFailureListener {
                                    Toast.makeText(this, "Erro ao salvar dados: ${it.message}", Toast.LENGTH_SHORT).show()
                                }
                        }
                    } else {
                        Toast.makeText(this, "Erro: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}