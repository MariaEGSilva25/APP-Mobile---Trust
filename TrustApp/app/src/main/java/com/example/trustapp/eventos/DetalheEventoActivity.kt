package com.example.trustapp.eventos

import Evento
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.trustapp.R
import com.example.trustapp.payment.PagamentoActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.text.NumberFormat
import java.util.*

class DetalheEventoActivity : AppCompatActivity() {

    private lateinit var tvNomeEvento: TextView
    private lateinit var tvNomeEventoDetalhe: TextView
    private lateinit var tvHorarioEvento: TextView
    private lateinit var tvLocalEvento: TextView
    private lateinit var tvValorIngresso: TextView
    private lateinit var btnVoltar: ImageView
    private lateinit var tvQuantidade: TextView
    private lateinit var tvDescricaoEvento: TextView

    private var quantidade = 1
    private var valorUnitario = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_evento_detalhe)

        val uid = FirebaseAuth.getInstance().currentUser?.uid
        val database = FirebaseDatabase.getInstance()
        val produtorRef = database.getReference("produtores").child(uid!!)

        tvNomeEvento = findViewById(R.id.tvNomeEvento)
        tvNomeEventoDetalhe = findViewById(R.id.tvNomeEventoDetalhe)
        tvHorarioEvento = findViewById(R.id.tvHorarioEvento)
        tvValorIngresso = findViewById(R.id.tvValorIngresso)
        tvLocalEvento = findViewById(R.id.tvLocalEvento)
        btnVoltar = findViewById(R.id.btnVoltar)
        tvQuantidade = findViewById(R.id.tvQuantidade)
        tvDescricaoEvento = findViewById(R.id.tvDescricaoEvento)

        val btnMenos = findViewById<Button>(R.id.btnMenos)
        val btnMais = findViewById<Button>(R.id.btnMais)
        val btnComprar = findViewById<Button>(R.id.btnComprar)

        val evento = intent.getSerializableExtra("evento") as? Evento

        if (evento != null) {
            tvNomeEvento.text = evento.nome
            tvNomeEventoDetalhe.text = evento.nome
            val horario = "${evento.dataInicio} - ${evento.horaInicio} até ${evento.dataTermino} - ${evento.horaTermino}"
            tvHorarioEvento.text = horario
            tvLocalEvento.text = "${evento.estado} - ${evento.local}"
            tvDescricaoEvento.text = evento.descricao ?: "Sem descrição disponível"
            valorUnitario = evento.valor ?: 0.0
            atualizarValorTotal()


            btnMais.setOnClickListener {
                if (quantidade < evento.quantidadePorCompra) {
                    quantidade++
                    tvQuantidade.text = quantidade.toString()
                    atualizarValorTotal()
                }
            }

            btnMenos.setOnClickListener {
                if (quantidade > 1) {
                    quantidade--
                    tvQuantidade.text = quantidade.toString()
                    atualizarValorTotal()
                }
            }

        } else {
            tvNomeEvento.text = "Evento não encontrado"
            tvNomeEventoDetalhe.text = ""
            tvHorarioEvento.text = ""
            tvLocalEvento.text = ""
            tvValorIngresso.text = ""
        }

        btnVoltar.setOnClickListener {
            finish()
        }

        produtorRef.get().addOnSuccessListener { produtorSnapshot ->
            if (produtorSnapshot.exists()) {

                btnComprar.isEnabled = false
                btnComprar.alpha = 0.5f
                btnMenos.isEnabled = false
                btnMenos.alpha = 0.5f
                btnMais.isEnabled = false
                btnMais.alpha = 0.5f
            }
        }

        btnComprar.setOnClickListener {
            val intent = Intent(this, PagamentoActivity::class.java)
            intent.putExtra("evento", evento)
            intent.putExtra("quantidade", quantidade)
            startActivity(intent)
        }

    }


    private fun atualizarValorTotal() {
        val valorTotal = valorUnitario * quantidade
        val valorFormatado = NumberFormat.getCurrencyInstance(Locale("pt", "BR")).format(valorTotal)
        tvValorIngresso.text = valorFormatado
    }
}
