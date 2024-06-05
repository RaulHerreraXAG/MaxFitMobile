package com.example.maxfit.Activity

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.maxfit.R

class DietaDetalleActivity : AppCompatActivity() {

    private lateinit var tvReceta: TextView
    private lateinit var tvPasos: TextView
    private lateinit var tvAlimentos: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dieta_detalle)

        tvReceta = findViewById(R.id.tvRecetaDetalle)
        tvPasos = findViewById(R.id.tvPasos)
        tvAlimentos = findViewById(R.id.tvAlimentos)

        val receta = intent.getStringExtra("RECETA")
        val pasos = intent.getStringExtra("PASOS")
        val alimentos = intent.getStringExtra("ALIMENTOS")

        tvReceta.text = receta
        tvPasos.text = pasos
        tvAlimentos.text = alimentos
    }
}
