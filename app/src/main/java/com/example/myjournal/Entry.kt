package com.example.myjournal

import android.content.Context
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*

class Entry() {

    fun saveEntry(tagsTextName: EditText, entryTextName:EditText, context: Context,currentUser: String){
        val dateString = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val tag = tagsTextName.text.toString().trim()
        val entry = entryTextName.text.toString().trim()
        val tags = if(tag.isEmpty()) null else tag.split(" ")
        if(entry.isEmpty()){
            entryTextName.error = "Please enter a journal entry"
            entryTextName.requestFocus()
            return
        }
        val ref = FirebaseDatabase.getInstance().getReference("$currentUser/entry")
        val id = ref.push().key
        val journalEntry = JournalEntry(id!!,getISODate(dateString),entry,tags)

        ref.child(id).setValue(journalEntry)
        val toast = Toast.makeText(context,"Your Journal Entry has been saved", Toast.LENGTH_LONG)
        toast.show()
        tagsTextName.setText("")
        entryTextName.setText("")
    }

    fun saveTags(tagsTextName: String, context: Context, currentUser: String){
        val tag = tagsTextName.trim()
        val ref = FirebaseDatabase.getInstance().getReference("$currentUser/tags")
        val id = ref.push().key
        val tags = Tags(id!!,"#FFFFFF","#000000","#000000",tag)

        ref.child(id).setValue(tags)
        val toast = Toast.makeText(context,"Your Journal Entry has been saved", Toast.LENGTH_LONG)
        toast.show()

    }

    private fun getISODate(stringDate: String):String{
        val year = stringDate.substring(0,4)
        val month = stringDate.substring(4,6)
        val day = stringDate.substring(6,8)
        val hour = stringDate.substring(9,11)
        val min = stringDate.substring(11,13)
        val sec = stringDate.substring(13,15)
        return "$year-$month-$day"+"T$hour:$min:$sec"
    }
}
