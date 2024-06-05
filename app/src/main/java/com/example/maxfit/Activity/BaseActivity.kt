package com.example.maxfit.Activity

import android.content.Intent
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.maxfit.R
import com.google.android.material.bottomnavigation.BottomNavigationView

open class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // No llames a setContentView aquí
    }

    override fun setContentView(layoutResID: Int) {
        val matriculaCliente = LoginActivity.matriculaCliente
        val coordinatorLayout = layoutInflater.inflate(R.layout.activity_base, null)
        val container = coordinatorLayout.findViewById<FrameLayout>(R.id.container)
        layoutInflater.inflate(layoutResID, container, true)
        super.setContentView(coordinatorLayout)

        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigation.setOnItemSelectedListener { item ->
            if (item.itemId != bottomNavigation.selectedItemId) {
                when (item.itemId) {
                    R.id.navigation_home -> {
                            if (matriculaCliente != null) {
                                val intent = Intent(this, MenuActivity::class.java)
                                intent.putExtra("MATRICULA_CLIENTE", matriculaCliente)
                                startActivity(intent)
                            } else {
                                Toast.makeText(this, "La matrícula del cliente no está disponible", Toast.LENGTH_SHORT).show()
                            }
                        true
                    }
                    R.id.navigation_rutina -> {
                        if (this !is RutinaActivity) {
                            if (matriculaCliente != null) {
                                val intent = Intent(this, RutinaActivity::class.java)
                                intent.putExtra("MATRICULA_CLIENTE", matriculaCliente)
                                startActivity(intent)
                            } else {
                                Toast.makeText(this, "La matrícula del cliente no está disponible", Toast.LENGTH_SHORT).show()
                            }
                        }
                        true
                    }
                    R.id.navigation_diet -> {
                        if (this !is DietaActivity) {
                            if (matriculaCliente != null) {
                                val intent = Intent(this, DietaActivity::class.java)
                                intent.putExtra("MATRICULA_CLIENTE", matriculaCliente)
                                startActivity(intent)
                            } else {
                                Toast.makeText(this, "La matrícula del cliente no está disponible", Toast.LENGTH_SHORT).show()
                            }
                        }
                        true
                    }
                    R.id.navigation_profile -> {
                        if (this !is InformacionActivity) {
                            if (matriculaCliente != null) {
                                val intent = Intent(this, InformacionActivity::class.java)
                                intent.putExtra("MATRICULA_CLIENTE", matriculaCliente)
                                startActivity(intent)
                            } else {
                                Toast.makeText(this, "La matrícula del cliente no está disponible", Toast.LENGTH_SHORT).show()
                            }
                        }
                        true
                    }
                    else -> false
                }
            } else {
                false
            }
        }

        // Actualiza la selección del menú con base en la actividad actual
        updateNavigationBarState()
    }

    private fun updateNavigationBarState() {
        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        val menu = bottomNavigation.menu

        for (i in 0 until menu.size()) {
            val item = menu.getItem(i)
            item.isChecked = false
        }

        when (this) {
            is RutinaActivity -> menu.findItem(R.id.navigation_rutina).isChecked = true
            is DietaActivity -> menu.findItem(R.id.navigation_diet).isChecked = true
            is InformacionActivity -> menu.findItem(R.id.navigation_profile).isChecked = true
        }
    }
}
