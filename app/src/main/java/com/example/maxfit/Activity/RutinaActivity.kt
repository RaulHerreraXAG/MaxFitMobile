package com.example.maxfit.Activity

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.maxfit.R
import org.json.JSONException
import org.json.JSONObject

class RutinaActivity : BaseActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var rutinaAdapter: RutinaAdapter // Asegúrate de tener un adaptador adecuado para tu RecyclerView
    private lateinit var txtMatricula: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rutina)

        //txtMatricula = findViewById(R.id.matriculaTextView)
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        rutinaAdapter = RutinaAdapter() // Inicializa tu adaptador aquí
        recyclerView.adapter = rutinaAdapter

        val matriculaCliente2 = LoginActivity.matriculaCliente


        // Verificar que la matrícula del cliente no sea nula
        if (matriculaCliente2 != null) {
            // URL del script PHP para obtener la rutina del cliente
            val url = "http://192.168.56.1/maxfitdb/rutina.php?matricula=$matriculaCliente2"

            // Crear la solicitud GET utilizando Volley
            val requestQueue = Volley.newRequestQueue(this)
            val stringRequest = StringRequest(
                Request.Method.GET, url,
                { response ->
                    // Procesar la respuesta JSON
                    // Dentro del bloque try en la función StringRequest
                    val jsonResponse = JSONObject(response)
                    val success = jsonResponse.getBoolean("success")
                    if (success) {
                        val rutinaObject = jsonResponse.getJSONObject("rutina")
                        val rutinaKeys = rutinaObject.keys()

                        val listaEjercicios = mutableListOf<Ejercicio>()
                        while (rutinaKeys.hasNext()) {
                            val key = rutinaKeys.next()
                            val diaArray = rutinaObject.getJSONArray(key)

                            for (i in 0 until diaArray.length()) {
                                val ejercicioJson = diaArray.getJSONObject(i)
                                val ejercicio = Ejercicio(
                                    ejercicioJson.getInt("id"),
                                    key, // Usamos el día como la clave del ejercicio
                                    ejercicioJson.getInt("series"),
                                    ejercicioJson.getInt("repeticiones"),
                                    ejercicioJson.getString("nombre_ejercicio"),
                                    ejercicioJson.getString("grupo_muscular")
                                    // Usamos el ID del ejercicio como ejercicio
                                    // No hay información sobre el cliente en el JSON proporcionado, por lo que usamos 0
                                )
                                listaEjercicios.add(ejercicio)
                            }
                        }
                        rutinaAdapter.setEjercicios(listaEjercicios)
                    } else {
                        val message = jsonResponse.getString("message")
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                    }
                },
                { error ->
                    // Manejar errores de la solicitud
                    error.printStackTrace()
                    Toast.makeText(this, "Error en la solicitud: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            )

            // Agregar la solicitud a la cola de solicitudes
            requestQueue.add(stringRequest)
        } else {
            Log.e("RutinaActivity", "No se ha proporcionado la matrícula del cliente")
        }
    }
}


data class Ejercicio(val id: Int, val dia: String, val series: Int, val repeticiones: Int, val nombre: String, val grupo:String)

class RutinaAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val items = mutableListOf<Any>()

    companion object {
        private const val VIEW_TYPE_DIA = 0
        private const val VIEW_TYPE_EJERCICIO = 1
    }

    fun setEjercicios(lista: List<Ejercicio>) {
        items.clear()
        var lastDay: String? = null
        lista.forEach { ejercicio ->
            if (ejercicio.dia != lastDay) {
                items.add(ejercicio.dia)
                lastDay = ejercicio.dia
            }
            items.add(ejercicio)
        }
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return if (items[position] is String) VIEW_TYPE_DIA else VIEW_TYPE_EJERCICIO
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_DIA) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_dia, parent, false)
            DiaViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_ejercicio, parent, false)
            EjercicioViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is DiaViewHolder) {
            holder.bind(items[position] as String)
        } else if (holder is EjercicioViewHolder) {
            holder.bind(items[position] as Ejercicio)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class DiaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvDia: TextView = itemView.findViewById(R.id.tvDia)

        fun bind(dia: String) {
            tvDia.text = dia
        }
    }

    class EjercicioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvNombreEjercicio: TextView = itemView.findViewById(R.id.tvNombreEjercicio)
        private val tvCuerpo: TextView = itemView.findViewById(R.id.tvCuerpo)
        private val tvNumero: TextView = itemView.findViewById(R.id.tvNumero)
        private val tvObservaciones: TextView = itemView.findViewById(R.id.tvObservaciones)

        fun bind(ejercicio: Ejercicio) {
            tvNombreEjercicio.text = ejercicio.nombre
            tvCuerpo.text = "Series: ${ejercicio.series}"
            tvNumero.text = "Repeticiones: ${ejercicio.repeticiones}"
            tvObservaciones.text = "Grupo Muscolar: ${ejercicio.grupo}"
        }
    }
}



