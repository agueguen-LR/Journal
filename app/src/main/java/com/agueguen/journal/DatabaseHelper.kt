package com.agueguen.journal

import android.annotation.SuppressLint
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, "JournalDB", null, 1){
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE JournalDB (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "title TEXT," +
                "content TEXT," +
                "date TEXT," +
                "tag BOOLEAN)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        //nothing for now
    }

    enum class COLUMNS{
        ID,
        TITLE,
        CONTENT,
        DATE,
        TAG
    }

    fun insertData(title: String, content: String, date: String, tag: Boolean){
        this.writableDatabase.execSQL("INSERT INTO JournalDB(title, content, date, tag)" +
                "VALUES('${title}', '${content}', '${date}', ${tag})")
    }

    fun deleteData(id: Int){
        this.writableDatabase.execSQL("DELETE FROM JournalDB WHERE id = $id")
    }

    @SuppressLint("Range")
    fun getData(list: ArrayList<JournalEntry>, orderBy: DatabaseHelper.COLUMNS?, having: String?) {
        list.clear()
        val cursor = this.readableDatabase.query("JournalDB",
            null,
            null,
            null,
            null,
            having,
            "$orderBy")
        if (cursor.moveToFirst()){
            do {
                val journalEntry = JournalEntry(
                    cursor.getInt(cursor.getColumnIndex("id")),
                    cursor.getString(cursor.getColumnIndex("title")),
                    cursor.getString(cursor.getColumnIndex("content")),
                    cursor.getString(cursor.getColumnIndex("date")),
                    cursor.getInt(cursor.getColumnIndex("tag")) == 1
                )
                list.add(journalEntry)
            } while (cursor.moveToNext())
        }
        cursor.close()
        Log.i("DatabaseHelper", "$list")
    }
}