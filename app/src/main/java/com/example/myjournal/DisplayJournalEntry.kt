package com.example.myjournal

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.nfc.Tag
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.LinearLayout.LayoutParams
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class DisplayJournalEntry: AppCompatActivity() {
    val TAG = "DisplayJournalEntry"
    lateinit var date: TextView
    lateinit var tags: TextView
    lateinit var entry: TextView
    lateinit var update: TextView
    lateinit var delete: TextView
    lateinit var auth: FirebaseAuth
    lateinit var fdb: DatabaseReference
    lateinit var ref: DatabaseReference
    lateinit var tagList: MutableList<Tags>
    lateinit var builder: AlertDialog.Builder
    lateinit var inflater: LayoutInflater

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.display_entry)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        builder = AlertDialog.Builder(this)
        inflater = LayoutInflater.from(this)
        if(currentUser != null) {
            val id = intent.getStringExtra("id")
            var map: HashMap<String, String> = hashMapOf()
            ref = FirebaseDatabase.getInstance().getReference("${currentUser.uid}/tags")
            tagList = mutableListOf()
            ref.addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onDataChange(p0: DataSnapshot) {
                    for(t in p0.children) {
                        if(t.exists()) {
                            var tag = t.getValue(Tags::class.java)
                            tagList.add(tag!!)
                        }
                    }
                }
            })

            fdb = FirebaseDatabase.getInstance().getReference("${currentUser.uid}/entry")
            fdb.child(id).addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onDataChange(p0: DataSnapshot) {
                    val helper = Helper()
                    for(j in p0.children){
                        map.set(j.key.toString(),j.value.toString())
                    }
                    date = findViewById(R.id.textShowDate)
                    entry = findViewById(R.id.textShowEntry)
                    update = findViewById(R.id.updateButton)
                    delete = findViewById(R.id.deleteButton)
                    var tagLayout = findViewById<LinearLayout>(R.id.tagsLayout)
                    tagLayout.removeAllViewsInLayout()
                    update.setOnClickListener{
                        showUpdateDialog(map)
                    }
                    delete.setOnClickListener{
                        showDeleteDialog(map)
                    }
                    date.text = helper.getFormatedDate(map.get("date")!!) + " " + helper.getFormatedTime(map.get("date")!!)
                    entry.text = map.get("entry")!!
                    for(tag in map.get("tags")!!.substring(1, map.get("tags")!!.length - 1).split(",")){

                        val layoutInflater: LayoutInflater = LayoutInflater.from(this@DisplayJournalEntry)
                        var view: View = layoutInflater.inflate(R.layout.tag_text, null)
                        val textView = view.findViewById<TextView>(R.id.tag_text)
                        val layoutParamss = LayoutParams(
                            LayoutParams.WRAP_CONTENT, // This will define text view width
                            LayoutParams.WRAP_CONTENT // This will define text view height
                        )
                        layoutParamss.setMargins(10,10,10,10)
                        textView.layoutParams = layoutParamss
                        for(t in tagList) {
                            if(tag.trim().equals(t.tag)) {
                                var drawableBackground = GradientDrawable()
                                drawableBackground.cornerRadius = 50F
                                drawableBackground.setStroke(7, Color.parseColor(t.borderColor))
                                drawableBackground.setColor(Color.parseColor(t.innerColor))
                                textView.background = drawableBackground
                                textView.setTextColor(Color.parseColor(t.textColor))
                            }
                        }
                        textView.text = tag
                        tagLayout?.addView(view)
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
            val journalEntry = JournalEntry(map.get("id").toString(),map.get("date").toString(),entry,tags,map.get("timeStamp")!!.toLong())
            fdb.child(map.get("id").toString()).setValue(journalEntry)

            Toast.makeText(this,"Our journal entry has been updated",Toast.LENGTH_LONG).show()
        }

        val alert = builder.create()
        alert.show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        menu.removeItem(R.id.search)
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