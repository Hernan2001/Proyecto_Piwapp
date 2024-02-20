package com.example.proyecto_piwapp_1_0

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore

class Activity7__Usuario : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth;
    private lateinit var firebaseFirestore: FirebaseFirestore;
    private lateinit var currentUser: FirebaseUser;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_activity7_usuario)

        firebaseAuth = Firebase.auth
        firebaseFirestore = FirebaseFirestore.getInstance()
        currentUser = FirebaseAuth.getInstance().currentUser!!

        val imgbtnEditarUsuario= findViewById<ImageView>(R.id.img_btnEditarUsuario)
        imgbtnEditarUsuario.setOnClickListener {
            val imgbtnUinicio= Intent(this,ActivityUperfilEdicion::class.java);
            startActivity(imgbtnUinicio)
        }


        val img_btnUinicio= findViewById<ImageView>(R.id.img_btnUinicio)
        val img_btnUcrear= findViewById<ImageView>(R.id.img_btnUcrear)
        val img_btnUusuario= findViewById<ImageView>(R.id.img_btnUusuario)



        img_btnUinicio.setOnClickListener {
            val img_btnUinicio= Intent(this,Activity5__Inicio::class.java);
            startActivity(img_btnUinicio)
            Log.i("Actividad_manual","Boton1 (pagina_inicio) pulsado -pagina_inicio")
        }

        img_btnUcrear.setOnClickListener {
            val img_btnUcrear= Intent(this,Activity6__Crear::class.java);
            startActivity(img_btnUcrear)
            Log.i("Actividad_manual","Boton2 (pagina_crear) pulsado -pagina_crear")
        }

        img_btnUusuario.setOnClickListener {
            val img_btnUusuario= Intent(this,Activity7__Usuario::class.java);
            startActivity(img_btnUusuario)
            Log.i("Actividad_manual","Boton3 (pagina_usuario) pulsado -pagina_usuario")
        }

        val EventCerrarSesion= findViewById<TextView>(R.id.txt_btnCerrarSesion)
        EventCerrarSesion.setOnClickListener {
            CerrarSesion()
        }

        val usuarioNombF= findViewById<TextView>(R.id.txt_usuarioNombre)
        val txt_btnPerfil= findViewById<TextView>(R.id.txt_btnPerfil)

        // Obtener el UID del usuario actual
        val uid = currentUser.uid

        usuarioNombF.setOnClickListener {
            val txt_usuarioNomb= Intent(this,ActivityUperfil::class.java);
            startActivity(txt_usuarioNomb)
            Log.i("Actividad_manual","Boton3 (txt_nombreusuario) pulsado -txt_nombreusuario")
        }
        txt_btnPerfil.setOnClickListener {
            val txt_btnPerfil= Intent(this,ActivityUperfil::class.java);
            startActivity(txt_btnPerfil)
            Log.i("Actividad_manual","Boton3 (txt_btnNombreusuario) pulsado -txt_btnNombreusuario")
        }


        // Consultar el documento del usuario en Firestore
        firebaseFirestore.collection("usuarios").document(uid)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    // Obtener el nombre del documento
                    val nombre = documentSnapshot.getString("nombre")

                    // Mostrar el nombre en la vista correspondiente
                    usuarioNombF.text = nombre
                } else {
                    // Manejar el caso en el que el documento no existe
                    Toast.makeText(this, "Error: Documento no encontrado", Toast.LENGTH_SHORT).show()
                    finish() // Cierra la actividad si el documento no existe
                }
            }
            .addOnFailureListener { e ->
                e.printStackTrace()
                // Manejar el caso de un fallo en la lectura
                Toast.makeText(this, "Error al leer datos", Toast.LENGTH_SHORT).show()
                finish() // Cierra la actividad en caso de fallo
            }

        // Resto del código...
    }

    private fun CerrarSesion() {
        // Asegúrate de que firebaseAuth esté inicializado correctamente
        firebaseAuth = Firebase.auth
        firebaseAuth.signOut()

        val intentCerrarSesion = Intent(this, Activity3_Login::class.java)
        startActivity(intentCerrarSesion)
        Toast.makeText(baseContext, "Cerró Sesión exitoso", Toast.LENGTH_SHORT).show()
    }

}