package com.example.myjournal

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class Login: AppCompatActivity(){

    lateinit var login: Button
    lateinit var loginReg: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_main)

        login = findViewById(R.id.loginButton)
        loginReg = findViewById(R.id.loginRegisterButton)

        login.setOnClickListener {
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }
        loginReg.setOnClickListener{
            startActivity(Intent(this,Register::class.java))
            finish()
        }
    }
}