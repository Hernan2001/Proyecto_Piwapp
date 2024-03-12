package com.example.proyecto_piwapp_1_0

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.proyecto_piwapp_1_0.databinding.ActivityActivity5InicioBinding
import com.example.proyecto_piwapp_1_0.databinding.ActivityActivity6CrearBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import kotlinx.coroutines.newSingleThreadContext
import java.io.ByteArrayOutputStream


class Activity6__Crear : AppCompatActivity() {
    private lateinit var binding: ActivityActivity6CrearBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseFirestore: FirebaseFirestore
    private lateinit var currentUser: FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityActivity6CrearBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        firebaseFirestore = FirebaseFirestore.getInstance()
        currentUser = firebaseAuth.currentUser!!

        val img_btnCinicio= findViewById<ImageView>(R.id.img_btnCinicio)
        val img_btnCcrear= findViewById<ImageView>(R.id.img_btnCcrear)
        val img_btnCusuario= findViewById<ImageView>(R.id.img_btnCusuario)

        var btncrearProyecto: Button= findViewById(R.id.btn_crearProyecto)

        img_btnCinicio.setOnClickListener {
            val img_btnCinicio= Intent(this,Activity5__Inicio::class.java)
            startActivity(img_btnCinicio)
            Log.i("Actividad_manual","Boton1 (pagina_inicio) pulsado -pagina_inicio")
        }
        img_btnCcrear.setOnClickListener {
            val img_btnCcrear= Intent(this,Activity6__Crear::class.java)
            startActivity(img_btnCcrear)
            Log.i("Actividad_manual","Boton2 (pagina_crear) pulsado -pagina_crear")
        }
        img_btnCusuario.setOnClickListener {
            val img_btnCusuario= Intent(this,Activity7__Usuario::class.java)
            startActivity(img_btnCusuario)
            Log.i("Actividad_manual","Boton3 (pagina_usuario) pulsado -pagina_usuario")
        }

        btncrearProyecto.setOnClickListener {
            val nombreProyecto = binding.txtNombreProyecto.text.toString()
            val phoneNumber = binding.txtNumCelular.text.toString()
            val message = binding.txtMsjprincipal.text.toString()

            if (nombreProyecto.isEmpty() || phoneNumber.isEmpty() || message.isEmpty()) {
                Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            verificarNombreProyecto(nombreProyecto) { nombreProyectoUnico ->
                if (nombreProyectoUnico) {
                    guardarDatosEnFirestore(nombreProyecto, phoneNumber, message)
                } else {
                    Toast.makeText(this, "Proyecto existente, escriba otro nombre", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun verificarNombreProyecto(nombreProyecto: String, onComplete: (Boolean) -> Unit) {
        val uid = firebaseAuth.currentUser?.uid
        if (uid != null) {
            firebaseFirestore.collection("proyectos").document(uid).collection("proyectos")
                .whereEqualTo("nombreProyecto", nombreProyecto)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    onComplete(querySnapshot.isEmpty)
                }
                .addOnFailureListener { e ->
                    e.printStackTrace()
                    onComplete(false)
                }
        } else {
            onComplete(false)
        }
    }

    private fun guardarDatosEnFirestore(
        nombreProyecto: String,
        phoneNumber: String,
        message: String
    ) {
        val whatsappLink = "https://wa.me/51$phoneNumber?${Uri.encode(message)}"

        try {
            val barcodeEncoder = BarcodeEncoder()
            val bitmap = barcodeEncoder.encodeBitmap(
                whatsappLink,
                BarcodeFormat.QR_CODE,
                750,
                750
            )

            // Convertir la imagen del QR a cadena Base64
            val imgBase64 = bitmapToBase64(bitmap)

            val userData = hashMapOf(
                "nombreProyecto" to nombreProyecto,
                "phoneNumber" to phoneNumber,
                "message" to message,
                "whatsappLink" to whatsappLink,
                "imgBase64" to imgBase64
            )

            val uid = firebaseAuth.currentUser?.uid
            if (uid != null) {
                firebaseFirestore.collection("proyectos").document(uid).collection("proyectos")
                    .add(userData)
                    .addOnSuccessListener {
                        // Éxito al guardar los datos
                        val intent = Intent(this, Activity5__Inicio::class.java)
                        startActivity(intent)
                    }
                    .addOnFailureListener { e ->
                        // Manejar errores al guardar los datos
                        e.printStackTrace()
                    }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun bitmapToBase64(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }
}