package com.example.myjournal

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import java.lang.Exception

class ListAdapter(val mCtx: Context, val layoutResId: Int, val journalList: List<JournalEntry>): ArrayAdapter<JournalEntry>(mCtx,layoutResId,journalList) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater: LayoutInflater = LayoutInflater.from(mCtx)
        val view: View = layoutInflater.inflate(layoutResId, null)
        val textViewDate = view.findViewById<TextView>(R.id.textViewDate)
        val textViewTags = view.findViewById<TextView>(R.id.textViewTags)
        val journalEntry = journalList[position]
        textViewDate.text = journalEntry.date
        textViewTags.text = journalEntry.tags.toString()
        return view
    }
}