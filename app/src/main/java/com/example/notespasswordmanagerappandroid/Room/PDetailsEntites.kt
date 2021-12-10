package com.example.notespasswordmanagerappandroid.Room

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "PasswordDetailsTable")
 class PDetailsEntites (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "item_id")
    val id:Int,
    @ColumnInfo(name = "Title")
    val title:String,
    @ColumnInfo(name = "account")
    val Account:String,
    @ColumnInfo(name = "username")
    val username:String,
    @ColumnInfo(name = "Password")
    val password:String,
    @ColumnInfo(name = "weblink")
    val weblink:String,
    @ColumnInfo(name="PasswordArray")
    val passwordArray:ByteArray
    ):Parcelable
{


   constructor(parcel: Parcel) : this(
      parcel.readInt(),
      parcel.readString().toString(),
      parcel.readString().toString(),
      parcel.readString().toString(),
      parcel.readString().toString(),
      parcel.readString().toString(),
      parcel.createByteArray()!!
   ) {
   }

   @Ignore
    constructor(title: String,Account: String,username: String,password: String,weblink: String,passwordArray:ByteArray):this(0,title, Account, username, password, weblink,passwordArray)

   override fun writeToParcel(parcel: Parcel, flags: Int) {
      parcel.writeInt(id)
      parcel.writeString(title)
      parcel.writeString(Account)
      parcel.writeString(username)
      parcel.writeString(password)
      parcel.writeString(weblink)
      parcel.writeByteArray(passwordArray)
   }

   override fun describeContents(): Int {
      return 0
   }

   companion object CREATOR : Parcelable.Creator<PDetailsEntites> {
      override fun createFromParcel(parcel: Parcel): PDetailsEntites {
         return PDetailsEntites(parcel)
      }

      override fun newArray(size: Int): Array<PDetailsEntites?> {
         return arrayOfNulls(size)
      }
   }
}
