package com.example.proyecto_piwapp_1_0

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.TextView
//import com.google.firebase.auth.FirebaseAuth

class Activity1_Star : AppCompatActivity() {
    //firebase
    //private lateinit var auth: FirebaseAuth;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_activity1_star)

        //auth= FirebaseAuth.getInstance();
/*
        Handler(Looper.myLooper()!!).postDelayed(Runnable {
            if(auth.currentUser != null){
                val homeSistema= Intent(this,Activity5__Inicio::class.java);
                startActivity(homeSistema)
            }else{
                val sesionSistema= Intent(this,Activity2_Sesion::class.java);
                startActivity(sesionSistema)
            }
        }, 2000)

 */


        val btnIniciar= findViewById<Button>(R.id.btn_Iniciar)

        btnIniciar.setOnClickListener {
            val btnIniciar= Intent(this,Activity2_Sesion::class.java);
            startActivity(btnIniciar)

            Log.i("Actividad_manual","Boton1 (siguiente) pulsado -Star presentacion")
        }



    }
}