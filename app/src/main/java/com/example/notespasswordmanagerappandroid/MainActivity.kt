package com.example.notespasswordmanagerappandroid

import android.R.attr
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import com.example.notespasswordmanagerappandroid.databinding.ActivityMainBinding
import java.util.concurrent.Executor

import android.R.attr.phoneNumber
import android.content.ContentValues.TAG
import android.util.Log
import androidx.arch.core.executor.TaskExecutor
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
import kotlin.math.sign


class MainActivity : AppCompatActivity() {
    lateinit var binding:ActivityMainBinding
    lateinit var auth:FirebaseAuth
    var smsCODE:String?=null
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private var storedVerificationId: String? = ""

    lateinit var googleSignInClient:GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(LayoutInflater.from(applicationContext))
        val view=binding.root
        setContentView(view)

        /**Add the following initialization code to your app so that it runs before you use any other Firebase SDKs:*/
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

        binding.GetOtpButton.setOnClickListener {
           /* binding.OTP.isEnabled=true
            binding.OTP.visibility= View.VISIBLE

            binding.signinButton.isEnabled=true
            binding.signinButton.visibility=View.VISIBLE*/
          //  var otp=binding.phoneNumberEdittext.text.toString()
            //var credentialss=PhoneAuthProvider.getCredential(storedVerificationId!!,otp)
           // signInWithPhoneAuthCredential(credentialss)

        }

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        signIn()

      /*  val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber("+919650226920")       // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this)                 // Activity (for callback binding)
            .setCallbacks(object:PhoneAuthProvider.OnVerificationStateChangedCallbacks(){

                //when app automatically recognize otp therefore this method called .
                override fun onVerificationCompleted(credentials: PhoneAuthCredential) {
               Log.d("onVerifiedAutomaticall ","onVerificationCompleted called")
               signInWithPhoneAuthCredential(credentials)

                }
                //when app doesnt't automatically recognize otp therefore this method called .
                override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                    super.onCodeSent(p0, p1)

                    storedVerificationId = p0
                }

                override fun onVerificationFailed(p0: FirebaseException) {
                    Log.d("PHONEFAILEDVERFICATION",p0.localizedMessage)
                }



            })          // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)

*/

    }

  /*  private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("SIGNINSUCCEESS", "signInWithCredential:success")

                } else {
                    // Sign in failed, display a message and update the UI
                    Log.w("sigininFAIL", "signInWithCredential:failure", task.exception)
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                    }
                    // Update UI
                }
            }
    }*/

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, 100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == 100) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)

            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e)
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser

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
           Log.d("DATAUSERRS",currentUser.displayName!!+currentUser.email!!+currentUser.phoneNumber!!+currentUser.photoUrl!!)
       }
    }




}