package com.example.myjournal

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DisplayJournalEntry: AppCompatActivity() {
    val TAG = "DisplayJournalEntry"
    lateinit var date: TextView
    lateinit var tags: TextView
    lateinit var entry: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.display_entry)
        val jEntry = intent.getStringArrayListExtra("jEntry")
        Log.d(TAG,"test ${jEntry[1].length}")
        if(jEntry.size > 0) {
            date = findViewById(R.id.textShowDate)
            tags = findViewById(R.id.textShowTags)
            entry = findViewById(R.id.textShowEntry)
            date.text = getFormatedDate(jEntry[1]) + " " + getFormatedTime(jEntry[1])
            tags.text = jEntry[2].substring(1, jEntry[2].length - 1).replace(",", "")
            entry.text = jEntry[3]
        }
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