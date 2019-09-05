package com.example.myjournal

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class TagAdapter(val mCtx: Context, val layoutResId: Int, val tagList: MutableList<Tags>)
    :ArrayAdapter<Tags>(mCtx, layoutResId, tagList){
    val TAG: String = "TagAdapter"
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater: LayoutInflater = LayoutInflater.from(mCtx)
        val view: View = layoutInflater.inflate(layoutResId, null)
        val tagText = view.findViewById<TextView>(R.id.tag_text)
        val tag = tagList[position]
        tagText.text = tag.tag
        var drawableBackground: GradientDrawable = GradientDrawable()
        drawableBackground.cornerRadius = 50F
        drawableBackground.setStroke(7, Color.parseColor(tag.borderColor))
        drawableBackground.setColor(Color.parseColor(tag.innerColor))
        tagText.background = drawableBackground
        tagText.setTextColor(Color.parseColor(tag.textColor))
        return view
    }
}