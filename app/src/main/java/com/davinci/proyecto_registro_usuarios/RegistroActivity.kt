
package com.davinci.proyecto_registro_usuarios

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class RegistroActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registro)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )
            insets
        }

        dbHelper = DatabaseHelper(this)

        val etNombre = findViewById<EditText>(R.id.etNombre)
        val etCorreo = findViewById<EditText>(R.id.etCorreoRegistro)
        val etPassword = findViewById<EditText>(R.id.etPasswordRegistro)
        val etConfirmar = findViewById<EditText>(R.id.etConfirmarPassword)
        val btnRegistrar = findViewById<Button>(R.id.btnRegistrar)
        val tvVolverLogin = findViewById<TextView>(R.id.tvVolverLogin)

        btnRegistrar.setOnClickListener {

            val nombre = etNombre.text.toString().trim()
            val correo = etCorreo.text.toString().trim()
            val password = etPassword.text.toString()
            val confirmar = etConfirmar.text.toString()

            when {
                nombre.isEmpty() || correo.isEmpty() ||
                        password.isEmpty() || confirmar.isEmpty() -> {
                    Toast.makeText(
                        this,
                        "Completa todos los campos",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                !Patterns.EMAIL_ADDRESS.matcher(correo).matches() -> {
                    Toast.makeText(
                        this,
                        "Ingresa un correo válido",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                password.length < 6 -> {
                    Toast.makeText(
                        this,
                        "La contraseña debe tener al menos 6 caracteres",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                password != confirmar -> {
                    Toast.makeText(
                        this,
                        "Las contraseñas no coinciden",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                else -> {
                    val registrado = dbHelper.registrarUsuario(
                        nombre,
                        correo,
                        password
                    )

                    if (registrado) {
                        Toast.makeText(
                            this,
                            "Usuario registrado correctamente",
                            Toast.LENGTH_LONG
                        ).show()

                        finish()
                    } else {
                        Toast.makeText(
                            this,
                            "Este correo ya está registrado",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }

        tvVolverLogin.setOnClickListener {
            finish()
        }
    }
}
