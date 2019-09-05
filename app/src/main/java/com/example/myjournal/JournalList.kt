package com.example.myjournal

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class JournalList: AppCompatActivity() {

    lateinit var ref: DatabaseReference
    lateinit var refTags: DatabaseReference
    lateinit var journalEntryList: MutableList<JournalEntry>
    lateinit var listView: ListView
    lateinit var auth: FirebaseAuth
    lateinit var sBox: EditText
    lateinit var tagList: MutableList<Tags>
    val TAG: String = "JournalList"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.lists_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        sBox = findViewById<EditText>(R.id.search_box)
        setSupportActionBar(toolbar)
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        if(currentUser != null) {
            journalEntryList = mutableListOf()
            listView = findViewById(R.id.j_list)
            ref = FirebaseDatabase.getInstance().getReference("${currentUser.uid}/entry")
            ref.orderByChild("timeStamp").limitToLast(10).addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }
                override fun onDataChange(p0: DataSnapshot) {
                    buildList(p0, "All",null)
                }
            })
            sBox.setOnKeyListener(View.OnKeyListener{v, keyCode, event ->
                if(keyCode == KeyEvent.KEYCODE_ENTER  && event.action == KeyEvent.ACTION_UP){
                    searchFor(sBox.text.toString())
                    true
                }
                false
            })

            refTags = FirebaseDatabase.getInstance().getReference("${currentUser.uid}/tags")
            tagList = mutableListOf()
            refTags.addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onDataChange(p0: DataSnapshot) {
                    for (t in p0.children) {
                        var tag = t.getValue(Tags::class.java)
                        tagList.add(tag!!)
                    }
                }
            })
        }else{
            Log.e(TAG,"no data")
            Toast.makeText(this,"This is a test. Not user info found",Toast.LENGTH_LONG)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        menu.removeItem(R.id.list)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.list -> {
                startActivity(Intent(this, JournalList::class.java))
                true
            }
            R.id.search -> {
                if(sBox.visibility != View.VISIBLE) {
                    sBox.visibility = View.VISIBLE
                    sBox.requestFocus()
                    val activity: Activity = this
                    val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_IMPLICIT_ONLY)
                }else{
                    searchFor(sBox.text.toString())
                }
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

    fun searchFor(value: String){
        val isSingleDate: Boolean = value.substring(1,2) == "/" || value.substring(1,2) == "-"
        val isDoubleDate: Boolean = value.substring(2,3) == "/" || value.substring(2,3) == "-"

        if(isSingleDate || isDoubleDate){
            // Is a Date
            dateSearch(value.replace("/","-"))
        }else{
            //Is a tag
            tagSearch(value)
        }
    }

    fun tagSearch(tag: String){
        Log.d(TAG,"Test $journalEntryList")
        val tagRef = ref.orderByChild("tags")
        tagRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                buildList(p0, tag, "tag")
            }
        })

    }

    fun dateSearch(date: String){
        val dateArray = date.split("-")
        val mon = if(dateArray[0].length == 1) "0${dateArray[0]}" else dateArray[0]
        val day = if(dateArray[1].length == 1) "0${dateArray[1]}" else dateArray[1]
        val year = if(dateArray[2].length == 2) "20${dateArray[2]}" else dateArray[2]
        val dateRef = ref.orderByChild("date")
            .startAt("$year-$mon-${day}T00")
            .endAt("$year-$mon-${day}T24")
        dateRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                buildList(p0, date, "date")
            }
        })

    }

    fun buildList(dataSet: DataSnapshot,searchTerm: String,type: String?){
        journalEntryList.clear()
        if (dataSet!!.exists()) {
            for (j in dataSet.children) {
                var journalEntry = j.getValue(JournalEntry::class.java)
                if(type.equals("tag")){
                    for (tag in journalEntry!!.tags!!.iterator()){
                        if(tag.equals(searchTerm)) {
                            journalEntryList.add(journalEntry!!)
                        }
                    }
                }else {
                    journalEntryList.add(journalEntry!!)
                }
            }
            if(journalEntryList.size < 1){
                Toast.makeText(this,"Sorry no records found for $searchTerm",Toast.LENGTH_LONG).show()
            }
            val journalEntryListReversed = journalEntryList.asReversed()
            val adapter = ListAdapter(this@JournalList, R.layout.journal_entry, journalEntryListReversed, tagList)
            listView.adapter = adapter
            listView.setOnItemClickListener{adapterView, view, position: Int, id: Long ->
                var intent = Intent(this@JournalList, DisplayJournalEntry::class.java)
                intent.putExtra("id",journalEntryListReversed[position].id)
                startActivity(intent)
            }
        }
    }
}