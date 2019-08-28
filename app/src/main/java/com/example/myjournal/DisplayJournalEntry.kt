package com.example.myjournal

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class DisplayJournalEntry: AppCompatActivity() {
    val TAG = "DisplayJournalEntry"
    lateinit var date: TextView
    lateinit var tags: TextView
    lateinit var entry: TextView
    lateinit var update: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.display_entry)
        val jEntry = intent.getStringArrayListExtra("jEntry")
        Log.d(TAG,"test ${jEntry[1].length}")
        if(jEntry.size > 0) {
            date = findViewById(R.id.textShowDate)
            tags = findViewById(R.id.textShowTags)
            entry = findViewById(R.id.textShowEntry)
            update = findViewById(R.id.updateButton)
            update.setOnClickListener{
                showUpdateDialog(jEntry)
            }
            date.text = getFormatedDate(jEntry[1]) + " " + getFormatedTime(jEntry[1])
            tags.text = jEntry[2].substring(1, jEntry[2].length - 1).replace(",", "")
            entry.text = jEntry[3]

        }
    }

    fun showUpdateDialog(jEntry: ArrayList<String>){
        val builder = AlertDialog.Builder(this)
        val inflater = LayoutInflater.from(this)
        val view = inflater.inflate(R.layout.layout_update_entry,  null)
        val editTextTags = view.findViewById<EditText>(R.id.tagsDialogText)
        val editTextEntry = view.findViewById<EditText>(R.id.entryDialogText)

        editTextTags.setText(jEntry[2].substring(1, jEntry[2].length - 1).replace(",", ""))
        editTextEntry.setText(jEntry[3])

        builder.setTitle("Update your entry")
        builder.setView(view)
        builder.setNegativeButton("No") {p0,P1 ->
        }

        builder.setPositiveButton("Update") {p0,P1 ->
            val auth = FirebaseAuth.getInstance()
            val currentUser = auth.currentUser
            if(currentUser != null) {
                val fdb = FirebaseDatabase.getInstance().getReference("${currentUser.uid}/entry")
                val tags = if(editTextTags.text.isEmpty()) null else editTextTags.text.split(" ")
                val entry = editTextEntry.text.toString().trim()

                if(entry.isEmpty()){
                    editTextEntry.error = "Please enter an Entry"
                    editTextEntry.requestFocus()
                    return@setPositiveButton
                }
                val journalEntry = JournalEntry(jEntry[0],jEntry[1],entry,tags)

                fdb.child(jEntry[0]).setValue(journalEntry)

                Toast.makeText(this,"Our journal entry has been updated",Toast.LENGTH_LONG).show()
            }
        }

        val alert = builder.create()
        alert.show()
    }

    //ToDo create a helper class with these
    fun getFormatedDate(date: String): String{
        if(!date.isEmpty()){
            val year = date.substring(0,4)
            val mon = date.substring(5,7)
            val day = date.substring(8,10)

            return "$mon/$day/$year"
        }else{
            return ""
        }
    }

    fun getFormatedTime(time: String): String{
        if(!time.isEmpty()){
            return time.substring(11)
        }else{
            return ""
        }
    }
}