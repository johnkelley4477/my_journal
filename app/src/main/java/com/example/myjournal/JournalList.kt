package com.example.myjournal

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class JournalList: AppCompatActivity() {

    lateinit var ref: DatabaseReference
    lateinit var journalEntryList: MutableList<JournalEntry>
    lateinit var listView: ListView
    lateinit var auth: FirebaseAuth
    val TAG: String = "JournalList"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.lists_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        if(currentUser != null) {
            journalEntryList = mutableListOf()

            listView = findViewById(R.id.j_list)

            ref = FirebaseDatabase.getInstance().getReference("${currentUser.uid}/entry")

            ref.addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }
                override fun onDataChange(p0: DataSnapshot) {
                    journalEntryList.clear()
                    if (p0!!.exists()) {
                        for (j in p0.children) {
                            var journalEntry = j.getValue(JournalEntry::class.java)
                            journalEntryList.add(journalEntry!!)
                        }
                        val adapter = ListAdapter(this@JournalList, R.layout.journal_entry, journalEntryList)
                        listView.adapter = adapter
                        listView.setOnItemClickListener{adapterView, view, position: Int, id: Long ->
                            var intent = Intent(this@JournalList, DisplayJournalEntry::class.java)
                            intent.putExtra("id",journalEntryList[position].id)
                            startActivity(intent)
                        }
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

    fun searchFor(value: String){
        val isSingleDate: Boolean = value.substring(1,2) == "/" || value.substring(1,2) == "-"
        val isDoubleDate: Boolean = value.substring(2,3) == "/" || value.substring(2,3) == "-"

        if(isSingleDate || isDoubleDate){
            // Is a Date
            dateSearch(value.replace("/","-"))
        }else{
            //Is a tag
        }
    }

    fun dateSearch(date: String){
        val dateArray = date.split("-")
        val mon = if(dateArray[0].length == 1) "0${dateArray[0]}" else dateArray[0]
        val day = if(dateArray[1].length == 1) "0${dateArray[1]}" else dateArray[1]
        val year = if(dateArray[2].length == 2) "20${dateArray[2]}" else dateArray[2]
        val dateRef = ref.orderByChild("date").startAt("$year-$mon-$day")
        dateRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                for(j in p0.children) {
                    Log.d(TAG, "key is: ${j.value}")
                }
            }
        })

    }
}