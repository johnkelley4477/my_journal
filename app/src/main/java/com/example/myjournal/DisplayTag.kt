package com.example.myjournal

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.flask.colorpicker.ColorPickerView
import com.flask.colorpicker.builder.ColorPickerDialogBuilder
import android.util.Log



class DisplayTag: AppCompatActivity() {
    val TAG: String = "TagDisplay"
    lateinit var ref: DatabaseReference
    lateinit var auth: FirebaseAuth
    lateinit var tagText: TextView
    lateinit var tagTextColor: TextView
    lateinit var tagBorderColor: TextView
    lateinit var tagBackgroundColor: TextView
    lateinit var id: String
    var map: HashMap<String,String> = hashMapOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.display_tag)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        tagText = findViewById(R.id.tag_display_text)
        tagTextColor = findViewById(R.id.tag_display_textColor)
        tagBorderColor = findViewById(R.id.tag_display_borderColor)
        tagBackgroundColor = findViewById(R.id.tag_display_backgroundColor)
        setSupportActionBar(toolbar)
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        id = intent.getStringExtra("id")
        if(currentUser != null) {
            ref = FirebaseDatabase.getInstance().getReference("${currentUser.uid}/tags")
            ref.child(id).addValueEventListener(object: ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onDataChange(p0: DataSnapshot) {
                    for(t in p0.children) {
                        map.set(t.key.toString(), t.value.toString())
                    }
                    tagText.text = map.get("tag")
                    var drawableBackground = GradientDrawable()
                    drawableBackground.cornerRadius = 50F
                    drawableBackground.setStroke(7, Color.parseColor(map.get("borderColor")))
                    drawableBackground.setColor(Color.parseColor(map.get("innerColor")))
                    tagText.background = drawableBackground
                    tagText.setTextColor(Color.parseColor(map.get("textColor")))
                }
            })
        }
        tagTextColor.setOnClickListener{
            colorSelector(Color.parseColor(map.get("textColor")),"Text","textColor")
        }
        tagBorderColor.setOnClickListener{
            colorSelector(Color.parseColor(map.get("borderColor")),"Border","borderColor")
        }
        tagBackgroundColor.setOnClickListener{
            colorSelector(Color.parseColor(map.get("innerColor")),"Background","innerColor")
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
            R.id.tag_manager -> {
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

    fun colorSelector(currentBackgroundColor: Int,type: String,key: String){
        ColorPickerDialogBuilder
            .with(this)
            .setTitle("Choose color for $type")
            .initialColor(currentBackgroundColor)
            .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
            .density(12)
            .setOnColorSelectedListener { selectedColor ->
                Log.d(TAG,"onColorSelected: 0x${Integer.toHexString(selectedColor)}")
            }
            .setPositiveButton(
                "ok"
            ) { dialog, selectedColor, allColors ->
                ref.child(id).child(key).setValue("#${Integer.toHexString(selectedColor).capitalize()}")
            }
            .setNegativeButton(
                "cancel"
            ) { dialog, which -> }
            .build()
            .show()
    }
}