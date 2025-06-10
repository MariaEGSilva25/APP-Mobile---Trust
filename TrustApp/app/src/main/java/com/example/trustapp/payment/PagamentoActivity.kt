package com.example.trustapp.payment

import Evento
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.trustapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class PagamentoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pagamento)

        val tvResumoEvento = findViewById<TextView>(R.id.tvResumoEvento)
        val btnPix = findViewById<Button>(R.id.btnPix)
        val btnCredito = findViewById<Button>(R.id.btnCredito)
        val btnDebito = findViewById<Button>(R.id.btnDebito)
        val layoutDinamico = findViewById<LinearLayout>(R.id.layoutPagamentoDinamico)

        val evento = intent.getSerializableExtra("evento") as? Evento
        val quantidade = intent.getIntExtra("quantidade", 1)

        val back = findViewById<ImageView>(R.id.back)
        back.setOnClickListener {
            finish()
        }

        evento?.let {
            val valorTotal = quantidade * (evento.valor ?: 0.0)
            val resumo = """
                Nome: ${it.nome}
                Data: ${it.dataInicio}
                Horário: ${it.horaInicio} às ${it.horaTermino}
                Local: ${it.local} - ${it.estado}
                Quantidade: $quantidade
                Valor: R$ %.2f
            """.trimIndent().format(valorTotal)

            tvResumoEvento.text = resumo
        }

        btnPix.setOnClickListener {
            layoutDinamico.removeAllViews()
            evento?.let { ev ->
                mostrarPagamentoPix(layoutDinamico, ev, quantidade)
            }
        }

        btnCredito.setOnClickListener {
            layoutDinamico.removeAllViews()
            evento?.let { ev ->
                mostrarPagamentoCartao(layoutDinamico, "crédito", ev, quantidade)
            }
        }

        btnDebito.setOnClickListener {
            layoutDinamico.removeAllViews()
            evento?.let { ev ->
                mostrarPagamentoCartao(layoutDinamico, "débito", ev, quantidade)
            }
        }
    }

    private fun mostrarPagamentoPix(layout: LinearLayout, evento: Evento, quantidade: Int) {
        val imageView = ImageView(this).apply {
            setImageResource(R.drawable.qrcode)
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 600
            )
        }

        val btnConfirmar = Button(this).apply {
            text = "Já paguei"
            setOnClickListener {
                Toast.makeText(
                    this@PagamentoActivity,
                    "Pagamento via PIX confirmado!",
                    Toast.LENGTH_SHORT
                ).show()
                salvarCompraUsuario(evento, quantidade)
            }
        }

        layout.addView(imageView)

        val text = TextView(this).apply {
            text = "Escaneie o QR Code para pagar. Após o pagamento, clique em 'Já paguei'."
            textSize = 16f
            setPadding(0, 16, 0, 0)
        }

        layout.addView(text)
        layout.addView(btnConfirmar)
    }

    private fun mostrarPagamentoCartao(
        layout: LinearLayout,
        tipo: String,
        evento: Evento,
        quantidade: Int
    ) {
        val numero = EditText(this).apply { hint = "Número do cartão" }
        val validade = EditText(this).apply { hint = "Validade (MM/AA)" }
        val cvv = EditText(this).apply { hint = "CVV" }

        val botaoPagar = Button(this).apply {
            text = "Confirmar pagamento $tipo"
            setOnClickListener {
                Toast.makeText(
                    this@PagamentoActivity,
                    "Pagamento $tipo realizado com sucesso!",
                    Toast.LENGTH_SHORT
                ).show()
                salvarCompraUsuario(evento, quantidade)
            }
        }

        layout.addView(numero)
        layout.addView(validade)
        layout.addView(cvv)
        layout.addView(botaoPagar)
    }

    private fun irParaConfirmacao() {
        val intent = Intent(this, ConfirmacaoActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun salvarCompraUsuario(evento: Evento, quantidade: Int) {
        val database = FirebaseDatabase.getInstance()
        val auth = FirebaseAuth.getInstance()
        val uid = auth.currentUser?.uid ?: return

        val referenciaUsuario =
            database.getReference("usuarios").child(uid).child("ingressosComprados").push()

        val ingresso = mapOf(
            "idEvento" to evento.id,
            "nomeEvento" to evento.nome,
            "quantidade" to quantidade,
            "data" to evento.dataInicio,
            "hora" to evento.horaInicio,
            "local" to evento.local,
            "estado" to evento.estado
        )

        referenciaUsuario.setValue(ingresso).addOnSuccessListener {
            atualizarQuantidadeIngressosEvento(evento, quantidade)
        }.addOnFailureListener {
            Toast.makeText(this, "Erro ao salvar ingresso para o usuário.", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun atualizarQuantidadeIngressosEvento(evento: Evento, quantidadeComprada: Int) {
        val database = FirebaseDatabase.getInstance()
        val referenciaEvento = database.getReference("eventos").child(evento.id ?: return)

        referenciaEvento.child("quantidadeIngressosDisponiveis").get()
            .addOnSuccessListener { snapshot ->
                val quantidadeAtual = snapshot.getValue(Int::class.java) ?: 0
                val novaQuantidade = quantidadeAtual - quantidadeComprada

                if (novaQuantidade >= 0) {
                    referenciaEvento.child("quantidadeIngressosDisponiveis")
                        .setValue(novaQuantidade)
                        .addOnSuccessListener {
                            Toast.makeText(
                                this,
                                "Compra realizada e evento atualizado!",
                                Toast.LENGTH_SHORT
                            ).show()
                            irParaConfirmacao()
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Erro ao atualizar evento.", Toast.LENGTH_SHORT)
                                .show()
                        }
                } else {
                    Toast.makeText(this, "Ingressos insuficientes!", Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener {
            Toast.makeText(this, "Erro ao buscar quantidade de ingressos.", Toast.LENGTH_SHORT)
                .show()
        }
    }
}