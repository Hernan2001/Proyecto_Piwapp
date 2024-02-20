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
    private lateinit var binding: ActivityActivity6CrearBinding;
    private lateinit var firebaseAuth: FirebaseAuth;
    private lateinit var firebaseFirestore: FirebaseFirestore;
    private lateinit var currentUser: FirebaseUser;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_activity6_crear);
        binding= ActivityActivity6CrearBinding.inflate(layoutInflater)
        setContentView(binding.root);

        firebaseAuth = FirebaseAuth.getInstance()
        firebaseFirestore = FirebaseFirestore.getInstance()
        currentUser = firebaseAuth.currentUser!!

        val img_btnCinicio= findViewById<ImageView>(R.id.img_btnCinicio);
        val img_btnCcrear= findViewById<ImageView>(R.id.img_btnCcrear);
        val img_btnCusuario= findViewById<ImageView>(R.id.img_btnCusuario);

        //Boton generador QR
        var btncrearProyecto: Button= findViewById(R.id.btn_crearProyecto);
        //Texto numero celular
        val txtnumCel: EditText = findViewById(R.id.txt_numCelular);
        //Imagen muestra QR
        val imgmostradorQr: ImageView= findViewById(R.id.img_generaQr);

        //val txtmsjprincipal: EditText= findViewById(R.id.txt_msjprincipal);
        val txtlinkGenerado: TextView= findViewById(R.id.txt_linkGenerado);


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

        btncrearProyecto.setOnClickListener {
            Log.i("Actividad_manual","Boton4 (btnCrear) pulsado -btnCrear")
            val nombreProyecto = binding.txtNombreProyecto.text.toString()
            val phoneNumber = binding.txtNumCelular.text.toString()
            val message = binding.txtMsjprincipal.text.toString()

            // Concatena "Watsap//" antes del número de teléfono
            val whatsappLink = "https://wa.me/51$phoneNumber?${Uri.encode(message)}"

            // Muestra el enlace de WhatsApp en un TextView
            binding.txtLinkGenerado.text = whatsappLink

            try {
                var barcodeEncoder: BarcodeEncoder= BarcodeEncoder();
                var bitmap: Bitmap= barcodeEncoder.encodeBitmap(
                    whatsappLink,
                    BarcodeFormat.QR_CODE,
                    750,
                    750
                )
                imgmostradorQr.setImageBitmap(bitmap)

                // Convertir la imagen del QR a cadena Base64
                val imgBase64 = bitmapToBase64(bitmap)

                // Obtener un identificador único (documentId) para el proyecto
                val proyectoId = firebaseFirestore.collection("proyectos").document().id

                // Guardar datos en Firestore con un nuevo documento
                guardarDatosEnFirestore(proyectoId, nombreProyecto, phoneNumber, message, whatsappLink, imgBase64)

            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }


    private fun bitmapToBase64(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    private fun guardarDatosEnFirestore(
        proyectoId: String,
        nombreProyecto: String,
        phoneNumber: String,
        message: String,
        whatsappLink: String,
        imgBase64: String
    ) {
        val userData = hashMapOf(
            "nombreProyecto" to nombreProyecto,
            "phoneNumber" to phoneNumber,
            "message" to message,
            "whatsappLink" to whatsappLink,
            "imgBase64" to imgBase64
        )

        // Obtener el UID del usuario actual
        val uid = currentUser.uid

        // Guardar los datos en Firestore con un nuevo documento
        firebaseFirestore.collection("proyectos").document(uid).collection("proyectos").document(proyectoId)
            .set(userData)
            .addOnSuccessListener {
                // Éxito al guardar los datos
                // Puedes agregar aquí cualquier acción adicional después de guardar los datos
            }
            .addOnFailureListener { e ->
                // Manejar errores al guardar los datos
                // Puedes imprimir el mensaje de error o tomar acciones específicas
                e.printStackTrace()
            }
    }

}