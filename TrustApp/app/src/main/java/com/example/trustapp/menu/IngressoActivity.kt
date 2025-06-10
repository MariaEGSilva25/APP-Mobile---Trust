package com.example.trustapp.menu

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.trustapp.R

class IngressoActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ingresso)

        val back = findViewById<ImageView>(R.id.back)
        back.setOnClickListener {
            finish()
        }

        val local = intent.getStringExtra("local")
        val data = intent.getStringExtra("data")
        val hora = intent.getStringExtra("hora")

        findViewById<TextView>(R.id.tvLocalIngresso).text = "Local: ${local ?: "--"}"
        findViewById<TextView>(R.id.tvDataIngresso).text = "Data: ${data ?: "--"}"
        findViewById<TextView>(R.id.tvHoraIngresso).text = "Horário: ${hora ?: "--"}"
    }
}
