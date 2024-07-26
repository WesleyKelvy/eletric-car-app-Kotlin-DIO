package com.example.eletriccarapp.ui

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.eletriccarapp.R

class ActivityCalcularAutonomia : AppCompatActivity() {
    lateinit var btnVoltar: Button
    lateinit var kmPercorrido: EditText
    lateinit var preco: EditText
    lateinit var resultado: TextView
    lateinit var btnCalcular: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calcular_autonomia)
        setupView()
        setuplisteners()
        setupCachedResult()

    }

    private fun setupCachedResult() {
        val valorCalculado = getSharedPref()
        resultado.text = valorCalculado.toString()
    }

    private fun setupView() {
        btnVoltar = findViewById(R.id.btn_voltar)
        preco = findViewById(R.id.et_preco_kwh)
        kmPercorrido = findViewById(R.id.et_km_percorrido)
        btnCalcular = findViewById(R.id.btn_calcular)
        resultado = findViewById(R.id.tv_resultado)
    }

    private fun setuplisteners() {
        btnVoltar.setOnClickListener {
            finish()
        }
        btnCalcular.setOnClickListener {
            calcular()
        }
    }

    private fun calcular() {
        val preco = preco.text.toString().toFloat()
        val km = kmPercorrido.text.toString().toFloat()
        val result = preco / km

        resultado.text = result.toString()
        saveSharedPref(result)
    }

    private fun saveSharedPref(resultado: Float) {
        val sharedPref = getPreferences(Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            putFloat(getString(R.string.saved_calc), resultado)
            apply()
        }
    }

    private fun getSharedPref(): Float {
        val sharedPref = getPreferences(Context.MODE_PRIVATE)
        val calculo = sharedPref.getFloat(getString(R.string.saved_calc), 0.0f)
        return calculo
    }
}