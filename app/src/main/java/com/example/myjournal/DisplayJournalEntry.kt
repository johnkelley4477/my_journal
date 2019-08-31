package com.example.myjournal

import android.app.AlertDialog
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.LinearLayout.LayoutParams
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.login_main.view.*

class DisplayJournalEntry: AppCompatActivity() {
    val TAG = "DisplayJournalEntry"
    lateinit var date: TextView
    lateinit var tags: TextView
    lateinit var entry: TextView
    lateinit var update: TextView
    lateinit var delete: TextView
    lateinit var auth: FirebaseAuth
    lateinit var fdb: DatabaseReference
    lateinit var builder: AlertDialog.Builder
    lateinit var inflater: LayoutInflater

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.display_entry)
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        builder = AlertDialog.Builder(this)
        inflater = LayoutInflater.from(this)
        if(currentUser != null) {
            val id = intent.getStringExtra("id")
            var map: HashMap<String, String> = hashMapOf()

            fdb = FirebaseDatabase.getInstance().getReference("${currentUser.uid}/entry")
            fdb.child(id).addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onDataChange(p0: DataSnapshot) {
                    for(j in p0.children){
                        map.set(j.key.toString(),j.value.toString())
                    }
                    date = findViewById(R.id.textShowDate)
                    entry = findViewById(R.id.textShowEntry)
                    update = findViewById(R.id.updateButton)
                    delete = findViewById(R.id.deleteButton)
                    var tagLayout = findViewById<LinearLayout>(R.id.tagsLayout)
                    update.setOnClickListener{
                        showUpdateDialog(map)
                    }
                    delete.setOnClickListener{
                        showDeleteDialog(map)
                    }
                    date.text = getFormatedDate(map.get("date")!!) + " " + getFormatedTime(map.get("date")!!)
                    //tags.text = map.get("tags")!!.substring(1, map.get("tags")!!.length - 1).replace(",", "")
                    entry.text = map.get("entry")!!
                    var i = 0
                    for(tag in map.get("tags")!!.substring(1, map.get("tags")!!.length - 1).split(",")){
                        val layoutInflater: LayoutInflater = LayoutInflater.from(this@DisplayJournalEntry)
                        var view: View = layoutInflater.inflate(R.layout.tag_text, null)
                        val textView = view.findViewById<TextView>(R.id.tag_text)
                        val layoutParamss = LayoutParams(
                            LayoutParams.WRAP_CONTENT, // This will define text view width
                            LayoutParams.WRAP_CONTENT // This will define text view height
                        )
                        layoutParamss.setMargins(10,10,10,10)
                        //textView.background = Drawable.(R.drawable.rounded_corner_green)
                        textView.layoutParams = layoutParamss
                        textView.text = tag
                        tagLayout?.addView(view)
                        ++i
                    }
                }
            })
        }else{
            startActivity(Intent(this, Login::class.java))
            finish()
        }
    }

    fun showDeleteDialog(map: HashMap<String,String>){
        Log.d(TAG,"Clicked Delete $map")
        builder.setTitle("Are you sure you want to DELETE this entry?")
        builder.setPositiveButton("Yes") { p0, P1 ->
            fdb.child(map.get("id").toString()).setValue(null)
            Toast.makeText(this,"Our journal entry has been deleted",Toast.LENGTH_LONG).show()
            startActivity(Intent(this, JournalList::class.java))
            finish()
        }
        builder.setNegativeButton("No") {p0,P1 ->
        }
        val alert = builder.create()
        alert.show()
    }

    fun showUpdateDialog(map: HashMap<String,String>){
        val view = inflater.inflate(R.layout.layout_update_entry,  null)
        val editTextTags = view.findViewById<EditText>(R.id.tagsDialogText)
        val editTextEntry = view.findViewById<EditText>(R.id.entryDialogText)

        editTextTags.setText(map.get("tags")!!.substring(1, map.get("tags")!!.length - 1).replace(",", ""))
        editTextEntry.setText(map.get("entry")!!)

        builder.setTitle("Update your entry")
        builder.setView(view)
        builder.setNegativeButton("No") {p0,P1 ->
        }

        builder.setPositiveButton("Update") {p0,P1 ->
            val tags = if(editTextTags.text.isEmpty()) null else editTextTags.text.split(" ")
            val entry = editTextEntry.text.toString().trim()

            if(entry.isEmpty()){
                editTextEntry.error = "Please enter an Entry"
                editTextEntry.requestFocus()
                return@setPositiveButton
            }
            val journalEntry = JournalEntry(map.get("id").toString(),map.get("date").toString(),entry,tags)
            val fab = fdb.child(map.get("id").toString()).setValue(journalEntry)
            Log.d(TAG,"test id ${fab}")

            Toast.makeText(this,"Our journal entry has been updated",Toast.LENGTH_LONG).show()
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