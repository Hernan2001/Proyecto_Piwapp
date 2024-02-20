package com.example.proyecto_piwapp_1_0

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.proyecto_piwapp_1_0.databinding.ActivityActivity3LoginBinding
import com.example.proyecto_piwapp_1_0.databinding.ActivityActivity4RegisterBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore


class Activity3_Login : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth;
    private lateinit var authStateListener: FirebaseAuth.AuthStateListener;
    private lateinit var firebaseFirestore: FirebaseFirestore;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_activity3_login)

        val btnLogin: Button = findViewById(R.id.btn_loginIniciarSesion)
        val txtemail: EditText = findViewById(R.id.txt_loginEmail)
        val txtpassword: EditText = findViewById(R.id.txt_loginPassword)

        //Ir a la ventana registro
        val loginAregister: TextView = findViewById(R.id.txt_loginCrearCuenta)
        loginAregister.setOnClickListener {
            val loginAregister= Intent(this,Activity4_Register::class.java);
            startActivity(loginAregister)
        }

        firebaseAuth = Firebase.auth;

        btnLogin.setOnClickListener {
            Login(txtemail.text.toString(), txtpassword.text.toString())
            //Log.i("Actividad_manual","Boton1 (iniciar_sesion) pulsado -iniciar_sesion")
            //Log.i("Actividad_manual","Boton1 (iniciar_sesion) pulsado -${txt_loginEmail.text.toString()}")
            //Log.i("Actividad_manual","Boton1 (iniciar_sesion) pulsado -${txt_loginPassword.text.toString()}")
        }
    }


    private fun Login(email: String, password: String) {
        val emailEditText: EditText = findViewById(R.id.txt_loginEmail)
        val passwordEditText: EditText = findViewById(R.id.txt_loginPassword)

        if (email.isEmpty()) {
            emailEditText.error = "El correo electrónico no puede estar vacío"
            return
        }

        if (!email.contains("@")) {
            emailEditText.error = "Añadir @ e información correcta"
            return
        }

        if (password.isEmpty()) {
            passwordEditText.error = "La contraseña no puede estar vacía"
            return
        }

        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = firebaseAuth.currentUser
                    val emailVerified = user?.isEmailVerified

                    if (emailVerified == true) {
                        val db = FirebaseFirestore.getInstance()
                        val userRef = db.collection("usuarios").document(user?.uid.toString())

                        userRef.get()
                            .addOnSuccessListener { documentSnapshot ->
                                if (documentSnapshot.exists()) {
                                    val nombre = documentSnapshot.getString("nombre")
                                    if (!nombre.isNullOrEmpty()) {
                                        val intent = Intent(this, Activity5__Inicio::class.java)
                                        startActivity(intent)
                                    } else {
                                        val intent = Intent(this, ActivityUperfilEdicion::class.java)
                                        startActivity(intent)
                                    }
                                } else {
                                    val intent = Intent(this, ActivityUperfilEdicion::class.java)
                                    startActivity(intent)
                                }
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(baseContext, "Error al obtener datos: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                    } else {
                        Toast.makeText(baseContext, "Falta verificar su correo electrónico", Toast.LENGTH_SHORT).show()
                    }

                } else {
                    Toast.makeText(baseContext, "Login incorrecto", Toast.LENGTH_SHORT).show()
                }
            }
    }



}