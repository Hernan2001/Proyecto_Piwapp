package com.example.proyecto_piwapp_1_0

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Base64
import android.widget.Toast
import androidx.core.content.FileProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageMetadata
import com.google.firebase.storage.StorageReference
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class Activity8_VentanaProyecto : AppCompatActivity() {
    private lateinit var firebaseFirestore: FirebaseFirestore;
    private lateinit var currentUser: FirebaseUser;
    private lateinit var storageReference: StorageReference
    private lateinit var pdfUri: Uri
    private lateinit var pathLinkF: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_activity8_ventana_proyecto)

        firebaseFirestore = FirebaseFirestore.getInstance();
        currentUser = FirebaseAuth.getInstance().currentUser!!;
        // Obtener el UID del usuario actual
        val uid = currentUser.uid
        // Inicializar StorageReference
        storageReference = FirebaseStorage.getInstance().reference

        // Obtener el proyectoId del Intent
        val proyectoId = intent.getStringExtra("proyectoId")
        if (proyectoId != null) {
            // Obtener el nombre del archivo PDF almacenado en Cloud Storage
            obtenerNombreArchivoDesdeStorage(proyectoId)
        }

        //boton EDITAR
        val btneditarProyecto = findViewById<Button>(R.id.btn_editarProyecto)

        //Mostrar desde Firestore
        val nombreProyectoF = findViewById<TextView>(R.id.txt_nombreProyectoCop)
        val phoneNumberF = findViewById<TextView>(R.id.txt_numCelCop)
        val messageF = findViewById<TextView>(R.id.txt_mensajeCop)
        val descripcionF = findViewById<TextView>(R.id.txt_descripcion)
        val LinkGeneradoF = findViewById<TextView>(R.id.txt_linkCop)
        val mostradorQrF = findViewById<ImageView>(R.id.img_qrCop)

        pathLinkF = findViewById(R.id.txt_pathLink)
        val btn_buscarFileProyecto = findViewById<Button>(R.id.btn_buscarFileProyecto)
        val btn_abrirFileProyecto = findViewById<Button>(R.id.btn_abrirFileProyecto)
        // Configurar el evento para subir un PDF al presionar el botón btn_buscarFileProyecto
        btn_buscarFileProyecto.setOnClickListener {
            abrirExploradorPDF()
            Toast.makeText(this, "Buscar y Subir PDF", Toast.LENGTH_SHORT).show()
        }
        // Configurar el evento para abrir el PDF al presionar el botón btn_abrirFileProyecto
        btn_abrirFileProyecto.setOnClickListener {
            abrirPDF()
            Toast.makeText(this, "Abrir PDF", Toast.LENGTH_SHORT).show()
        }

        val img_btnPinicio = findViewById<ImageView>(R.id.img_btnPinicio)
        val img_btnPcrear = findViewById<ImageView>(R.id.img_btnPcrear)
        val img_btnPusuario = findViewById<ImageView>(R.id.img_btnPusuario)
        img_btnPinicio.setOnClickListener {
            val img_btnPinicio = Intent(this, Activity5__Inicio::class.java);
            startActivity(img_btnPinicio)
            Log.i("Actividad_manual", "Boton1 (pagina_inicio) pulsado -pagina_inicio")
        }
        img_btnPcrear.setOnClickListener {
            val img_btnPcrear = Intent(this, Activity6__Crear::class.java);
            startActivity(img_btnPcrear)
            Log.i("Actividad_manual", "Boton2 (pagina_crear) pulsado -pagina_crear")
        }
        img_btnPusuario.setOnClickListener {
            val img_btnPusuario = Intent(this, Activity7__Usuario::class.java);
            startActivity(img_btnPusuario)
            Log.i("Actividad_manual", "Boton3 (pagina_usuario) pulsado -pagina_usuario")
        }




        // Obtener el enlace generado del Intent
        val whatsappLink = intent.getStringExtra("whatsappLink")

        //*Evento Compartir texto
        val btnCompartirLink = findViewById<Button>(R.id.btn_compartirLink)
        // LLama función Compartir Link
        btnCompartirLink.setOnClickListener {
            val whatsappLink = LinkGeneradoF.text.toString()
            if (whatsappLink.isNotEmpty()) {
                shareTxt(whatsappLink)
            } else {
                Toast.makeText(this, "LINK no encontrado", Toast.LENGTH_SHORT).show()
            }
        }
        //Compartir imagen
        val btncompartirQr = findViewById<Button>(R.id.btn_compartirQr);
        btncompartirQr.setOnClickListener {
            // Obtener el ImageView como un Drawable
            val drawable = mostradorQrF.drawable

            // Convertir el Drawable a un Bitmap
            val bitmap: Bitmap = when (drawable) {
                is BitmapDrawable -> drawable.bitmap
                else -> Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
            }

            // Guardar el Bitmap en un archivo temporal
            val file = File(this.cacheDir, "qr_image.png")
            try {
                val stream = FileOutputStream(file)
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                stream.flush()
                stream.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

            // Obtener la URI del archivo temporal
            val uri = FileProvider.getUriForFile(this, "${packageName}.provider", file)

            // Crear un intent para compartir la imagen
            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_STREAM, uri)
                type = "image/png"  // Cambia el tipo de archivo según el formato de tu imagen
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

            // Iniciar el selector de aplicaciones para compartir
            startActivity(Intent.createChooser(shareIntent, "Compartir imagen"))
        }



        btneditarProyecto.setOnClickListener {
            // Obtener el proyectoId del Intent
            val proyectoId = intent.getStringExtra("proyectoId")

            if (proyectoId != null) {
                val intentEditar = Intent(this, Activity8_VentanaEdicion::class.java)
                intentEditar.putExtra("proyectoId", proyectoId)
                startActivity(intentEditar)
                Log.i("Actividad_manual", "Boton4 (btnEditar) pulsado -btnEditar")
            } else {
                // Manejar el caso en el que proyectoId sea nulo
                Toast.makeText(this, "Error: proyectoId nulo", Toast.LENGTH_SHORT).show()
            }
        }


        if (proyectoId != null) {
            // Consultar el documento correspondiente al proyectoId en Firestore
            firebaseFirestore.collection("proyectos").document(currentUser.uid)
                .collection("proyectos").document(proyectoId)
                .get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        val nombreProyecto = documentSnapshot.getString("nombreProyecto")
                        val phoneNumber = documentSnapshot.getString("phoneNumber")
                        val message = documentSnapshot.getString("message")
                        val descripcion = documentSnapshot.getString("descripcion")
                        val whatsappLink = documentSnapshot.getString("whatsappLink")
                        val imgBase64 = documentSnapshot.getString("imgBase64")

                        // Asignar los datos a las vistas
                        nombreProyectoF.text = nombreProyecto
                        phoneNumberF.text = phoneNumber
                        messageF.text = message
                        descripcionF.text = descripcion
                        LinkGeneradoF.text = whatsappLink

                        // Convertir imgBase64 a Bitmap y asignarlo a ImageView
                        val bitmap = base64ToBitmap(imgBase64)
                        mostradorQrF.setImageBitmap(bitmap)

                        // Obtener el nombre del archivo PDF almacenado en Cloud Storage
                        obtenerNombreArchivoDesdeStorage(proyectoId)
                    }
                }
                .addOnFailureListener { e ->
                    e.printStackTrace()
                }
        }
    }

    //Compartir LINK
    private fun shareTxt(textToShare: String) {
        val intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, textToShare)
            type = "text/plain"
        }
        startActivity(Intent.createChooser(intent, "Compartir"))
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

    // Método para abrir el explorador de archivos y seleccionar un PDF
    private fun abrirExploradorPDF() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "application/pdf"
        startActivityForResult(intent, PDF_REQUEST_CODE)
    }

    // Método para manejar el resultado de la selección del PDF
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PDF_REQUEST_CODE && resultCode == RESULT_OK) {
            // Obtener la URI del PDF seleccionado
            pdfUri = data?.data!!

            // Mostrar el nombre del archivo en txt_pathLink
            val pdfFileName = obtenerNombreArchivo(pdfUri)
            if (pdfFileName != null) {
                pathLinkF.text = pdfFileName
                subirPDF(pdfFileName)
            } else {
                Toast.makeText(this, "Aviso informativo: Sin subir PDF", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun obtenerNombreArchivo(uri: Uri): String? {
        var displayName: String? = null
        try {
            contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                if (cursor.moveToFirst()) {
                    val index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    if (index != -1) {
                        displayName = cursor.getString(index)
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return displayName
    }



    // Método para subir el PDF a Cloud Storage
    private fun subirPDF(pdfNombre: String) {
        // Obtener el proyectoId del Intent
        val proyectoId = intent.getStringExtra("proyectoId") ?: return

        val pdfRef = storageReference.child("pdfs/$proyectoId.pdf")

        // Crear metadatos con el nombre del archivo
        val metadata = StorageMetadata.Builder()
            .setCustomMetadata("nombreArchivo", pdfNombre)
            .build()

        pdfRef.putFile(pdfUri, metadata)
            .addOnSuccessListener { taskSnapshot ->
                Toast.makeText(this, "PDF subido correctamente", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error al subir PDF: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    // Método para abrir el PDF almacenado en Cloud Storage
    private fun abrirPDF() {
        // Obtener el proyectoId del Intent
        val proyectoId = intent.getStringExtra("proyectoId") ?: return

        val pdfRef = storageReference.child("pdfs/$proyectoId.pdf")

        pdfRef.downloadUrl
            .addOnSuccessListener { uri ->
                // Obtener el nombre del archivo PDF almacenado
                val pdfFileName = obtenerNombreArchivo(uri)

                // Si el nombre del archivo no es null, actualizamos el TextView
                pdfFileName?.let {
                    pathLinkF.text = it
                }

                // Abrir el PDF usando la URI obtenida
                val intent = Intent(Intent.ACTION_VIEW)
                intent.setDataAndType(uri, "application/pdf")
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error al abrir PDF: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    // Método para obtener el nombre del archivo PDF desde Cloud Storage y mostrarlo
    private fun obtenerNombreArchivoDesdeStorage(proyectoId: String) {
        val pdfRef = storageReference.child("pdfs/$proyectoId.pdf")

        pdfRef.metadata
            .addOnSuccessListener { metadata ->
                val pdfFileName = metadata?.getCustomMetadata("nombreArchivo")
                if (pdfFileName != null) {
                    pathLinkF.text = pdfFileName
                } else {
                    // Puedes poner un valor predeterminado o un mensaje alternativo si el nombre es nulo
                    Toast.makeText(this, "Nombre de archivo desconocido", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Aviso informativo: No hay PDF subido", Toast.LENGTH_SHORT).show()
            }
    }

    companion object {
        private const val PDF_REQUEST_CODE = 42
    }


}
