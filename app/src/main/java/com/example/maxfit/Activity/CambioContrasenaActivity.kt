package com.example.maxfit.Activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.maxfit.R
import org.json.JSONException
import org.json.JSONObject

class CambioContrasenaActivity : AppCompatActivity() {

    private lateinit var editTextNuevaContrasena: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cambio_contrasena)

        val matriculaCliente: String = LoginActivity.matriculaCliente ?: ""

        editTextNuevaContrasena = findViewById(R.id.txtNuevaContrasena)
        val buttonCambiarContrasena: Button = findViewById(R.id.btnCambiarContrasena)

        buttonCambiarContrasena.setOnClickListener {
            val nuevaContrasena = editTextNuevaContrasena.text.toString()

            // Registro de matrícula y nueva contraseña antes de enviar la solicitud al servidor
            Log.d("CambioContrasenaActivity", "Matrícula: $matriculaCliente, Nueva contraseña: $nuevaContrasena")

            // Aquí se debe implementar la lógica para cambiar la contraseña en el servidor
            cambiarContrasenaServidor(matriculaCliente, nuevaContrasena)
        }
    }

    private fun cambiarContrasenaServidor(matricula: String, nuevaContrasena: String) {
        val url = "http://192.168.56.1/maxfitdb/cambiocontrasena.php"

        val requestQueue = Volley.newRequestQueue(this)
        val stringRequest = object : StringRequest(
            Request.Method.POST, url,
            Response.Listener { response ->
                try {
                    val jsonResponse = JSONObject(response)
                    val success = jsonResponse.getBoolean("success")
                    val message = jsonResponse.getString("message")
                    if (success) {
                        mostrarMensaje("Contraseña cambiada correctamente")
                        // Redirigir al LoginActivity
                        redirigirALogin()
                    } else {
                        mostrarMensaje("Error: $message")
                    }
                } catch (e: JSONException) {
                    Log.e("CambioContrasenaActivity", "Error de análisis JSON: ${e.message}")
                    mostrarMensaje("Error de análisis JSON")
                }
            },
            Response.ErrorListener { error ->
                Log.e("CambioContrasenaActivity", "Error de conexión: ${error.message}")
                mostrarMensaje("Error de conexión: ${error.message}")
            }) {
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["matricula"] = matricula
                params["contrasena"] = nuevaContrasena
                return params
            }
        }
        requestQueue.add(stringRequest)
    }

    private fun redirigirALogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish() // Finaliza la actividad actual para que el usuario no pueda volver atrás
    }

    private fun mostrarMensaje(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
    }
}
