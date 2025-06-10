package com.example.trustapp.menu

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.trustapp.R
import com.example.trustapp.model.Pedido

class PedidosAdapter(private val pedidos: List<Pedido>) : RecyclerView.Adapter<PedidosAdapter.PedidoViewHolder>() {

    class PedidoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTituloEvento: TextView = itemView.findViewById(R.id.tvTituloEvento)
        val tvDataHoraInicio: TextView = itemView.findViewById(R.id.tvDataHoraInicio)
        val tvLocalEvento: TextView = itemView.findViewById(R.id.tvLocalEvento)
        val tvQuantidade: TextView = itemView.findViewById(R.id.tvQuantidade)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PedidoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_event, parent, false)
        return PedidoViewHolder(view)
    }

    override fun onBindViewHolder(holder: PedidoViewHolder, position: Int) {
        val pedido = pedidos[position]

        holder.tvTituloEvento.text = pedido.nomeEvento ?: "Evento desconhecido"
        holder.tvDataHoraInicio.text = "Início: ${pedido.data ?: "--"} às ${pedido.hora ?: "--"}"
        holder.tvLocalEvento.text = pedido.local ?: "Local não informado"
        holder.tvQuantidade.text = "Quantidade: ${pedido.quantidade ?: "--"}"

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, IngressoActivity::class.java).apply {
                putExtra("local", pedido.local)
                putExtra("data", pedido.data)
                putExtra("hora", pedido.hora)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = pedidos.size
}





