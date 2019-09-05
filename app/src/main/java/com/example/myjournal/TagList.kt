package com.example.myjournal

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class TagList: AppCompatActivity() {
    lateinit var ref: DatabaseReference
    lateinit var auth: FirebaseAuth
    lateinit var tagList: MutableList<Tags>
    lateinit var listView: ListView
    val TAG: String = "TagList"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.lists_tags)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        if(currentUser != null) {
            tagList = mutableListOf()
            listView = findViewById(R.id.t_list)
            ref = FirebaseDatabase.getInstance().getReference("${currentUser.uid}/tags")
            ref.addValueEventListener(object: ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onDataChange(p0: DataSnapshot) {
                    buildList(p0)
                }

            })
        }
    }

    fun buildList(dataSet: DataSnapshot){
        tagList.clear()
        if(dataSet!!.exists()){
            for(t in dataSet.children){
                var tag = t.getValue(Tags::class.java)
                tagList.add(tag!!)
            }
            if(tagList.size < 1){
                Toast.makeText(this,"No Tags found",Toast.LENGTH_LONG).show()
            }
            val adapter = TagAdapter(this@TagList, R.layout.tag_item, tagList)
            listView.adapter = adapter
            listView.setOnItemClickListener{adapterView, view, position: Int, id: Long ->
                var intent = Intent(this@TagList, DisplayTag::class.java)
                intent.putExtra("id",tagList[position].id)
                startActivity(intent)
            }
        }
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

            R.id.tag_manager ->{
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