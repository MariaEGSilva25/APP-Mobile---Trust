package com.example.trustapp.eventos

import Evento
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.trustapp.R
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class EventosPorCategoriaActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var eventoAdapter: EventoAdapter
    private lateinit var database: DatabaseReference
    private val listaEventos = mutableListOf<Evento>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.item_events_categoria)

        val categoria = intent.getStringExtra("categoria")
        findViewById<TextView>(R.id.tvTituloCategoria).text = "Eventos de $categoria"

        recyclerView = findViewById(R.id.recyclerEventos)
        recyclerView.layoutManager = LinearLayoutManager(this)

        eventoAdapter = EventoAdapter(listaEventos) { eventoSelecionado ->
            Log.d("EventosPorCategoria", "Evento clicado: ${eventoSelecionado.nome}")
            val intent = Intent(this, DetalheEventoActivity::class.java)
            intent.putExtra("evento", eventoSelecionado)
            startActivity(intent)
        }

        recyclerView.adapter = eventoAdapter

        database = FirebaseDatabase.getInstance().getReference("eventos")
        buscarEventosPorCategoria(categoria)

        val back = findViewById<ImageView>(R.id.back)
        back.setOnClickListener {
            finish()
        }
    }

    private fun buscarEventosPorCategoria(categoria: String?) {
        if (categoria.isNullOrBlank()) return
        database.orderByChild("categoria").equalTo(categoria).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listaEventos.clear()
                val tvSemEventos = findViewById<TextView>(R.id.tvSemEventos)

                val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                val agora = Date()

                for (eventSnapshot in snapshot.children) {
                    val evento = eventSnapshot.getValue(Evento::class.java)

                    if (evento != null && evento.dataTermino.isNotBlank() && evento.horaTermino.isNotBlank()) {
                        try {
                            val dataHoraStr = "${evento.dataTermino} ${evento.horaTermino}"
                            val dataHoraTermino = dateFormat.parse(dataHoraStr)

                            if (dataHoraTermino != null && dataHoraTermino.after(agora)) {
                                listaEventos.add(evento)
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }

                eventoAdapter.notifyDataSetChanged()

                if (listaEventos.isEmpty()) {
                    tvSemEventos.visibility = TextView.VISIBLE
                    recyclerView.visibility = RecyclerView.GONE
                } else {
                    tvSemEventos.visibility = TextView.GONE
                    recyclerView.visibility = RecyclerView.VISIBLE
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

}
