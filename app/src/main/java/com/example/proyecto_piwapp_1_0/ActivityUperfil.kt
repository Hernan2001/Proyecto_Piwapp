package com.example.proyecto_piwapp_1_0

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class ActivityUperfil : AppCompatActivity() {
    private lateinit var firebaseFirestore: FirebaseFirestore;
    private lateinit var currentUser: FirebaseUser;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_activityuperfil)

        firebaseFirestore = FirebaseFirestore.getInstance()
        currentUser = FirebaseAuth.getInstance().currentUser!!

        // Referencias a las vistas
        val nombreF = findViewById<TextView>(R.id.txt_nombreU)
        val direccionF = findViewById<TextView>(R.id.txt_direccionU)
        val generoF = findViewById<TextView>(R.id.txt_generoU)
        val nacimientoF = findViewById<TextView>(R.id.txt_nacimientoU)
        val paisF = findViewById<TextView>(R.id.txt_paisU)
        val departamentoF = findViewById<TextView>(R.id.txt_departamentoU)
        val provinciaF = findViewById<TextView>(R.id.txt_provinciaU)
        val distritoF = findViewById<TextView>(R.id.txt_distritoU)

        // Obtener el UID del usuario actual
        val uid = currentUser.uid

        // Consultar el documento del usuario en Firestore
        firebaseFirestore.collection("usuarios").document(uid)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    // Obtener los datos del documento
                    val nombre = documentSnapshot.getString("nombre")
                    val direccion = documentSnapshot.getString("direccion")
                    val genero = documentSnapshot.getString("genero")
                    val fechaNacimiento = documentSnapshot.getString("fechnacimiento")
                    val pais = documentSnapshot.getString("pais")
                    val departamento = documentSnapshot.getString("departamento")
                    val provincia = documentSnapshot.getString("provincia")
                    val distrito = documentSnapshot.getString("distrito")

                    // Mostrar los datos en las vistas
                    nombreF.text = nombre
                    direccionF.text = direccion
                    generoF.text = genero
                    nacimientoF.text = fechaNacimiento
                    paisF.text = pais
                    departamentoF.text = departamento
                    provinciaF.text = provincia
                    distritoF.text = distrito
                } else {
                    // Manejar el caso en el que el documento no existe
                    Toast.makeText(this, "BD no encontrado", Toast.LENGTH_SHORT).show()
                    finish() // Cierra la actividad si el documento no existe
                }
            }
            .addOnFailureListener { e ->
                e.printStackTrace()
                // Manejar el caso de un fallo en la lectura
                Toast.makeText(this, "Acceso fallido", Toast.LENGTH_SHORT).show()
                finish() // Cierra la actividad en caso de fallo
            }
    }
}