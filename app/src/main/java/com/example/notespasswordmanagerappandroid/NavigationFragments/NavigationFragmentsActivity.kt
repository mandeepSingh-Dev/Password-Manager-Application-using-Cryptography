package com.example.notespasswordmanagerappandroid.NavigationFragments

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.DialogFragmentNavigatorDestinationBuilder
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.notespasswordmanagerappandroid.KeyStoreWrapper
import com.example.notespasswordmanagerappandroid.MainActivity
import com.example.notespasswordmanagerappandroid.R
import com.example.notespasswordmanagerappandroid.Room.MyRoom
import com.example.notespasswordmanagerappandroid.Room.PDetailsEntites
import com.example.notespasswordmanagerappandroid.databinding.ActivityNavigationFragmentsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

import com.google.android.gms.auth.api.signin.GoogleSignIn
import java.security.AccessController.getContext


class NavigationFragmentsActivity : AppCompatActivity()
{
    var binding:ActivityNavigationFragmentsBinding?=null
    var navController:NavController?=null
    var auth:FirebaseAuth?=null
    var currentUser:FirebaseUser?=null

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

        val fragmentManager=supportFragmentManager
        var navHostFragment=fragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController=navHostFragment.findNavController()

        auth= FirebaseAuth.getInstance()
        currentUser=auth?.currentUser


        var pdetails=PDetailsEntites("Hello","My Password","Googleemmmmmmm","Mandeep singh","123456789","kjfdjfjdkfjd".toByteArray())

        val username=intent.getStringExtra("USERNAME")
        val photoURL=intent.getStringExtra("PHOTOURL")
        val phoneNumber=intent.getStringExtra("PHONENUMBER")
        val email=intent.getStringExtra("EMAIL")

        binding?.navdrawer?.getHeaderView(0)?.findViewById<TextView>(R.id.Profile_Email)?.setText(email)
        binding?.navdrawer?.getHeaderView(0)?.findViewById<TextView>(R.id.Profile_Name)?.setText(username)

        val profilepic=binding?.navdrawer?.getHeaderView(0)?.findViewById<CircleImageView>(R.id.profile_pic)
       // Log.d("photofdfdfd",photoURL!!)
        Glide.with(this).load(photoURL).into(profilepic!!)

         binding?.navdrawer?.setNavigationItemSelectedListener {

             if(it.title.equals("Settings"))
             {}
             else if(it.title.equals("Log out"))
             {
                      if(currentUser!=null)
                      {
                          //firebase signout
                          auth?.signOut()
                          //google signout
                          GoogleSignIn.getClient(this, GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()).signOut()
                         // Firebase.auth.signOut()
                          Toast.makeText(this,"Logged out",Toast.LENGTH_LONG).show()
                          startActivity(Intent(this,MainActivity::class.java))
                      }
             }
            return@setNavigationItemSelectedListener true
         }
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