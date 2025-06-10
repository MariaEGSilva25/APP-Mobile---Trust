package com.example.trustapp.menu

import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.trustapp.R
import com.example.trustapp.model.Pedido
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
class MeusPedidosActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PedidosAdapter
    private val pedidosList = mutableListOf<Pedido>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meus_pedidos)

        val back = findViewById<ImageView>(R.id.back)
        back.setOnClickListener {
            finish()
        }

        recyclerView = findViewById(R.id.rvPedidos)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = PedidosAdapter(pedidosList)
        recyclerView.adapter = adapter

        buscarPedidosDoUsuario()
    }

    private fun buscarPedidosDoUsuario() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val referencia = FirebaseDatabase.getInstance().getReference("usuarios")
            .child(uid)
            .child("ingressosComprados")

        referencia.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                pedidosList.clear()
                for (pedidoSnapshot in snapshot.children) {
                    val pedido = pedidoSnapshot.getValue(Pedido::class.java)
                    pedido?.let { pedidosList.add(it) }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MeusPedidosActivity, "Erro ao carregar pedidos", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
