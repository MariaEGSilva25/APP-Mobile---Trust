package com.example.trustapp.payment


import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.trustapp.R
import com.example.trustapp.menu.MeusPedidosActivity

class ConfirmacaoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirmacao)

        val tvMensagem = findViewById<TextView>(R.id.tvMensagem)
        tvMensagem.text = "Pagamento realizado com sucesso!\nSeus ingressos estão disponíveis na área do usuário."
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, MeusPedidosActivity::class.java)
            startActivity(intent)
            finish()
        }, 2500) //
    }
}
