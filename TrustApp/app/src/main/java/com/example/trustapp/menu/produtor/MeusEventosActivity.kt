package com.example.trustapp.menu.produtor

import Evento
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.trustapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MeusEventosActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private val listaEventos = mutableListOf<Evento>()
    private lateinit var adapter: MeusEventosAdapter
    private val database = FirebaseDatabase.getInstance().reference
    private val userId = FirebaseAuth.getInstance().currentUser?.uid

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meus_eventos)

        val back = findViewById<ImageView>(R.id.back)
        back.setOnClickListener {
            finish()
        }

        recyclerView = findViewById(R.id.recycler_meus_eventos)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = MeusEventosAdapter(listaEventos)
        recyclerView.adapter = adapter

        carregarEventosDoProdutor()
    }

    private fun carregarEventosDoProdutor() {
        database.child("eventos").orderByChild("idProdutor").equalTo(userId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    listaEventos.clear()
                    for (eventoSnap in snapshot.children) {
                        val evento = eventoSnap.getValue(Evento::class.java)
                        if (evento != null) {
                            listaEventos.add(evento)
                        }
                    }
                    adapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@MeusEventosActivity, "Erro ao carregar eventos", Toast.LENGTH_SHORT).show()
                }
            })
    }
}
