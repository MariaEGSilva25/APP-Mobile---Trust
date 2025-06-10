package com.example.trustapp.eventos

import Evento
import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.trustapp.R

class EventoAdapter(
    private val eventos: List<Evento>,
    private val onItemClick: (Evento) -> Unit
) : RecyclerView.Adapter<EventoAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_event, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = eventos.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titulo: TextView = view.findViewById(R.id.tvTituloEvento)
        val local: TextView = view.findViewById(R.id.tvLocalEvento)

        val dataHoraInicio: TextView = view.findViewById(R.id.tvDataHoraInicio)
        val dataHoraTermino: TextView = view.findViewById(R.id.tvDataHoraTermino)

        fun bind(evento: Evento) {
            titulo.text = evento.nome
            local.text = "Local: ${evento.estado} - ${evento.local}"
            dataHoraInicio.text = "Início: ${evento.dataInicio} às ${evento.horaInicio}"
            dataHoraTermino.text = "Término: ${evento.dataTermino} às ${evento.horaTermino}"

            itemView.setOnClickListener {
                Log.d("EventoAdapter", "Evento clicado: ${evento.nome}")
                onItemClick(evento)
            }

        }
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val evento = eventos[position]
        holder.bind(evento)
    }
}
