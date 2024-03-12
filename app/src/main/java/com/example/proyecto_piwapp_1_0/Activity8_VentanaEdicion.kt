package com.example.proyecto_piwapp_1_0

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class Activity8_VentanaEdicion : AppCompatActivity() {
    private lateinit var firebaseFirestore: FirebaseFirestore;
    private lateinit var currentUser: FirebaseUser;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_activity8_ventana_edicion)

        firebaseFirestore = FirebaseFirestore.getInstance();
        currentUser = FirebaseAuth.getInstance().currentUser!!;

        //Mostrar desde Firestore
        val nombreProyectoC = findViewById<TextView>(R.id.txt_nombreProyectoEdit)
        val phoneNumberC = findViewById<TextView>(R.id.txt_numCelEdit)
        val messageC = findViewById<TextView>(R.id.txt_mensajeEdit)
        val LinkGeneradoC = findViewById<TextView>(R.id.txt_linkEdit)
        val mostradorQrC = findViewById<ImageView>(R.id.img_qrEdit)
        //Editar descripcion
        val descripcionF = findViewById<EditText>(R.id.txt_descripcionEdit)

        // Obtener el UID del usuario actual
        val uid = currentUser.uid

        val btnborrarProyecto = findViewById<Button>(R.id.btn_borrarProyecto)
        val cancelarEdicionA8 = findViewById<Button>(R.id.btn_cancelarEdicionA8)
        val guardarEdicionA8 = findViewById<Button>(R.id.btn_guardarEdicionA8)

        cancelarEdicionA8.setOnClickListener {
            val cancelarEdicion = Intent(this, Activity8_VentanaProyecto::class.java);
            startActivity(cancelarEdicion)
        }

        // Obtener el proyectoId del Intent
        val proyectoId = intent.getStringExtra("proyectoId")


        if (proyectoId != null) {
            firebaseFirestore.collection("proyectos").document(uid)
                .collection("proyectos").document(proyectoId)
                .get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        val nombreProyecto = documentSnapshot.getString("nombreProyecto")
                        val phoneNumber = documentSnapshot.getString("phoneNumber")
                        val message = documentSnapshot.getString("message")
                        val imgBase64 = documentSnapshot.getString("imgBase64")
                        val descripcion = documentSnapshot.getString("descripcion")

                        nombreProyectoC.text = nombreProyecto
                        phoneNumberC.text = phoneNumber
                        messageC.text = message
                        LinkGeneradoC.text = "https://wa.me/51$phoneNumber?$message"
                        descripcionF.setText(descripcion)

                        val bitmap = base64ToBitmap(imgBase64)
                        mostradorQrC.setImageBitmap(bitmap)

                        btnborrarProyecto.setOnClickListener {
                            borrarProyecto(uid, proyectoId)

                        }

                    }else {
                        // Aquí puedes manejar el caso en el que el documento no existe
                        Toast.makeText(this, "Error: Documento no encontrado", Toast.LENGTH_SHORT).show()
                        finish() // Cierra la actividad si el documento no existe
                    }
                }
                .addOnFailureListener { e ->
                    e.printStackTrace()
                    // Aquí puedes manejar el caso de un fallo en la lectura
                    Toast.makeText(this, "Error al leer datos", Toast.LENGTH_SHORT).show()
                    finish() // Cierra la actividad en caso de fallo
                }
        }else {
            // Aquí puedes manejar el caso en el que proyectoId sea nulo
            // Por ejemplo, mostrar un mensaje de error o volver a la pantalla anterior
            Toast.makeText(this, "ProyectoId nulo", Toast.LENGTH_SHORT).show()
            finish() // Cierra la actividad si proyectoId es nulo
        }

        guardarEdicionA8.setOnClickListener {
            if (proyectoId != null) {
                val nuevaDescripcion = descripcionF.text.toString()

                firebaseFirestore.collection("proyectos").document(uid)
                    .collection("proyectos").document(proyectoId)
                    .update("descripcion", nuevaDescripcion)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Guardado", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener { e ->
                        e.printStackTrace()
                        Toast.makeText(this, "Error al guardar", Toast.LENGTH_SHORT).show()
                    }
            }
        }

    }


    private fun base64ToBitmap(base64String: String?): Bitmap? {
        return try {
            val decodedBytes = Base64.decode(base64String, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun borrarProyecto(uid: String, proyectoId: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Confirmar")
        builder.setMessage("¿Estás seguro de que quieres borrar este proyecto?")

        builder.setPositiveButton("Si") { _, _ ->
            firebaseFirestore.collection("proyectos").document(uid)
                .collection("proyectos").document(proyectoId)
                .delete()
                .addOnSuccessListener {
                    Toast.makeText(this, "Proyecto eliminado", Toast.LENGTH_SHORT).show()
                    finish()
                    val borrarProyectoV = Intent(this, Activity5__Inicio::class.java);
                    startActivity(borrarProyectoV)

                }
                .addOnFailureListener { e ->
                    e.printStackTrace()
                    Toast.makeText(this, "Error al borrar", Toast.LENGTH_SHORT).show()
                }
        }

        builder.setNegativeButton("No") { _, _ ->
            // No hacer nada, el usuario decidió no borrar
        }

        val dialog = builder.create()
        dialog.show()
    }
}