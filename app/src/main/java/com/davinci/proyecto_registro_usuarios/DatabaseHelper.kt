
package com.davinci.proyecto_registro_usuarios

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, "MiPerfil.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        val crearTabla = """
            CREATE TABLE usuarios (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT NOT NULL,
                correo TEXT NOT NULL UNIQUE,
                password TEXT NOT NULL
            )
        """.trimIndent()

        db.execSQL(crearTabla)
    }

    override fun onUpgrade(
        db: SQLiteDatabase,
        oldVersion: Int,
        newVersion: Int
    ) {
        db.execSQL("DROP TABLE IF EXISTS usuarios")
        onCreate(db)
    }

    fun registrarUsuario(
        nombre: String,
        correo: String,
        password: String
    ): Boolean {

        val db = writableDatabase

        val valores = ContentValues().apply {
            put("nombre", nombre)
            put("correo", correo)
            put("password", password)
        }

        val resultado = db.insert("usuarios", null, valores)

        db.close()

        return resultado != -1L
    }
}
