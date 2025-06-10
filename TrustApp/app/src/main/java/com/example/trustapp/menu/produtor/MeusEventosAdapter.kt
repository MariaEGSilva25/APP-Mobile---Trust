package com.example.trustapp.menu.produtor

import Evento
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.trustapp.R

class MeusEventosAdapter(private val listaEventos: List<Evento>) :
    RecyclerView.Adapter<MeusEventosAdapter.EventoViewHolder>() {

    class EventoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nomeEvento: TextView = itemView.findViewById(R.id.tvNomeEvento)
        val dataEvento: TextView = itemView.findViewById(R.id.tvDataHora)
        val localEvento: TextView = itemView.findViewById(R.id.tvLocal)
        val tipoIngresso: TextView = itemView.findViewById(R.id.tvTipoIngresso)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_evento, parent, false)
        return EventoViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventoViewHolder, position: Int) {
        val evento = listaEventos[position]
        holder.nomeEvento.text = evento.nome
        holder.dataEvento.text = "${evento.dataInicio} às ${evento.horaInicio}"
        holder.localEvento.text = "Local: ${evento.local ?: "Indefinido"}"
        holder.tipoIngresso.text = if (evento.gratuito) {
            "Ingresso: Gratuito"
        } else {
            "Ingresso: Pago"
        }
    }


    override fun getItemCount(): Int = listaEventos.size
}
