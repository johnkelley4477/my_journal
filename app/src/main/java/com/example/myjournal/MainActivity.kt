package com.example.myjournal

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
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
    lateinit var currentUserId: String
    val TAG: String = "Main"
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

        val today = LocalDateTime.now()
        val formatted = today.format(simpleFormat)
        dateTextName.setText(formatted.toString())

        saveButton.setOnClickListener {
            val entry = Entry()
            entry.saveEntry(dateTextName, tagsTextName, entryTextName, this@MainActivity,currentUserId)
        }
    }

    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if(currentUser != null) {
            currentUserId = currentUser.uid
        }
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
