package com.example.trustapp.eventos

import Evento
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.trustapp.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class ProximosEventosActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var eventoAdapter: EventoAdapter
    private lateinit var database: DatabaseReference
    private val listaEventos = mutableListOf<Evento>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.item_events_categoria)

        findViewById<TextView>(R.id.tvTituloCategoria).text = "Próximos Eventos"

        recyclerView = findViewById(R.id.recyclerEventos)
        recyclerView.layoutManager = LinearLayoutManager(this)

        eventoAdapter = EventoAdapter(listaEventos) { eventoSelecionado ->
            val intent = Intent(this, DetalheEventoActivity::class.java)
            intent.putExtra("evento", eventoSelecionado)
            startActivity(intent)
        }

        recyclerView.adapter = eventoAdapter

        database = FirebaseDatabase.getInstance().getReference("eventos")
        buscarProximosEventos()

        findViewById<ImageView>(R.id.back).setOnClickListener {
            finish()
        }
    }

    private fun buscarProximosEventos() {
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listaEventos.clear()

                val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                val agora = Date()

                val eventosValidos = mutableListOf<Evento>()

                for (eventSnapshot in snapshot.children) {
                    val evento = eventSnapshot.getValue(Evento::class.java)
                    if (evento != null && evento.dataTermino.isNotBlank() && evento.horaTermino.isNotBlank()) {
                        try {
                            val dataHoraStr = "${evento.dataTermino} ${evento.horaTermino}"
                            val dataHoraTermino = dateFormat.parse(dataHoraStr)
                            if (dataHoraTermino != null && dataHoraTermino.after(agora)) {
                                eventosValidos.add(evento)
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }

                eventosValidos.sortBy {
                    val dataHoraStr = "${it.dataTermino} ${it.horaTermino}"
                    dateFormat.parse(dataHoraStr)
                }
                listaEventos.addAll(eventosValidos.take(5))

                eventoAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
}

