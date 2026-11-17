
package com.davinci.proyecto_registro_usuarios

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

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

        val etCorreo = findViewById<EditText>(R.id.etCorreo)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val tvRegistro = findViewById<TextView>(R.id.tvRegistro)

        btnLogin.setOnClickListener {

            val correo = etCorreo.text.toString().trim()
            val password = etPassword.text.toString()

            when {
                correo.isEmpty() || password.isEmpty() -> {
                    Toast.makeText(
                        this,
                        "Ingresa tu correo y contraseña",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                else -> {
                    val valido = dbHelper.verificarUsuario(correo, password)

                    val existe = dbHelper.existeUsuario(correo)

                    Toast.makeText(
                        this,
                        "¿Usuario encontrado? $existe",
                        Toast.LENGTH_LONG
                    ).show()
                    if (valido) {
                        Toast.makeText(
                            this,
                            "¡Bienvenido!",
                            Toast.LENGTH_SHORT
                        ).show()

                        // Próximamente abriremos la pantalla de perfil.
                    } else {
                        Toast.makeText(
                            this,
                            "Correo o contraseña incorrectos",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

        tvRegistro.setOnClickListener {
            val intent = Intent(this, RegistroActivity::class.java)
            startActivity(intent)
        }
    }
}
