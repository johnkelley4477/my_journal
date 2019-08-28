package com.example.myjournal

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.ArrayList

class JournalList: AppCompatActivity() {

    lateinit var ref: DatabaseReference
    lateinit var journalEntryList: MutableList<JournalEntry>
    lateinit var listView: ListView
    val TAG: String = "JournalList"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.lists_main)
        val auth = FirebaseAuth.getInstance()
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
                            var arrayItems = ArrayList<String>()
                            arrayItems.add(journalEntryList[position].id)
                            arrayItems.add(journalEntryList[position].date)
                            arrayItems.add(journalEntryList[position].tags.toString())
                            arrayItems.add(journalEntryList[position].entry)
                            Log.d(TAG,"journalEntryList at click ${arrayItems}")
                            intent.putExtra("jEntry",arrayItems)
                            startActivity(intent)
                            //finish()

                        }
                    }
                }
            })
        }else{
            Log.e(TAG,"no data")
            Toast.makeText(this,"This is a test. Not user info found",Toast.LENGTH_LONG)
        }
    }
}