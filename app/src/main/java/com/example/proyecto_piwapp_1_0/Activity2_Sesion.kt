package com.example.proyecto_piwapp_1_0

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button

class Activity2_Sesion : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_activity2_sesion)

        val btnSesionLogin= findViewById<Button>(R.id.btn_SesionLogin)
        val btnSesionRegister= findViewById<Button>(R.id.btn_SesionRegistro)

        btnSesionLogin.setOnClickListener {
            val btnSesionLogin= Intent(this,Activity3_Login::class.java);
            startActivity(btnSesionLogin)
            Log.i("Actividad_manual","Boton1 (iniciar sesion) pulsado -  iniciar sesion")
        }
        btnSesionRegister.setOnClickListener {
            val btnSesionRegister= Intent(this,Activity4_Register::class.java);
            startActivity(btnSesionRegister)
            Log.i("Actividad_manual","Boton2 (crear cuenta ) pulsado -  crear cuenta")
        }

    }
}