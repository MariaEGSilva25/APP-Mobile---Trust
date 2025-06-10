package com.example.trustapp.menu

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.trustapp.main.InitialActivity
import com.example.trustapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase

class MeusDadosActivity : AppCompatActivity() {

    private lateinit var tvEmail: TextView
    private lateinit var tvNome: TextView
    private lateinit var tvEndereco: TextView
    private lateinit var tvNascimento: TextView
    private lateinit var tvRazao: TextView

    private lateinit var tvNomeLabel: TextView
    private lateinit var tvEnderecoLabel: TextView
    private lateinit var tvNascimentoLabel: TextView
    private lateinit var tvRazaoLabel: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meus_dados)

        tvEmail = findViewById(R.id.tvEmail)
        tvNome = findViewById(R.id.tvNome)
        tvEndereco = findViewById(R.id.tvEndereco)
        tvNascimento = findViewById(R.id.tvNascimento)
        tvRazao = findViewById(R.id.tvRazao)

        tvNomeLabel = findViewById(R.id.tvNomeLabel)
        tvEnderecoLabel = findViewById(R.id.tvEnderecoLabel)
        tvNascimentoLabel = findViewById(R.id.tvNascimentoLabel)
        tvRazaoLabel = findViewById(R.id.tvRazaoLabel)

        val back = findViewById<ImageView>(R.id.back)

        back.setOnClickListener{
            val intent = Intent(this, InitialActivity::class.java)
            startActivity(intent)
        }

        val uid = FirebaseAuth.getInstance().currentUser?.uid
        if (uid == null) {
            Toast.makeText(this, "Usuário não autenticado", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val database = FirebaseDatabase.getInstance().reference

        val usuariosRef = database.child("usuarios").child(uid)
        val produtoresRef = database.child("produtores").child(uid)

        usuariosRef.get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                val tipoUsuario = snapshot.child("tipo").getValue(String::class.java)
                if (tipoUsuario == "usuario") {
                    preencherCamposUsuario(snapshot)
                } else {
                    Toast.makeText(this, "Tipo de usuário desconhecido em usuários", Toast.LENGTH_SHORT).show()
                    finish()
                }
            } else {
                produtoresRef.get().addOnSuccessListener { prodSnapshot ->
                    if (prodSnapshot.exists()) {
                        val tipoProdutor = prodSnapshot.child("tipo").getValue(String::class.java)
                        if (tipoProdutor == "produtor") {
                            preencherCamposProdutor(prodSnapshot)
                        } else {
                            Toast.makeText(this, "Tipo de usuário desconhecido em produtores", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                    } else {
                        Toast.makeText(this, "Usuário não encontrado", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }.addOnFailureListener {
                    Toast.makeText(this, "Erro ao carregar dados do produtor", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Erro ao carregar dados do usuário", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun preencherCamposUsuario(snapshot: DataSnapshot) {
        tvEmail.text = snapshot.child("email").getValue(String::class.java) ?: "Não informado"
        tvNome.text = snapshot.child("nomeCompleto").getValue(String::class.java) ?: "Não informado"
        tvEndereco.text = snapshot.child("endereco").getValue(String::class.java) ?: "Não informado"
        tvNascimento.text = snapshot.child("dataDeNascimento").getValue(String::class.java) ?: "Não informado"

        tvNome.visibility = TextView.VISIBLE
        tvNomeLabel.visibility = TextView.VISIBLE
        tvEndereco.visibility = TextView.VISIBLE
        tvEnderecoLabel.visibility = TextView.VISIBLE
        tvNascimento.visibility = TextView.VISIBLE
        tvNascimentoLabel.visibility = TextView.VISIBLE

        tvRazao.visibility = TextView.GONE
        tvRazaoLabel.visibility = TextView.GONE
    }

    private fun preencherCamposProdutor(snapshot: DataSnapshot) {
        tvEmail.text = snapshot.child("email").getValue(String::class.java) ?: "Não informado"
        tvRazao.text = snapshot.child("razaoSocial").getValue(String::class.java) ?: "Não informado"

        tvRazao.visibility = TextView.VISIBLE
        tvRazaoLabel.visibility = TextView.VISIBLE

        tvNome.visibility = TextView.GONE
        tvNomeLabel.visibility = TextView.GONE
        tvEndereco.visibility = TextView.GONE
        tvEnderecoLabel.visibility = TextView.GONE
        tvNascimento.visibility = TextView.GONE
        tvNascimentoLabel.visibility = TextView.GONE
    }
}
