package com.example.notespasswordmanagerappandroid.NavigationFragments

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import com.example.notespasswordmanagerappandroid.KeyStoreWrapper
import com.example.notespasswordmanagerappandroid.R
import com.example.notespasswordmanagerappandroid.Room.MyRoom
import com.example.notespasswordmanagerappandroid.Room.PDetailsEntites
import com.example.notespasswordmanagerappandroid.databinding.ActivityNavigationFragmentsBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NavigationFragmentsActivity : AppCompatActivity()
{
    var binding:ActivityNavigationFragmentsBinding?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityNavigationFragmentsBinding.inflate(LayoutInflater.from(this))
        setContentView(binding?.root)

        //KeyStoreWrapper.createAndroidKeyStoreAsymmetricKey("MASTER_KEY")


        supportActionBar?.hide()

        setSupportActionBar(binding?.toolbarr)

       var actionBarDrawerToggle= ActionBarDrawerToggle(this,binding?.drawerLayout,binding?.toolbarr,R.string.NavigationOpen,R.string.NavigationClose)
        binding?.drawerLayout?.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()


        var pdetails=PDetailsEntites("Hello","My Password","Googleemmmmmmm","Mandeep singh","123456789","kjfdjfjdkfjd".toByteArray())

        CoroutineScope(Dispatchers.Default).launch {

               // MyRoom.getInstance(applicationContext).getDaoInterface().insert(pdetails)

            var arrayList= MyRoom.getInstance(applicationContext).getDaoInterface().query().forEach {

               /*  runOnUiThread {
                     Log.d("datbase DATA",it.Account)
                     Toast.makeText(applicationContext, "${it.id}p", Toast.LENGTH_LONG).show()
                 }*/
             }
            //update
           // MyRoom.getInstance(applicationContext).getDaoInterface().update(pdetails)

            }


    }
}