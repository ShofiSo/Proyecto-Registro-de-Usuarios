
package com.davinci.proyecto_registro_usuarios

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Base64
import java.security.SecureRandom
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, "MiPerfil.db", null, 2) {

    override fun onCreate(db: SQLiteDatabase) {
        val crearTabla = """
            CREATE TABLE usuarios (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT NOT NULL,
                correo TEXT NOT NULL UNIQUE,
                password_hash TEXT NOT NULL,
                password_salt TEXT NOT NULL
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

    private fun generarHash(password: String, salt: ByteArray): String {
        val spec = PBEKeySpec(password.toCharArray(), salt, 120000, 256)
        val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1")
        val hash = factory.generateSecret(spec).encoded
        spec.clearPassword()

        return Base64.encodeToString(hash, Base64.NO_WRAP)
    }

    fun registrarUsuario(
        nombre: String,
        correo: String,
        password: String
    ): Boolean {

        val salt = ByteArray(16)
        SecureRandom().nextBytes(salt)

        val passwordHash = generarHash(password, salt)

        val valores = ContentValues().apply {
            put("nombre", nombre)
            put("correo", correo)
            put("password_hash", passwordHash)
            put("password_salt", Base64.encodeToString(salt, Base64.NO_WRAP))
        }

        val db = writableDatabase
        val resultado = db.insert("usuarios", null, valores)
        db.close()

        return resultado != -1L
    }
    fun verificarUsuario(correo: String, password: String): Boolean {

        val db = readableDatabase

        val cursor = db.rawQuery(
            "SELECT password_hash, password_salt FROM usuarios WHERE correo = ?",
            arrayOf(correo)
        )

        var valido = false

        if (cursor.moveToFirst()) {

            val hashGuardado = cursor.getString(0)
            val saltGuardado = cursor.getString(1)

            val salt = Base64.decode(saltGuardado, Base64.NO_WRAP)
            val hashIngresado = generarHash(password, salt)

            valido = hashGuardado == hashIngresado
        }

        cursor.close()
        db.close()

        return valido
    }
    fun existeUsuario(correo: String): Boolean {
        val db = readableDatabase

        val cursor = db.rawQuery(
            "SELECT id FROM usuarios WHERE correo = ?",
            arrayOf(correo.trim())
        )

        val existe = cursor.moveToFirst()

        cursor.close()
        db.close()

        return existe
    }
}
