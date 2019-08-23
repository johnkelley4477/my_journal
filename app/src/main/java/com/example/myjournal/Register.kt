package com.example.myjournal

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class Register: AppCompatActivity() {
    private lateinit var regUserText: EditText
    private lateinit var regPasswordText: EditText
    private lateinit var regConfirmPasswordText: EditText
    private lateinit var regButton: Button
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_main)
        auth = FirebaseAuth.getInstance()
        regUserText = findViewById(R.id.regUserText)
        regPasswordText = findViewById(R.id.regPasswordText)
        regConfirmPasswordText = findViewById(R.id.regConfirmPasswordText)
        regButton = findViewById(R.id.regButton)
        regButton.setOnClickListener{
            signUpUser()
        }
    }

    fun signUpUser(){

        if(regUserText.text.toString().isEmpty()){
            regUserText.error = "Please enter an email"
            regUserText.requestFocus()
            return
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(regUserText.text.toString()).matches()){
            regUserText.error = "Please enter a valid email"
            regUserText.requestFocus()
            return
        }
        if(regPasswordText.text.toString().isEmpty()){
            regPasswordText.error = "Please enter a password"
            regPasswordText.requestFocus()
            return
        }
        if(regConfirmPasswordText.text.toString().isEmpty()){
            regConfirmPasswordText.error = "Please confirm your password"
            regConfirmPasswordText.requestFocus()
            return
        }
        if(!regConfirmPasswordText.text.toString().equals(regPasswordText.text.toString())){
            regConfirmPasswordText.error = "Your passwords do not match"
            regConfirmPasswordText.requestFocus()
            return
        }
        auth.createUserWithEmailAndPassword(regUserText.text.toString(), regPasswordText.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Authentication was successful.",
                        Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this,MainActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }
}