package com.example.notespasswordmanagerappandroid.Room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import net.sqlcipher.database.SQLiteDatabase
import kotlin.coroutines.CoroutineContext
import net.sqlcipher.database.SupportFactory




@Database(entities = [PDetailsEntites::class], version = 2, exportSchema = false)
abstract class MyRoom:RoomDatabase()
{

    companion object {
        private var object1:Any?=""
        private var myRoom:MyRoom?=null
        private var databaseName:String="PassworddetailsDB.db"

        public fun getInstance(context: Context): MyRoom {
            if (myRoom == null) {
                synchronized(object1!!)
                {
                    //below two line is to secure/close the database
                    val passphrase: ByteArray = SQLiteDatabase.getBytes(CharArray(20))
                    val factory = SupportFactory(passphrase)


                    myRoom = Room.databaseBuilder(context, MyRoom::class.java, databaseName).openHelperFactory(factory)
                        .build()
                }
            }
            return myRoom!!
        }
    }

    abstract fun getDaoInterface():DaoInterface



}