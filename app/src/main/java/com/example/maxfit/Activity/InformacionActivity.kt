package com.example.maxfit.Activity

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.maxfit.R
import com.example.maxfit.cliente.Cliente
import org.json.JSONException
import org.json.JSONObject

class InformacionActivity : BaseActivity() {

    private lateinit var txtNombre: TextView
    private lateinit var txtApellidos: TextView
    private lateinit var txtEdad: TextView
    private lateinit var txtGenero: TextView
    private lateinit var txtAltura: TextView
    private lateinit var txtPeso: TextView
    private lateinit var txtCorreo: TextView
    private lateinit var txtFechaInicio: TextView
    private lateinit var txtFechaFin: TextView
    private lateinit var txtObservacion: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_informacion)

        txtNombre = findViewById(R.id.txtNombre)
        txtApellidos = findViewById(R.id.txtApellidos)
        txtEdad = findViewById(R.id.txtEdad)
        txtGenero = findViewById(R.id.txtGenero)
        txtAltura = findViewById(R.id.txtAltura)
        txtPeso = findViewById(R.id.txtPeso)
        txtCorreo = findViewById(R.id.txtCorreo)
        txtFechaInicio = findViewById(R.id.txtFechaInicio)
        txtFechaFin = findViewById(R.id.txtFechaFin)
        txtObservacion = findViewById(R.id.txtObservacion)

        val matriculaCliente = LoginActivity.matriculaCliente

        if (matriculaCliente != null) {
            obtenerInformacionCliente(matriculaCliente)
        }
    }

    private fun obtenerInformacionCliente(matriculaCliente: String?) {
        val url = "http://192.168.56.1/maxfitdb/insert.php?matricula=$matriculaCliente"

        val requestQueue = Volley.newRequestQueue(this)
        val stringRequest = StringRequest(
            Request.Method.GET, url,
            { response ->
                try {
                    val jsonResponse = JSONObject(response)
                    if (jsonResponse.getBoolean("success")) {
                        val clienteJsonObject = jsonResponse.getJSONObject("cliente")
                        val cliente = Cliente(
                            clienteJsonObject.getInt("matricula"),
                            clienteJsonObject.getString("nombre"),
                            clienteJsonObject.getString("apellidos"),
                            clienteJsonObject.getInt("edad"),
                            clienteJsonObject.getString("genero"),
                            clienteJsonObject.getString("altura"),
                            clienteJsonObject.getString("peso"),
                            clienteJsonObject.getString("correo"),
                            clienteJsonObject.getString("contrasena"),
                            clienteJsonObject.getString("fechaInicio"),
                            clienteJsonObject.getString("fechaFin"),
                            clienteJsonObject.getString("observacion")
                        )
                        mostrarInformacionCliente(cliente)
                    } else {
                        // Manejar error
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    // Manejar error de análisis JSON
                }
            },
            { error ->
                error.printStackTrace()
                // Manejar error de conexión
            }
        )
        requestQueue.add(stringRequest)
    }

    private fun mostrarInformacionCliente(cliente: Cliente) {
        // Mostrar la información del cliente en los TextView correspondientes
        txtNombre.text = cliente.nombre
        txtApellidos.text = cliente.apellidos
        txtEdad.text = cliente.edad.toString()
        txtGenero.text = cliente.genero
        txtAltura.text = cliente.altura
        txtPeso.text = cliente.peso
        txtCorreo.text = cliente.correo
        txtFechaInicio.text = cliente.fechaInicio
        txtFechaFin.text = cliente.fechaFin
        txtObservacion.text = cliente.observacion
    }
}
