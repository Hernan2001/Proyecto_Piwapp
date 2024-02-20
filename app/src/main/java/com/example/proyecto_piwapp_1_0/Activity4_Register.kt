package com.example.proyecto_piwapp_1_0

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.AttachedSurfaceControl
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.proyecto_piwapp_1_0.databinding.ActivityActivity4RegisterBinding
import com.example.proyecto_piwapp_1_0.databinding.ActivityActivity5InicioBinding
import com.example.proyecto_piwapp_1_0.databinding.ActivityActivity8VentanaProyectoBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.auth

//import com.google.firebase.database.DataSnapshot
//import com.google.firebase.database.DatabaseError
//import com.google.firebase.database.DatabaseReference
//import com.google.firebase.database.FirebaseDatabase
//import com.google.firebase.database.ValueEventListener
//import java.security.Provider

class Activity4_Register : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth;
    private lateinit var authStateListener: FirebaseAuth.AuthStateListener;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_activity4_register)

        val btnRegistro: Button= findViewById(R.id.btn_registerRegistrar)
        val txtemail: EditText= findViewById(R.id.txt_registEmail)
        val txtpassword: EditText= findViewById(R.id.txt_registPassword)
        val txtpasswordVerif: EditText= findViewById(R.id.txt_registPasswordVerif)

        val registerAlogin: TextView = findViewById(R.id.txt_registerLogin)
        registerAlogin.setOnClickListener {
            val registerAlogin= Intent(this,Activity3_Login::class.java);
            startActivity(registerAlogin)
        }

        firebaseAuth= FirebaseAuth.getInstance()
        //firebaseAuth=Firebase.auth;

        btnRegistro.setOnClickListener(){
            //val eEmail= txtemail.text.toString()
            //val ePassword= txtpassword.text.toString()
            //val ePasswordVerif= txtpasswordVerif.text.toString()
            RegistroUser(txtemail.text.toString(),txtpassword.text.toString());
            /*
            if(eEmail.isNotEmpty() && ePassword.isNotEmpty() && ePasswordVerif.isNotEmpty()){
                if(ePassword == ePasswordVerif){
                    RegistroUser(txtemail.text.toString(),txtpassword.text.toString());
                }else{
                    Toast.makeText(baseContext,"Contraseña diferente", Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(baseContext,"Datos incompletos", Toast.LENGTH_SHORT).show()
            }
             */

        }
    }



    private fun RegistroUser(email: String, password: String) {
        val emailEditText: EditText = findViewById(R.id.txt_registEmail)
        val passwordEditText: EditText = findViewById(R.id.txt_registPassword)
        val passwordVerifEditText: EditText = findViewById(R.id.txt_registPasswordVerif)

        if (email.isEmpty()) {
            emailEditText.error = "El correo electrónico no puede estar vacío"
            return
        }
        if (!email.contains("@")) {
            emailEditText.error = "Añadir @ e información correcta"
            return
        }

        if (password.isEmpty()) {
            passwordEditText.error = "El password no puede estar vacío"
            return
        }
        if (password.length < 6) {
            passwordEditText.error = "Tener al menos 6 caracteres en el password"
            return
        }

        if (!password.all { it.isLetterOrDigit() }) {
            passwordEditText.error = "Simbolos no son acpetados"
            return
        }
        /*
        if (password.isEmpty() || password.length < 6 || !password.isLetterOrDigit()) {
            passwordEditText.error = "La contraseña debe tener al menos 6 caracteres alfanuméricos"
            return
        }
         */

        if (passwordVerifEditText.text.toString() != password) {
            passwordVerifEditText.error = "Las contraseñas no coinciden"
            return
        }

        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    EmailVerificacion()
                    Toast.makeText(baseContext, "Registro exitoso", Toast.LENGTH_SHORT).show()
                    val btnLogin = Intent(this, ActivityUperfilEdicion::class.java)
                    startActivity(btnLogin)
                } else {
                    val errorCode = (task.exception as FirebaseAuthException).errorCode
                    if (errorCode == "ERROR_EMAIL_ALREADY_IN_USE") {
                        Toast.makeText(baseContext, "Correo email ya existe", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(baseContext, "Error al registrar", Toast.LENGTH_SHORT).show()
                    }
                }
            }


    }
    private fun EmailVerificacion(){
        val user= firebaseAuth.currentUser!!
        user.sendEmailVerification().addOnCompleteListener(this){task->
            if (task.isSuccessful){

            }else{

            }
        }

    }


}