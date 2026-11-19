
package com.davinci.proyecto_registro_usuarios

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class PerfilActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var imgPerfil: ImageView

    // Seleccionar fotografía de la galería
    private val seleccionarImagen = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->

        if (uri != null) {
            imgPerfil.setImageURI(uri)
        }
    }

    // Tomar fotografía con la cámara
    private val tomarFotografia = registerForActivityResult(
        ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->

        if (bitmap != null) {
            imgPerfil.setImageBitmap(bitmap)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_perfil)

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

        val correo = intent.getStringExtra("correo_usuario") ?: ""

        imgPerfil = findViewById(R.id.imgPerfil)

        val tvNombre = findViewById<TextView>(R.id.tvNombrePerfil)
        val tvCorreo = findViewById<TextView>(R.id.tvCorreoPerfil)

        val btnGaleria = findViewById<Button>(R.id.btnGaleria)
        val btnCamara = findViewById<Button>(R.id.btnCamara)
        val btnCerrarSesion = findViewById<Button>(R.id.btnCerrarSesion)

        // Cargar información del usuario
        val nombre = dbHelper.obtenerNombre(correo)

        if (nombre != null) {
            tvNombre.text = nombre
            tvCorreo.text = correo
        } else {
            Toast.makeText(
                this,
                "No se pudieron cargar los datos del usuario",
                Toast.LENGTH_LONG
            ).show()
        }

        // Abrir galería
        btnGaleria.setOnClickListener {
            seleccionarImagen.launch("image/*")
        }

        // Abrir cámara
        btnCamara.setOnClickListener {
            tomarFotografia.launch(null)
        }

        // Cerrar sesión
        btnCerrarSesion.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_CLEAR_TASK

            startActivity(intent)
            finish()
        }
    }
}
