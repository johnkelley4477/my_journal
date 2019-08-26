package com.example.myjournal

import android.content.Context
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase

class Entry() {

    fun saveEntry(dateTextName: EditText, tagsTextName: EditText, entryTextName:EditText, context: Context,currentUser: String){
        val dateString = dateTextName.text.toString().trim()
        val tag = tagsTextName.text.toString().trim()
        val entry = entryTextName.text.toString().trim()
        val tags = if(tag.isEmpty()) null else tag.split(" ")
        if(dateString.isEmpty()){
            dateTextName.error = "Please enter a date mm/dd/yy"
            dateTextName.requestFocus()
            return
        }else if(entry.isEmpty()){
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

    private fun getISODate(stringDate: String):String{
        val dateArray = stringDate.split("/")
        val month = if(dateArray[0].length < 2) "0" + dateArray[0] else dateArray[0]
        val day = if(dateArray[1].length < 2) "0" + dateArray[1] else dateArray[1]
        val year = if(dateArray[2].length < 4) "20" + dateArray[2] else dateArray[2]
        return year + "-" + month + "-" + day + "T00:00:00"
    }
}