package com.example.myjournal

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MainActivity : AppCompatActivity() {

    lateinit var dateTextName: EditText
    lateinit var tagsTextName: EditText
    lateinit var entryTextName: EditText
    lateinit var saveButton: Button
    lateinit var entryView: View
    lateinit var loginView: View
    lateinit var regView: View
    private lateinit var auth: FirebaseAuth
    val simpleFormat = DateTimeFormatter.ofPattern("M/d/yy")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        auth = FirebaseAuth.getInstance()

        dateTextName = findViewById(R.id.dateText)
        tagsTextName = findViewById(R.id.tagsText)
        entryTextName = findViewById(R.id.entryText)
        saveButton = findViewById(R.id.saveButton)
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
    }

    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
