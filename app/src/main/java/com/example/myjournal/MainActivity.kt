package com.example.myjournal

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    lateinit var tagsTextName: EditText
    lateinit var entryTextName: EditText
    lateinit var saveButton: Button
//    lateinit var listButton: Button
    lateinit var currentUserId: String
    val TAG: String = "Main"
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        auth = FirebaseAuth.getInstance()
        tagsTextName = findViewById(R.id.tagsText)
        entryTextName = findViewById(R.id.entryText)
        saveButton = findViewById(R.id.saveButton)
//        listButton = findViewById(R.id.listButton)
        saveButton.setOnClickListener {
            val entry = Entry()
            entry.saveEntry(tagsTextName, entryTextName, this@MainActivity,currentUserId)
            startActivity(Intent(this, JournalList::class.java))
        }
//        listButton.setOnClickListener{
//            startActivity(Intent(this, JournalList::class.java))
//        }
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
            R.id.list -> {
                startActivity(Intent(this, JournalList::class.java))
                true
            }
            R.id.search -> {
                true
            }
            R.id.logout -> {
                auth.signOut()
                startActivity(Intent(this, Login::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
