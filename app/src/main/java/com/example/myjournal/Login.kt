package com.example.myjournal

import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class Login: AppCompatActivity(){

    lateinit var login: Button
    lateinit var loginReg: Button

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)


        login = findViewById(R.id.loginButton)
        loginReg = findViewById(R.id.loginRegisterButton)

        login.setOnClickListener {

        }
        loginReg.setOnClickListener{

        }
    }
}