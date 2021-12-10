package com.example.notespasswordmanagerappandroid.Room

import android.util.Log
import androidx.room.*

@Dao
interface DaoInterface
{
    @Insert
     fun insert(pdetails:PDetailsEntites)
    @Query("SELECT * FROM PasswordDetailsTable")
    fun query():List<PDetailsEntites>
    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(pdetails: PDetailsEntites)
    @Delete
    fun delete(pdetails: PDetailsEntites)
    @Query("SELECT * FROM PasswordDetailsTable WHERE item_id LIKE :itemId")
    fun query(itemId:Int):PDetailsEntites
}