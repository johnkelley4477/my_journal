package com.example.myjournal

import android.os.Bundle
import android.os.PatternMatcher
import android.util.Log
import android.util.Patterns
//import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

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
    private lateinit var regButton: Button
    private lateinit var regUserText: EditText
    private lateinit var regPasswordText: EditText
    private lateinit var regConfirmPasswordText: EditText
    private fun signUpUser(){
        Toast.makeText(baseContext, "Test3.",
                        Toast.LENGTH_SHORT).show()

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
                    // Sign in success, update UI with the signed-in user's information
                   // Log.d(TAG, "createUserWithEmail:success")
                    Toast.makeText(baseContext, "Authentication was successful.",
                        Toast.LENGTH_SHORT).show()
                    loginHandler.login(loginView,regView)
                } else {
                    // If sign in fails, display a message to the user.
                   // Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }

                // ...
            }
    }
    //--------------------

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
       // setSupportActionBar(toolbar)

        //ToDo relocate this if possible
        auth = FirebaseAuth.getInstance()
        regButton = findViewById(R.id.regButton)
        regPasswordText = findViewById(R.id.regPasswordText)
        regUserText = findViewById(R.id.regUserText)
        regConfirmPasswordText = findViewById(R.id.regConfirmPasswordText)
        regButton.setOnClickListener{
            signUpUser()
        }

        //----------------

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
}
