package com.example.myjournal

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class Login: AppCompatActivity(){

    lateinit var login: Button
    lateinit var loginReg: Button
    lateinit var userText: EditText
    lateinit var passwordText: EditText
    private lateinit var auth: FirebaseAuth
    private val TAG: String = "Login"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_main)
        auth = FirebaseAuth.getInstance()
        userText = findViewById(R.id.userText)
        passwordText = findViewById(R.id.passwordText)
        login = findViewById(R.id.loginButton)
        loginReg = findViewById(R.id.loginRegisterButton)

        login.setOnClickListener {
            login()
        }
        loginReg.setOnClickListener{
            startActivity(Intent(this,Register::class.java))
            finish()
        }
    }

    override fun onStart(){
        super.onStart()
        val currentUser: FirebaseUser? = auth.currentUser
        userState(currentUser)

        //ToDo room this Logging
        Log.d(TAG,"current user is: " + currentUser)
    }

    fun login(){
        if(userText.text.toString().isEmpty()){
            userText.error = "Please enter an email"
            userText.requestFocus()
            return
        }
        if(passwordText.text.toString().isEmpty()){
            passwordText.error = "Please enter a password"
            passwordText.requestFocus()
            return
        }

        auth.signInWithEmailAndPassword(userText.text.toString(), passwordText.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    userState(user)
                } else {
                    userState(null)
                }
            }
    }

    fun userState(currentUser: FirebaseUser?){
        //Todo remove this Logging
        Log.d(TAG,"current user is: " + currentUser)

        if(currentUser != null){
            if(currentUser.isEmailVerified) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }else{
                Toast.makeText(baseContext, "Please verify your email address",
                    Toast.LENGTH_SHORT).show()
            }
        }else{
            Toast.makeText(baseContext, "Authentication failed.",
                Toast.LENGTH_SHORT).show()
        }
    }
}