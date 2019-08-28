package com.example.myjournal

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.TextView

class ListAdapter(val mCtx: Context, val layoutResId: Int, val journalList: List<JournalEntry>): ArrayAdapter<JournalEntry>(mCtx,layoutResId,journalList) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater: LayoutInflater = LayoutInflater.from(mCtx)
        val view: View = layoutInflater.inflate(layoutResId, null)
        val textViewDate = view.findViewById<TextView>(R.id.textViewDate)
        val textViewTime = view.findViewById<TextView>(R.id.textViewTime)
        val textViewTags = view.findViewById<TextView>(R.id.textViewTags)
        val journalEntry = journalList[position]
        textViewDate.text = getFormatedDate(journalEntry.date)
        textViewTime.text = getFormatedTime(journalEntry.date)
        textViewTags.text = getFormatedTags(journalEntry.tags)

        return view
    }

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

    fun getFormatedTags(tags: List<String>?): String{
        if(tags != null){
            var tagList = ""
            for(tag in tags){
                tagList += "$tag "
            }
            return tagList
        }else{
            return ""
        }
    }
}