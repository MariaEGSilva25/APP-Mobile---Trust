package com.example.trustapp.main

import Evento
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.trustapp.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.checkbox.MaterialCheckBox
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat

import java.util.*

class CriarEventoActivity : AppCompatActivity() {

    private lateinit var inputNomeEvento: TextInputEditText
    private lateinit var inputCategoria: AutoCompleteTextView
    private lateinit var inputAssunto: TextInputEditText
    private lateinit var inputDataInicio: TextInputEditText
    private lateinit var inputHoraInicio: TextInputEditText
    private lateinit var inputDataTermino: TextInputEditText
    private lateinit var inputHoraTermino: TextInputEditText
    private lateinit var inputDescricao: TextInputEditText
    private lateinit var inputLocal: TextInputEditText
    private lateinit var inputCep: TextInputEditText
    private lateinit var inputEndereco: TextInputEditText
    private lateinit var inputComplemento: TextInputEditText
    private lateinit var inputEstado: TextInputEditText
    private lateinit var inputCidade: TextInputEditText
    private lateinit var inputValor: TextInputEditText
    private lateinit var inputQuantidade: TextInputEditText
    private lateinit var inputQtdPorCompra: TextInputEditText
    private lateinit var checkboxGratuito: MaterialCheckBox
    private lateinit var idProdutor: String
    private lateinit var btnPublicar: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_criar_evento)

        val user = FirebaseAuth.getInstance().currentUser
        if (user == null) {
            Toast.makeText(this, "Erro: Usuário não autenticado.", Toast.LENGTH_SHORT).show()
            return
        }
        idProdutor = user.uid

        val back = findViewById<ImageView>(R.id.back)
        back.setOnClickListener {
            finish()
        }

        inputNomeEvento = findViewById(R.id.input_nome_evento)
        inputCategoria = findViewById(R.id.input_categoria)
        val categorias = listOf("Workshop", "Retiro", "Culto", "Outros")
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, categorias)
        inputCategoria.setAdapter(adapter)
        inputAssunto = findViewById(R.id.input_assunto)
        inputDataInicio = findViewById(R.id.input_data_inicio)
        inputHoraInicio = findViewById(R.id.input_hora_inicio)
        inputDataTermino = findViewById(R.id.input_data_termino)
        inputHoraTermino = findViewById(R.id.input_hora_termino)
        inputDescricao = findViewById(R.id.input_descricao)
        inputLocal = findViewById(R.id.input_local)
        inputCep = findViewById(R.id.input_cep)
        inputEndereco = findViewById(R.id.input_endereco)
        inputComplemento = findViewById(R.id.input_complemento)
        inputEstado = findViewById(R.id.input_estado)
        inputCidade = findViewById(R.id.input_cidade)
        inputValor = findViewById(R.id.input_valor)
        inputQuantidade = findViewById(R.id.input_quantidade)
        inputQtdPorCompra = findViewById(R.id.input_qtd_por_compra)

        checkboxGratuito = findViewById(R.id.checkbox_gratuito)
        btnPublicar = findViewById(R.id.button_publicar_evento)

        checkboxGratuito.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                inputValor.setText("0.0")
                inputValor.isEnabled = false
            } else {
                inputValor.setText("")
                inputValor.isEnabled = true
            }
        }

        inputDataInicio.setOnClickListener { showDatePicker(inputDataInicio) }
        inputHoraInicio.setOnClickListener { showTimePicker(inputHoraInicio) }
        inputDataTermino.setOnClickListener { showDatePicker(inputDataTermino) }
        inputHoraTermino.setOnClickListener { showTimePicker(inputHoraTermino) }

        btnPublicar.setOnClickListener {
            if (validarCampos()) {
                salvarEvento()
            }
        }
    }

    private fun validarCampos(): Boolean {
        if (inputNomeEvento.text.isNullOrBlank() ||
            inputCategoria.text.isNullOrBlank() ||
            inputDataInicio.text.isNullOrBlank() ||
            inputHoraInicio.text.isNullOrBlank() ||
            inputDataTermino.text.isNullOrBlank() ||
            inputHoraTermino.text.isNullOrBlank()
        ) {
            Toast.makeText(this, "Preencha todos os campos obrigatórios.", Toast.LENGTH_SHORT).show()
            return false
        }

        val formato = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())

        return try {
            val inicio = formato.parse("${inputDataInicio.text} ${inputHoraInicio.text}") ?: return false
            val termino = formato.parse("${inputDataTermino.text} ${inputHoraTermino.text}") ?: return false
            val agora = Date()

            if (inicio.before(agora)) {
                Toast.makeText(this, "A data e hora de início devem ser futuras.", Toast.LENGTH_SHORT).show()
                return false
            }

                if (termino.before(inicio)) {
                Toast.makeText(this, "A data e hora de término devem ser iguais ou posteriores ao início.", Toast.LENGTH_SHORT).show()
                return false
            }
            if (!checkboxGratuito.isChecked) {
                val valor = inputValor.text.toString().toDoubleOrNull()
                if (valor == null || valor < 0.0) {
                    Toast.makeText(this, "Informe um valor válido para o ingresso.", Toast.LENGTH_SHORT).show()
                    return false
                }
            }

            true
        } catch (e: Exception) {
            Toast.makeText(this, "Formato de data/hora inválido.", Toast.LENGTH_SHORT).show()
            false
        }
    }


    private fun salvarEvento() {
        val idEvento = UUID.randomUUID().toString()

        val evento = Evento(
            id = idEvento,
            nome = inputNomeEvento.text.toString(),
            categoria = inputCategoria.text.toString(),
            assunto = inputAssunto.text.toString(),
            descricao = inputDescricao.text.toString(),
            dataInicio = inputDataInicio.text.toString(),
            horaInicio = inputHoraInicio.text.toString(),
            dataTermino = inputDataTermino.text.toString(),
            horaTermino = inputHoraTermino.text.toString(),
            local = inputLocal.text.toString(),
            cep = inputCep.text.toString(),
            endereco = inputEndereco.text.toString(),
            complemento = inputComplemento.text.toString(),
            estado = inputEstado.text.toString(),
            cidade = inputCidade.text.toString(),
            valor = if (checkboxGratuito.isChecked) 0.0 else inputValor.text.toString().toDoubleOrNull() ?: 0.0,
            quantidade = inputQuantidade.text.toString().toIntOrNull() ?: 0,
            quantidadePorCompra = inputQtdPorCompra.text.toString().toIntOrNull() ?: 1,
            gratuito = checkboxGratuito.isChecked,
            idProdutor = idProdutor
        )

        Log.d("CriarEvento", "Evento pronto para salvar: $evento")

        val database = FirebaseDatabase.getInstance().reference
        database.child("eventos").child(idEvento).setValue(evento)
            .addOnSuccessListener {
                Log.d("CriarEvento", "Evento salvo com sucesso!")
                Toast.makeText(this, "Evento criado com sucesso!", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener { e ->
                Log.e("CriarEvento", "Erro ao salvar no Firebase", e)
                Toast.makeText(this, "Erro ao criar evento: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }


    private fun showDatePicker(target: TextInputEditText) {
        val calendar = Calendar.getInstance()
        DatePickerDialog(
            this,
            { _, year, month, day ->
                val date = String.format("%02d/%02d/%04d", day, month + 1, year)
                target.setText(date)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun showTimePicker(target: TextInputEditText) {
        val calendar = Calendar.getInstance()
        TimePickerDialog(
            this,
            { _, hour, minute ->
                val time = String.format("%02d:%02d", hour, minute)
                target.setText(time)
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        ).show()
    }
}
