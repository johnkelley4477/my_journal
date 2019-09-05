package com.example.myjournal

class JournalEntry (
    val id: String,
    val date: String,
    val entry: String,
    val tags: List<String>?,
    val timeStamp: Long
){
    constructor(): this("","","",null,0)


}