package com.example.notespasswordmanagerappandroid.Splashscreen

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.WindowInsets
import android.view.WindowManager
import android.view.animation.AnimationUtils
import com.example.notespasswordmanagerappandroid.KeyStoreWrapper
import com.example.notespasswordmanagerappandroid.MainActivity
import com.example.notespasswordmanagerappandroid.NavigationFragments.NavigationFragmentsActivity
import com.example.notespasswordmanagerappandroid.R
import com.example.notespasswordmanagerappandroid.databinding.ActivitySplashScreenBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashScreen : AppCompatActivity() {

    lateinit var binding:ActivitySplashScreenBinding
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
         binding= ActivitySplashScreenBinding.inflate(LayoutInflater.from(applicationContext))
        val view=binding.root
        setContentView(view)



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
            window.insetsController?.hide(WindowInsets.Type.displayCutout())

        } else {
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }
        binding.notesImage.animation=AnimationUtils.loadAnimation(applicationContext,R.anim.splashscreen_anim_fromup)
       binding.passwordImage.animation=AnimationUtils.loadAnimation(applicationContext,R.anim.splashscreen_anim_fromdown)
        CoroutineScope(Dispatchers.Default).launch {
            delay(2000)

            val intent=Intent(applicationContext,MainActivity ::class.java)

               startActivity(intent)
            finish()
        }
    }

}