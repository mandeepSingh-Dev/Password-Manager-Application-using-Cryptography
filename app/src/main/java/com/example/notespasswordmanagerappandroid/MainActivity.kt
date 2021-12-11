package com.example.notespasswordmanagerappandroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import com.example.notespasswordmanagerappandroid.databinding.ActivityMainBinding

import android.content.ContentValues.TAG
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContract
import androidx.arch.core.executor.TaskExecutor
import androidx.core.app.ActivityOptionsCompat
import com.example.notespasswordmanagerappandroid.Authentications.MyphoneAuthentication
import com.example.notespasswordmanagerappandroid.NavigationFragments.NavigationFragmentsActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.TaskExecutors
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseException
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.safetynet.SafetyNetAppCheckProviderFactory
import com.google.firebase.auth.*

import java.util.concurrent.TimeUnit
import com.google.firebase.auth.PhoneAuthProvider

import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.lang.Exception
import kotlin.math.sign


class MainActivity : AppCompatActivity() {
    lateinit var binding:ActivityMainBinding
    lateinit var auth:FirebaseAuth
    var smsCODE:String?=null
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private var storedVerificationId: String? = ""
    public var con:Boolean=false

    lateinit var googleSignInClient:GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(LayoutInflater.from(applicationContext))
        val view=binding.root
        setContentView(view)

        /**Add the following initialization code to your app so that it runs before you use any other Firebase SDKs:*/
       //below code is to check or verify that user is not robot
        FirebaseApp.initializeApp(/*context=*/this)
        val firebaseAppCheck = FirebaseAppCheck.getInstance()
        firebaseAppCheck.installAppCheckProviderFactory(SafetyNetAppCheckProviderFactory.getInstance())

          supportActionBar?.hide()
          auth=FirebaseAuth.getInstance()
       /* if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.displayCutout())
        }
        else
        {
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }*/

        binding?.signinButton?.setOnClickListener {
            val phoneNumber=binding.phonenumberINPUTLAYOUT.editText?.text.toString()
            Log.d("phonenumber", phoneNumber)

            if(validatePhonenumber(phoneNumber)) {
                //MyphoneAuthentication.settingPhoneAuth(this, phoneNumber)
                    settingPhoneAuth(phoneNumber)

                //phonenumber edittextinputlayout gone on signin click
                binding.phonenumberINPUTLAYOUT.isEnabled = false
                binding.phonenumberINPUTLAYOUT.visibility = View.GONE
                //otp edittextinputlayout Visible on signin click
                binding.otpINPUTLAYOUT.isEnabled = true
                binding.otpINPUTLAYOUT.visibility = View.VISIBLE
                //getOTPButton enabled and Visible on signin click
                binding.GetOtpButton.isEnabled = true
                binding.GetOtpButton.visibility = View.VISIBLE
                Log.d("CONN_rrrrrrrrr",con.toString()+"f")

            }

        }
        binding.GetOtpButton.setOnClickListener {

            val otp=binding.otpINPUTLAYOUT.editText?.text.toString()
            if(validateOTP(otp))
            {
                try {
                    val phoneAuthCREDENTIAL = PhoneAuthProvider.getCredential(storedVerificationId!!, otp)
                    //MyphoneAuthentication.signInWithPhoneAuthCredential(this, phoneAuthCREDENTIAL)
                signInWithPhoneAuthCredential(phoneAuthCREDENTIAL)
                }catch (e:Exception){
                    Toast.makeText(this,e.cause?.message+"f",Toast.LENGTH_LONG).show()
                }
               /* if (con)
                {
                    Log.d("CONN__dfhjdhfjdhfjdhf",con.toString()+"f")
                val intent = Intent(this, NavigationFragmentsActivity::class.java)
               *//* intent.putExtra("USERNAME", auth.currentUser?.displayName)
                intent.putExtra("PHOTOURL", auth.currentUser?.photoUrl.toString())
                intent.putExtra("PHONENUMBER", auth.currentUser?.phoneNumber)
                intent.putExtra("EMAIL", auth.currentUser?.email)*//*
                startActivity(intent)
                finish()
                 }*/
            }
        }

        binding?.googleloginButton.setOnClickListener {
            settingGoogleMailSIGNIN()
        }


    }//end of onCreate()



    fun settingGoogleMailSIGNIN()
    {
        //getting registered gmail accounts in user phone
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
             //getting client that user clicked on account on accounts list
        googleSignInClient = GoogleSignIn.getClient(this, gso)
        //now sign in
        signIn()
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        signInIntent.putExtra("ResultCode",100)
        launcher.launch(signInIntent)
    }

    var contract=object:ActivityResultContract<Intent,Intent>(){
        override fun createIntent(context: Context, input: Intent?): Intent {
            return input!!
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Intent {
                    return intent!!
        }
    }//end of ActivityResultContract
    var launcher=registerForActivityResult(contract,object:ActivityResultCallback<Intent>{
        override fun onActivityResult(result: Intent?) {
            if(result?.getIntExtra("ResultCode",0)==0)
            {
                Log.d("SUCCESS",result.getIntExtra("ResultCode",0).toString())
                    val task = GoogleSignIn.getSignedInAccountFromIntent(result)
                       if(task.isSuccessful) {
                           // Google Sign In was successful, authenticate with Firebase
                           val signedInaccount = task.getResult(ApiException::class.java)!!
                           Log.d(TAG, "firebaseAuthWithGoogle:" + signedInaccount.id)
                           firebaseAuthWithGoogle(signedInaccount.idToken!!)


                       }
                   else{
                       Toast.makeText(applicationContext,task.exception.cause.toString(),Toast.LENGTH_LONG).show()
                   }
                }
        }

    })//end of registerforActivityResult

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser
                    val intent=Intent(this,NavigationFragmentsActivity::class.java)
                    intent.putExtra("USERNAME",user?.displayName)
                    intent.putExtra("PHOTOURL",user?.photoUrl.toString())
                    intent.putExtra("PHONENUMBER",user?.phoneNumber)
                    intent.putExtra("EMAIL",user?.email)
                    startActivity(intent)
                    finish()


                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                }
            }
    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
       if(currentUser!=null)
       {
           val intent=Intent(this,NavigationFragmentsActivity::class.java)
           intent.putExtra("USERNAME",currentUser.displayName)
           intent.putExtra("PHOTOURL",currentUser.photoUrl.toString())
           intent.putExtra("PHONENUMBER",currentUser.phoneNumber)
           intent.putExtra("EMAIL",currentUser.email)
           startActivity(intent)
           finish()
       }
    }

    fun validatePhonenumber(phonenumber:String):Boolean
    {
        if(phonenumber.length<10)
        {
            binding?.phonenumberINPUTLAYOUT?.error="Phone number is not valid!"
            binding?.phonenumberINPUTLAYOUT.isErrorEnabled=true
            binding?.phonenumberINPUTLAYOUT.setErrorTextColor(ColorStateList.valueOf(Color.RED))
            return false
        }
        else{
            binding?.phonenumberINPUTLAYOUT.isErrorEnabled=false
            return true
        }
    }
    fun validateOTP(phonenumber:String):Boolean
    {
        if(phonenumber.length<6)
        {
            binding?.otpINPUTLAYOUT?.error="wrong otp"
            binding?.otpINPUTLAYOUT.isErrorEnabled=true
            binding?.otpINPUTLAYOUT.setErrorTextColor(ColorStateList.valueOf(Color.RED))
            return false
        }
        else{
            binding?.otpINPUTLAYOUT.isErrorEnabled=false
            return true
        }
    }
  //getting otp(smscode) and ...
    public fun settingPhoneAuth( phoneNumber:String)
    {
        var conn:Boolean=false
        Log.d("phonenumber", phoneNumber)

        val phoneauthoptions = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber("+91"+phoneNumber)       // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this)                 // Activity (for callback binding)
            .setCallbacks(object: PhoneAuthProvider.OnVerificationStateChangedCallbacks(){

                //when app automatically recognize otp therefore this method called .
                override fun onVerificationCompleted(credentials: PhoneAuthCredential) {
                    Log.d("onVerifiedAutomaticall ","onVerificationCompleted called")
                    signInWithPhoneAuthCredential( credentials)


                }
                //when app doesnt't automatically recognize otp therefore this method called .
                override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                    super.onCodeSent(p0, p1)
                    Log.d("onCodeSent ","onCodeSent called")

                    storedVerificationId =p0
                }

                override fun onVerificationFailed(p0: FirebaseException) {
                    Log.d("PHONEFAILEDVERFICATION",p0.localizedMessage)
                }
            })          // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(phoneauthoptions)

    }
         //signedIN here manually
    public fun signInWithPhoneAuthCredential( credential: PhoneAuthCredential)
    {

        auth.signInWithCredential(credential).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                Log.d("SIGNINSUCCEESS", "signInWithCredential:success")
                Toast.makeText(this, task.result.user?.phoneNumber, Toast.LENGTH_LONG).show()
               startActivity(Intent(this,NavigationFragmentsActivity::class.java))
            } else {
                // Sign in failed, display a message and update the UI
                Log.w("sigininFAIL", "signInWithCredential:failure", task.exception)
                Toast.makeText(this, task.exception.toString(), Toast.LENGTH_LONG).show()

                if (task.exception is FirebaseAuthInvalidCredentialsException) {
                    // The verification code entered was invalid
                }
                // Update UI

            }
        }

    }




}