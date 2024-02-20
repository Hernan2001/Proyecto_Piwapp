package com.example.proyecto_piwapp_1_0

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnSiguiente1= findViewById<TextView>(R.id.txt_btnMain)

        btnSiguiente1.setOnClickListener {
            val btnSiguiente1= Intent(this,Activity1_Star::class.java);
            startActivity(btnSiguiente1)
        }




    }
}