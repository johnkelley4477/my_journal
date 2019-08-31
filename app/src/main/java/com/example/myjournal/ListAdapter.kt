package com.example.myjournal

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.TextView

class ListAdapter(val mCtx: Context, val layoutResId: Int, val journalList: List<JournalEntry>): ArrayAdapter<JournalEntry>(mCtx,layoutResId,journalList) {
    val TAG: String = "ListAdapter"
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater: LayoutInflater = LayoutInflater.from(mCtx)
        val view: View = layoutInflater.inflate(layoutResId, null)
        val textViewDate = view.findViewById<TextView>(R.id.textViewDate)
        val textViewTime = view.findViewById<TextView>(R.id.textViewTime)
        val journalEntry = journalList[position]
        textViewDate.text = getFormatedDate(journalEntry.date)
        textViewTime.text = getFormatedTime(journalEntry.date)
        var tagLayout = view.findViewById<LinearLayout>(R.id.layoutTags)
        for(tag in journalEntry.tags!!){
            val layoutInflater: LayoutInflater = LayoutInflater.from(mCtx)
            var tagView: View = layoutInflater.inflate(R.layout.tag_text, null)
            val textView = tagView.findViewById<TextView>(R.id.tag_text)
            val layoutParamss = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, // This will define text view width
                LinearLayout.LayoutParams.WRAP_CONTENT // This will define text view height
            )
            layoutParamss.setMargins(10,10,10,10)
            //textView.background = Drawable.(R.drawable.rounded_corner_green)
            textView.layoutParams = layoutParamss
            textView.text = tag
            textView.textSize = 9F
            tagLayout?.addView(tagView)
        }

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