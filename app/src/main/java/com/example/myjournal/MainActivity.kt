package com.example.myjournal

import android.os.Bundle
//import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

import kotlinx.android.synthetic.main.activity_main.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MainActivity : AppCompatActivity() {

    lateinit var dateTextName: EditText
    lateinit var tagsTextName: EditText
    lateinit var entryTextName: EditText
    lateinit var saveButton: Button
    lateinit var login: Button
    lateinit var loginReg: Button
    lateinit var entryView: View
    lateinit var loginView: View
    lateinit var regView: View

    val loginHandler = Login()

    val simpleFormat = DateTimeFormatter.ofPattern("M/d/yy")

    //ToDo relocate this if possible
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
       // setSupportActionBar(toolbar)

        //ToDo relocate this if possible
        auth = FirebaseAuth.getInstance()

        dateTextName = findViewById(R.id.dateText)
        tagsTextName = findViewById(R.id.tagsText)
        entryTextName = findViewById(R.id.entryText)
        saveButton = findViewById(R.id.saveButton)
        login = findViewById(R.id.loginButton)
        loginReg = findViewById(R.id.loginRegisterButton)
        regView = findViewById(R.id.regView)
        entryView = findViewById(R.id.entryView)
        loginView = findViewById(R.id.loginView)

        val today = LocalDateTime.now()
        val formatted = today.format(simpleFormat)
        dateTextName.setText(formatted.toString())

        saveButton.setOnClickListener {
           val entry = Entry()
           entry.saveEntry(dateTextName,tagsTextName,entryTextName,this@MainActivity)
        }
        login.setOnClickListener {
            loginHandler.login(entryView,loginView)
        }
        loginReg.setOnClickListener{
            loginHandler.login(regView, loginView)
        }
    }

    //ToDo relocate this if possible
    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    //ToDo relocate this if possible
    fun updateUI(currentUser: FirebaseUser?){

    }
}
