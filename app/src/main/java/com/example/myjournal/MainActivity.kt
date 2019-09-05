package com.example.myjournal

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.*
import java.util.stream.IntStream

class MainActivity : AppCompatActivity() {

    lateinit var ref: DatabaseReference
    lateinit var tagList: MutableList<Tags>
    lateinit var tagsTextName: EditText
    lateinit var entryTextName: EditText
    lateinit var saveButton: Button
    lateinit var currentUserId: String
    lateinit var helper: Helper
    val TAG: String = "Main"
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.content_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        helper = Helper()
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        if(currentUser != null) {
            ref = FirebaseDatabase.getInstance().getReference("${currentUser.uid}/tags")
            tagList = mutableListOf()
            ref.addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }
                override fun onDataChange(p0: DataSnapshot) {
                    for(t in p0.children){
                        var tag = t.getValue(Tags::class.java)
                        tagList.add(tag!!)
                    }
                }
            })
            tagsTextName = findViewById(R.id.tagsText)
            entryTextName = findViewById(R.id.entryText)
            saveButton = findViewById(R.id.saveButton)
            saveButton.setOnClickListener {
                val entry = Entry()
                if (tagsTextName.text != null) {
                    val tags = tagsTextName.text.split(" ")
                    for (tag in tags) {
                        var found = false
                        for (tagObject in tagList) {
                            if (tag.equals(tagObject.tag)) {
                                found = true
                                break
                            }
                        }
                        if(!found) {
                            entry.saveTags(tag, this@MainActivity, currentUserId)
                        }
                    }
                }
                entry.saveEntry(tagsTextName, entryTextName, this@MainActivity, currentUserId)
                startActivity(Intent(this, JournalList::class.java))
            }
        }else{
            startActivity(Intent(this, Login::class.java))
            finish()
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
        menu.removeItem(R.id.search)
        menu.removeItem(R.id.new_entry)
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
            R.id.tag_manager -> {
                startActivity(Intent(this, TagList::class.java))
                true
            }
            R.id.new_entry ->{
                startActivity(Intent(this, MainActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
