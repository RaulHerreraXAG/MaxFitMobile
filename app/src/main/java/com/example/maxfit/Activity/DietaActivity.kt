package com.example.maxfit.Activity

import android.os.Bundle
import android.widget.Toast
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
import java.util.Locale

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
            obtenerDietaPredeterminada(matriculaCliente) // Llamar a la función para obtener la dieta predeterminada
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
                            val carbohidratosTotal =
                                dietaJsonObject.getString("carbohidratos_total")
                            val grasasTotal = dietaJsonObject.getString("grasas_total")
                            val pasos = dietaJsonObject.getString("Pasos")
                            val alimentos = dietaJsonObject.getString("alimentos")
                            val momentoDia = dietaJsonObject.getString("momento_dia")

                            // Asignar un valor numérico a cada día de la semana
                            val diaOrden: Int = when (dia.toLowerCase(Locale.getDefault())) {
                                "lunes" -> 1
                                "martes" -> 2
                                "miércoles" -> 3
                                "jueves" -> 4
                                "viernes" -> 5
                                else -> 6 // Para cualquier otro día
                            }

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
                                    momentoDia,
                                    diaOrden // Agregar el valor numérico del día
                                )
                            )
                        }

                        // Ordenar la lista por el valor numérico del día
                        dietaList.sortBy { it.diaOrden }

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

    private fun obtenerDietaPredeterminada(matriculaCliente: String) {
        val urlDietaPredeterminada =
            "http://192.168.56.1/maxfitdb/dieta_predeterminada.php?matricula=$matriculaCliente"

        val requestQueue = Volley.newRequestQueue(this)
        val stringRequestPredeterminada = StringRequest(
            Request.Method.GET, urlDietaPredeterminada,
            { response ->
                try {
                    val jsonResponse = JSONObject(response)
                    if (jsonResponse.getBoolean("success")) {
                        // Si la dieta predeterminada no es nula, mostrarla
                        mostrarDietaPredeterminada(jsonResponse)
                    } else {
                        // Si la dieta predeterminada es nula, mostrar un mensaje indicando que no hay dietas disponibles
                        Toast.makeText(
                            this,
                            "No hay dietas disponibles para este cliente",
                            Toast.LENGTH_SHORT
                        ).show()
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
        requestQueue.add(stringRequestPredeterminada)
    }

    private fun mostrarDietaPredeterminada(jsonResponse: JSONObject) {
        try {
            val dietaArray = jsonResponse.getJSONArray("dieta")
            val dietaList = mutableListOf<Dieta>()

            for (i in 0 until dietaArray.length()) {
                val dietaJsonObject = dietaArray.getJSONObject(i)
                val dia = dietaJsonObject.getString("dia_semana")
                val nombre = dietaJsonObject.getString("nombre")
                val tiempoPreparacion = dietaJsonObject.getString("tiempo_preparacion")
                val kcalTotal = dietaJsonObject.getString("kcal_total")
                val carbohidratosTotal = dietaJsonObject.getString("carbohidratos_total")
                val grasasTotal = dietaJsonObject.getString("grasas_total")
                val pasos = dietaJsonObject.getString("Pasos")
                val alimentos = dietaJsonObject.getString("alimentos")
                val momentoDia = dietaJsonObject.getString("momento_dia")

                // Asignar un valor numérico a cada día de la semana
                val diaOrden: Int = when (dia.toLowerCase(Locale.getDefault())) {
                    "lunes" -> 1
                    "martes" -> 2
                    "miércoles" -> 3
                    "jueves" -> 4
                    "viernes" -> 5
                    else -> 6 // Para cualquier otro día
                }

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
                        momentoDia,
                        diaOrden // Agregar el valor numérico del día
                    )
                )
            }

            // Ordenar la lista por el valor numérico del día
            dietaList.sortBy { it.diaOrden }

            // Establecer la lista ordenada en el adaptador
            dietaAdapter.setDieta(dietaList)
        } catch (e: JSONException) {
            e.printStackTrace()
            Toast.makeText(this, "Error de análisis JSON", Toast.LENGTH_SHORT).show()
        }
    }
}
