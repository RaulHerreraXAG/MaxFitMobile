package com.example.maxfit.Activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.maxfit.R
import org.json.JSONException
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {

    private lateinit var txtUsername: EditText
    private lateinit var txtPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var txtMatricula: TextView

    companion object {
        var matriculaCliente: String? = null
        var nombreCliente: String? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        txtUsername = findViewById(R.id.txtUsername)
        txtPassword = findViewById(R.id.txtPassword)
        btnLogin = findViewById(R.id.btnLogin)
        txtMatricula = findViewById(R.id.txtMatricula)

        btnLogin.setOnClickListener {
            val correo = txtUsername.text.toString()
            val contrasena = txtPassword.text.toString()
            authenticateUser(correo, contrasena)
        }
    }

    private fun obtenerMatriculaCliente(correo: String, contrasena: String) {
        val url = "http://192.168.56.1/maxfitdb/login.php?correo=$correo&contrasena=$contrasena"

        Log.d("LoginActivity", "URL de solicitud: $url")

        val requestQueue = Volley.newRequestQueue(this)
        val stringRequest = StringRequest(
            Request.Method.GET, url,
            Response.Listener { response ->
                try {
                    Log.d("LoginActivity", "Respuesta del servidor: $response")

                    val jsonResponse = JSONObject(response)
                    val success = jsonResponse.getBoolean("success")
                    if (success) {
                        val cliente = jsonResponse.getJSONObject("cliente")
                        val matricula = cliente.getString("matricula")
                        val nombre = cliente.getString("nombre")

                        txtMatricula.text = "Matrícula del cliente: $matricula"
                        LoginActivity.matriculaCliente = matricula
                        LoginActivity.nombreCliente = nombre
                        iniciarMenu()
                    } else {
                        Toast.makeText(
                            this@LoginActivity,
                            "Error de autenticación",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } catch (e: JSONException) {
                    // Si la respuesta no es un JSON válido, maneja el error
                    Log.e("LoginActivity", "Error de análisis JSON: ${e.message}")
                    Toast.makeText(
                        this@LoginActivity,
                        "Error de análisis JSON",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            },
            Response.ErrorListener { error ->
                Log.e("LoginActivity", "Error de conexión: ${error.message}")
                Toast.makeText(
                    this@LoginActivity,
                    "Error de conexión: ${error.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        )
        requestQueue.add(stringRequest)
    }

    private fun iniciarMenu() {
        val intent = Intent(this, MenuActivity::class.java)
        intent.putExtra("MATRICULA_CLIENTE", matriculaCliente)
        intent.putExtra("NOMBRE_CLIENTE", nombreCliente)
        startActivity(intent)
    }

    private fun authenticateUser(correo: String, contrasena: String) {
        Log.d("LoginActivity", "Autenticando usuario con correo: $correo, contraseña: $contrasena")
        obtenerMatriculaCliente(correo, contrasena)
    }
}
