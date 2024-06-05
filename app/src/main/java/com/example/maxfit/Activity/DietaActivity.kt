package com.example.maxfit.Activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.maxfit.Adapter.DietaAdapter
import com.example.maxfit.R
import com.example.maxfit.dietaT.Dieta
import org.json.JSONException
import org.json.JSONObject

class DietaActivity : BaseActivity() {

    private lateinit var dietaAdapter: DietaAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dieta)

        recyclerView = findViewById(R.id.recyclerViewDieta)
        recyclerView.layoutManager = LinearLayoutManager(this)
        dietaAdapter = DietaAdapter()
        recyclerView.adapter = dietaAdapter

        val matriculaCliente = intent.getStringExtra("MATRICULA_CLIENTE")

        if (matriculaCliente != null) {
            obtenerDietaCliente(matriculaCliente)
        } else {
            Toast.makeText(this, "Matrícula del cliente no encontrada", Toast.LENGTH_SHORT).show()
        }
    }

    private fun obtenerDietaCliente(matriculaCliente: String) {
        val url = "http://192.168.56.1/maxfitdb/dieta.php?matricula=$matriculaCliente"

        val requestQueue = Volley.newRequestQueue(this)
        val stringRequest = StringRequest(
            Request.Method.GET, url,
            { response ->
                try {
                    val jsonResponse = JSONObject(response)
                    if (jsonResponse.getBoolean("success")) {
                        val dietaArray = jsonResponse.getJSONArray("dieta")
                        val dietaList = mutableListOf<Dieta>()

                        for (i in 0 until dietaArray.length()) {
                            val dietaJsonObject = dietaArray.getJSONObject(i)
                            val dia = dietaJsonObject.getString("dia")
                            val nombre = dietaJsonObject.getString("nombre")
                            val tiempoPreparacion = dietaJsonObject.getString("tiempo_preparacion")
                            val kcalTotal = dietaJsonObject.getString("kcal_total")
                            val carbohidratosTotal = dietaJsonObject.getString("carbohidratos_total")
                            val grasasTotal = dietaJsonObject.getString("grasas_total")
                            val pasos = dietaJsonObject.getString("Pasos")
                            val alimentos = dietaJsonObject.getString("alimentos")
                            val momento_dia = dietaJsonObject.getString("momento_dia")

                            dietaList.add(
                                Dieta(
                                    dia,
                                    nombre,
                                    tiempoPreparacion,
                                    kcalTotal,
                                    carbohidratosTotal,
                                    grasasTotal,
                                    pasos,
                                    alimentos,
                                    momento_dia
                                )
                            )
                        }

                        // Ordenar la lista por el día de la semana
                        dietaList.sortBy { it.dia }

                        // Establecer la lista ordenada en el adaptador
                        dietaAdapter.setDieta(dietaList)
                    } else {
                        Toast.makeText(this, "Error al obtener la dieta", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    Toast.makeText(this, "Error de análisis JSON", Toast.LENGTH_SHORT).show()
                }
            },
            { error ->
                error.printStackTrace()
                Toast.makeText(this, "Error de conexión: $error", Toast.LENGTH_SHORT).show()
            }
        )
        requestQueue.add(stringRequest)
    }
}
