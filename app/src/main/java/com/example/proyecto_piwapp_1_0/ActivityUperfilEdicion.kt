package com.example.proyecto_piwapp_1_0

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore

class ActivityUperfilEdicion : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth;
    private lateinit var firebaseFirestore: FirebaseFirestore
    private lateinit var currentUser: FirebaseUser
    private var lastBackPressTime = 0L
    private val backPressThreshold = 2000 // Tiempo en milisegundos


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_uperfil_edicion)

        val nombreU: EditText = findViewById(R.id.txt_nombreU)
        val direccionU: EditText = findViewById(R.id.txt_direccionU)
        val generoU: EditText = findViewById(R.id.txt_generoU)
        val fechnacimientoU: EditText = findViewById(R.id.txt_fechnacimientoU)
        val paisU: EditText = findViewById(R.id.txt_paisU)
        val departamentoU: EditText = findViewById(R.id.txt_departamentoU)
        val provinciaU: EditText = findViewById(R.id.txt_provinciaU)
        val distritoU: EditText = findViewById(R.id.txt_distritoU)

        val btnguardarEdicion: Button = findViewById(R.id.btn_guardarEdicion)
        val btncancelarEdicion: Button = findViewById(R.id.btn_cancelarEdicion)

        firebaseAuth = FirebaseAuth.getInstance()
        firebaseFirestore = FirebaseFirestore.getInstance()
        currentUser = firebaseAuth.currentUser!!

        // Obtener el UID del usuario actual
        val uid = currentUser.uid

        // Obtener los datos del usuario desde Firestore
        firebaseFirestore.collection("usuarios").document(uid)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val userData = documentSnapshot.data
                    nombreU.setText(userData?.get("nombre") as? String ?: "")
                    direccionU.setText(userData?.get("direccion") as? String ?: "")
                    generoU.setText(userData?.get("genero") as? String ?: "")
                    fechnacimientoU.setText(userData?.get("fechnacimiento") as? String ?: "")
                    paisU.setText(userData?.get("pais") as? String ?: "")
                    departamentoU.setText(userData?.get("departamento") as? String ?: "")
                    provinciaU.setText(userData?.get("provincia") as? String ?: "")
                    distritoU.setText(userData?.get("distrito") as? String ?: "")
                }
            }
            .addOnFailureListener { e ->
                e.printStackTrace()
                Toast.makeText(
                    baseContext,
                    "Error al obtener los datos del usuario",
                    Toast.LENGTH_SHORT
                ).show()
            }

        btnguardarEdicion.setOnClickListener {
            val nombre = nombreU.text.toString()
            val direccion = direccionU.text.toString()
            val genero = generoU.text.toString()
            val fechnacimiento = fechnacimientoU.text.toString()
            val pais = paisU.text.toString()
            val departamento = departamentoU.text.toString()
            val provincia = provinciaU.text.toString()
            val distrito = distritoU.text.toString()

            if (nombre.isEmpty() || direccion.isEmpty() || genero.isEmpty() || fechnacimiento.isEmpty() ||
                pais.isEmpty() || departamento.isEmpty() || provincia.isEmpty() || distrito.isEmpty()) {
                Toast.makeText(baseContext, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            AlertDialog.Builder(this)
                .setMessage("¿Está seguro de que desea guardar los cambios?")
                .setPositiveButton("Guardar") { _, _ ->
                    // Crear un mapa con los datos a guardar en Firestore
                    val userData = hashMapOf(
                        "nombre" to nombre,
                        "direccion" to direccion,
                        "genero" to genero,
                        "fechnacimiento" to fechnacimiento,
                        "pais" to pais,
                        "departamento" to departamento,
                        "provincia" to provincia,
                        "distrito" to distrito
                    )

                    // Guardar los datos en Firestore
                    firebaseFirestore.collection("usuarios").document(uid)
                        .set(userData)
                        .addOnSuccessListener {
                            Toast.makeText(baseContext, "Guardado", Toast.LENGTH_SHORT).show()
                            // Éxito al guardar los datos
                            // Puedes agregar aquí cualquier acción adicional después de guardar los datos
                        }
                        .addOnFailureListener { e ->
                            // Manejar errores al guardar los datos
                            // Puedes imprimir el mensaje de error o tomar acciones específicas
                            e.printStackTrace()
                            Toast.makeText(baseContext, "Guardado fallido", Toast.LENGTH_SHORT).show()
                        }
                }
                .setNegativeButton("Cancelar", null)
                .show()
        }

        btncancelarEdicion.setOnClickListener {
            if (nombreU.text.isEmpty()) {
                nombreU.error = "Por favor complete este campo"
                return@setOnClickListener
            }
            if (direccionU.text.isEmpty()) {
                direccionU.error = "Por favor complete este campo"
                return@setOnClickListener
            }
            if (generoU.text.isEmpty()) {
                generoU.error = "Por favor complete este campo"
                return@setOnClickListener
            }
            if (fechnacimientoU.text.isEmpty()) {
                fechnacimientoU.error = "Por favor complete este campo"
                return@setOnClickListener
            }
            if (paisU.text.isEmpty()) {
                paisU.error = "Por favor complete este campo"
                return@setOnClickListener
            }
            if (departamentoU.text.isEmpty()) {
                departamentoU.error = "Por favor complete este campo"
                return@setOnClickListener
            }
            if (provinciaU.text.isEmpty()) {
                provinciaU.error = "Por favor complete este campo"
                return@setOnClickListener
            }
            if (distritoU.text.isEmpty()) {
                distritoU.error = "Por favor complete este campo"
                return@setOnClickListener
            }

            val uid = FirebaseAuth.getInstance().currentUser?.uid
            if (uid != null) {
                firebaseFirestore.collection("usuarios").document(uid)
                    .get()
                    .addOnSuccessListener { documentSnapshot ->
                        if (documentSnapshot.exists()) {
                            val userData = documentSnapshot.data
                            val nombre = userData?.get("nombre") as? String
                            if (nombre != null && nombre.isNotEmpty()) {
                                // Al menos un dato del nombre existe, permitir salir
                                val intent = Intent(this, Activity7__Usuario::class.java)
                                startActivity(intent)
                            } else {
                                // No se encontró un dato del nombre en Firestore, mostrar mensaje
                                Toast.makeText(baseContext, "No se encontró un dato del nombre en Firestore", Toast.LENGTH_SHORT).show()
                            }
                        }
                        else {
                            // No se encontró un dato del nombre en Firestore, mostrar mensaje
                            Toast.makeText(baseContext, "Primero debes guardar todos los campos", Toast.LENGTH_SHORT).show()
                        }

                    }
                    .addOnFailureListener { e ->
                        e.printStackTrace()
                        Toast.makeText(baseContext, "Error al obtener los datos del usuario", Toast.LENGTH_SHORT).show()
                    }
            } else {
                // No se encontró un usuario autenticado, mostrar mensaje
                Toast.makeText(baseContext, "No se encontró un usuario autenticado", Toast.LENGTH_SHORT).show()
            }

        }


    }

    override fun onBackPressed() {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastBackPressTime > backPressThreshold) {
            Toast.makeText(this, "Presione de nuevo para salir", Toast.LENGTH_SHORT).show()
            lastBackPressTime = currentTime
        } else {
            super.onBackPressed()
        }
    }

}

