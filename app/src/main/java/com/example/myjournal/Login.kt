package com.example.myjournal

import android.content.Context
import android.view.View

class Login (){

    fun login(entryView: View, loginView: View){
        entryView.setVisibility(View.VISIBLE)
        loginView.setVisibility(View.GONE)
    }
}