package com.example.proyecto_piwapp_1_0

import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Email
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.proyecto_piwapp_1_0.databinding.ActivityActivity5InicioBinding
import com.example.proyecto_piwapp_1_0.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.zxing.BarcodeFormat
import com.google.zxing.integration.android.IntentIntegrator
import com.journeyapps.barcodescanner.BarcodeEncoder
import java.security.Provider

class Activity5__Inicio : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_activity5_inicio)

        val img_btnCinicio= findViewById<ImageView>(R.id.img_btnIinicio);
        val img_btnCcrear= findViewById<ImageView>(R.id.img_btnIcrear);
        val img_btnCusuario= findViewById<ImageView>(R.id.img_btnIusuario);

        val linearLayoutProyectos = findViewById<LinearLayout>(R.id.linearLayoutProyectos)



        img_btnCinicio.setOnClickListener {
            val img_btnCinicio= Intent(this,Activity5__Inicio::class.java);
            startActivity(img_btnCinicio)
            Log.i("Actividad_manual","Boton1 (pagina_inicio) pulsado -pagina_inicio")
        }
        img_btnCcrear.setOnClickListener {
            val img_btnCcrear= Intent(this,Activity6__Crear::class.java);
            startActivity(img_btnCcrear)
            Log.i("Actividad_manual","Boton2 (pagina_crear) pulsado -pagina_crear")
        }
        img_btnCusuario.setOnClickListener {
            val img_btnCusuario= Intent(this,Activity7__Usuario::class.java);
            startActivity(img_btnCusuario)
            Log.i("Actividad_manual","Boton3 (pagina_usuario) pulsado -pagina_usuario")
        }


        // Obtener el UID del usuario actual
        val uid = FirebaseAuth.getInstance().currentUser?.uid;

        // Consultar documentos en la colección "proyectos" para el usuario actual
        FirebaseFirestore.getInstance().collection("proyectos").document(uid!!)
            .collection("proyectos")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val nombreProyecto = document.getString("nombreProyecto")
                    val proyectoId = document.id

                    // Crear un botón dinámicamente para cada proyecto
                    val btnProyecto = Button(this)
                    btnProyecto.text = nombreProyecto

                    btnProyecto.setBackgroundResource(R.drawable.button_click_color1)
                    btnProyecto.setTextColor(ContextCompat.getColor(this, android.R.color.black))

                    val layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                    layoutParams.setMargins(0, 0, 0, resources.getDimensionPixelSize(R.dimen.espacio_entre_botones)) // Establecer el margen inferior
                    btnProyecto.layoutParams = layoutParams

                    btnProyecto.setOnClickListener {
                        // Al hacer clic en el botón, abrir Activity8_VentanaProyecto
                        val intent = Intent(this, Activity8_VentanaProyecto::class.java)
                        intent.putExtra("proyectoId", proyectoId)
                        startActivity(intent)
                    }

                    // Agregar el botón al LinearLayout
                    linearLayoutProyectos.addView(btnProyecto)
                }
            }
            .addOnFailureListener { e ->
                // Manejar errores al consultar documentos
                e.printStackTrace()
            }

    }

    override fun onBackPressed() {
        return
    }
}