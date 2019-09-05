package com.example.myjournal

import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.database.DatabaseReference

class Helper{
    val TAG = "Helper"
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

    fun buildModal(context: Context, view: View, map: HashMap<String,String>, fdb: DatabaseReference, tags: List<String>, entry: String){
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Update your entry")
        builder.setView(view)
        builder.setNegativeButton("No") {p0,P1 ->
        }

        builder.setPositiveButton("Update") {p0,P1 ->
            val journalEntry = JournalEntry(map.get("id").toString(),map.get("date").toString(),entry,tags)
            val fab = fdb.child(map.get("id").toString()).setValue(journalEntry)
            Log.d(TAG,"result: $fab")
            Toast.makeText(context,"Our journal entry has been updated", Toast.LENGTH_LONG).show()
        }

        val alert = builder.create()
        alert.show()
    }
}