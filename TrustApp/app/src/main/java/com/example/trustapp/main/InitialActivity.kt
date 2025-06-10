package com.example.trustapp.main

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.trustapp.R
import com.example.trustapp.SuporteActivity
import com.example.trustapp.auth.LoginActivity
import com.example.trustapp.eventos.EventosPorCategoriaActivity
import com.example.trustapp.eventos.ProximosEventosActivity
import com.example.trustapp.menu.MeusDadosActivity
import com.example.trustapp.menu.produtor.MeusEventosActivity
import com.example.trustapp.menu.MeusPedidosActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class InitialActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_initial)

        val textViewLocalizacao = findViewById<TextView>(R.id.localizacao)
        val btnAdd = findViewById<ImageView>(R.id.btnAdd)
        val imageView2 = findViewById<ImageView>(R.id.imageView2)

        val uid = FirebaseAuth.getInstance().currentUser?.uid
        val database = FirebaseDatabase.getInstance()

        val produtorRef = database.getReference("produtores").child(uid!!)
        val usuarioRef = database.getReference("usuarios").child(uid)

        produtorRef.get().addOnSuccessListener { produtorSnapshot ->
            if (produtorSnapshot.exists()) {
                btnAdd.visibility = View.VISIBLE
                textViewLocalizacao.visibility = View.GONE

                configurarMenu(imageView2, "produtor")

                btnAdd.setOnClickListener {
                    startActivity(Intent(this, CriarEventoActivity::class.java))
                }

            } else {
                usuarioRef.get().addOnSuccessListener { usuarioSnapshot ->
                    if (usuarioSnapshot.exists()) {
                        btnAdd.visibility = View.GONE
                        textViewLocalizacao.visibility = View.VISIBLE

                        val endereco = usuarioSnapshot.child("endereco").getValue(String::class.java)
                        textViewLocalizacao.text = endereco ?: "Localização não informada"

                        configurarMenu(imageView2, "usuario")
                    } else {
                        Toast.makeText(this, "Usuário não encontrado", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        findViewById<ImageView>(R.id.workshop).setOnClickListener {
            abrirEventosDaCategoria("Workshop")
        }

        findViewById<ImageView>(R.id.retiro).setOnClickListener {
            abrirEventosDaCategoria("Retiro")
        }

        findViewById<ImageView>(R.id.culto).setOnClickListener {
            abrirEventosDaCategoria("Culto")
        }
        findViewById<ImageView>(R.id.outros).setOnClickListener {
            abrirEventosDaCategoria("Outros")
        }

        val imgProximosEventos = findViewById<ImageView>(R.id.imgProximosEventos)
        imgProximosEventos.setOnClickListener {
            val intent = Intent(this, ProximosEventosActivity::class.java)
            startActivity(intent)
        }


    }

    private fun abrirEventosDaCategoria(categoria: String) {
        val intent = Intent(this, EventosPorCategoriaActivity::class.java)
        intent.putExtra("categoria", categoria)
        startActivity(intent)
    }


    @SuppressLint("ResourceType")
    private fun configurarMenu(anchor: ImageView, tipo: String) {
        anchor.setOnClickListener {
            val popup = PopupMenu(this, anchor)
            popup.menuInflater.inflate(R.layout.activity_menu_usuario, popup.menu)

            if (tipo == "usuario") {
                popup.menu.removeItem(R.id.menu_eventos)
            } else {
                popup.menu.removeItem(R.id.menu_pedidos)
            }

            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.menu_dados -> {
                        startActivity(Intent(this, MeusDadosActivity::class.java))
                        true
                    }
                    R.id.menu_eventos -> {
                        startActivity(Intent(this, MeusEventosActivity::class.java))
                        true
                    }
                    R.id.menu_pedidos -> {
                        startActivity(Intent(this, MeusPedidosActivity::class.java))
                        true
                    }

                    R.id.menu_suporte -> {
                        startActivity(Intent(this, SuporteActivity::class.java))
                        true
                    }
                    R.id.menu_sair -> {
                        FirebaseAuth.getInstance().signOut()
                        Toast.makeText(this, "Saindo...", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, LoginActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                        true
                    }
                    else -> false
                }
            }
            popup.show()
        }
    }

}
