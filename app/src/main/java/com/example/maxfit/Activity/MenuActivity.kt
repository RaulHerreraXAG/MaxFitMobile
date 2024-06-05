package com.example.maxfit.Activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.maxfit.R

class MenuActivity : AppCompatActivity() {
    private lateinit var txtBienvenida : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        txtBienvenida = findViewById(R.id.bienvenidaTextView)
        val btnRutina = findViewById<Button>(R.id.btnRutina)
        val btnDieta = findViewById<Button>(R.id.btnDieta)
        val btnInformacion = findViewById<Button>(R.id.btnInformacion)

        val matriculaCliente = LoginActivity.matriculaCliente
        val nombreCliente = LoginActivity.nombreCliente

        // Mostrar el nombre del cliente en el TextView de bienvenida
        if (nombreCliente != null) {
            txtBienvenida.text = "Bienvenid@ $nombreCliente"
        } else {
            Toast.makeText(this, "El nombre del cliente no está disponible", Toast.LENGTH_SHORT).show()
        }



        btnRutina.setOnClickListener {
            if (matriculaCliente != null) {
                val intent = Intent(this, RutinaActivity::class.java)
                intent.putExtra("MATRICULA_CLIENTE", matriculaCliente)
                startActivity(intent)
            } else {
                Toast.makeText(this, "La matrícula del cliente no está disponible", Toast.LENGTH_SHORT).show()
            }
        }

        btnDieta.setOnClickListener {
            if (matriculaCliente != null) {
                val intent = Intent(this, DietaActivity::class.java)
                intent.putExtra("MATRICULA_CLIENTE", matriculaCliente)
                startActivity(intent)
            } else {
                Toast.makeText(this, "La matrícula del cliente no está disponible", Toast.LENGTH_SHORT).show()
            }
        }

        btnInformacion.setOnClickListener {
            if (matriculaCliente != null) {
                val intent = Intent(this, InformacionActivity::class.java)
                intent.putExtra("MATRICULA_CLIENTE", matriculaCliente)
                startActivity(intent)
            } else {
                Toast.makeText(this, "La matrícula del cliente no está disponible", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
