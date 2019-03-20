package com.example.hp.echo.databases

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.hp.echo.Songs
import org.w3c.dom.Text

class EchoDatabase: SQLiteOpenHelper{

    var _songList = ArrayList<Songs>()
    object Staticated{
        var DB_VERSION =1
        val DB_NAME = "FavoriteDatabase"
        val TABLE_NAME ="FavoriteTable"
        val COLUMN_ID = "SongID"
        val COLUMN_SONG_TITLE = "SongTitle"
        val COLUMN_SONG_ARTIST = "SongArtist"
        val COLUMN_SONG_PATH = "SongPath"

    }
    override fun onCreate(sqliteDatabase: SQLiteDatabase?) {
        sqliteDatabase?.execSQL("CREATE TABLE " + Staticated.TABLE_NAME + "( " + Staticated.COLUMN_ID + " INTEGER," + Staticated.COLUMN_SONG_ARTIST + "STRING,"+ Staticated.COLUMN_SONG_TITLE + "STRING," +
                Staticated.COLUMN_SONG_PATH + "TEXT);")
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {

    }

    constructor(context: Context?, name: String?, factory: SQLiteDatabase.CursorFactory?, version: Int) : super(context, name, factory, version)
    constructor(context: Context?) : super(context, Staticated.DB_NAME, null, Staticated.DB_VERSION)

    fun storeAsFavorite(songId: Int?, songArtist: String?, songTitle: String?, songPath: String?) {
       val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(Staticated.COLUMN_ID, songId)
        contentValues.put(Staticated.COLUMN_SONG_ARTIST, songArtist)
        contentValues.put(Staticated.COLUMN_SONG_TITLE, songTitle)
        contentValues.put(Staticated.COLUMN_SONG_PATH, songPath)
        db.insert(Staticated.TABLE_NAME, null, contentValues)
        db.close()
    }
    fun queryDBList(): ArrayList<Songs>? {
        try {
            val db = this.readableDatabase
            val query_params = "SELECT * FROM " + Staticated.TABLE_NAME
            var cSor = db.rawQuery(query_params, null)
            if (cSor.moveToFirst()) {
                do {
                    var _id = cSor.getInt(cSor.getColumnIndexOrThrow(Staticated.COLUMN_ID))
                    var _artist = cSor.getString(cSor.getColumnIndexOrThrow(Staticated.COLUMN_SONG_ARTIST))
                    var _title = cSor.getString(cSor.getColumnIndexOrThrow(Staticated.COLUMN_SONG_TITLE))
                    var _songPath = cSor.getString(cSor.getColumnIndexOrThrow(Staticated.COLUMN_SONG_PATH))
                    _songList.add(Songs(_id.toLong(), _title, _artist, _songPath, 0))
                }
                while (cSor.moveToNext())
            }
            else {
                return null
            }
        }
        catch (e: Exception) {
            e.printStackTrace()
        }
        return _songList
    }
    fun checkifIdExists(_id: Int): Boolean {
        var storeId = -1090
        val db = this.readableDatabase
        val query_params = "SELECT * FROM " + Staticated.TABLE_NAME + " WHERE SongID = '$_id'"
        val cSor = db.rawQuery(query_params, null)
        if (cSor.moveToFirst()) {
            do {
                storeId = cSor.getInt(cSor.getColumnIndexOrThrow(Staticated.COLUMN_ID))
            } while (cSor.moveToNext())
        } else {
            return false
        }
        return storeId != -1090
    }

    fun deleteFavourite(_id: Int) {
        val db = this.writableDatabase
        db.delete(Staticated.TABLE_NAME, Staticated.COLUMN_ID + " = " + _id, null)
        db.close()
    }

    fun checkSize():Int{
        var counter =0
        val db = this.readableDatabase
        var query_params = "SELECT * FROM " + Staticated.TABLE_NAME
        val cSor = db.rawQuery(query_params, null)
        if (cSor.moveToFirst()){
            do {
                counter = counter + 1
            }while (cSor.moveToNext())
        }else{
            return 0
        }
        return counter
    }
}

private fun ContentValues.put(columN_SONG_PATH: String, songPath: Text?) {

}