package com.example.notespasswordmanagerappandroid.Authentications

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContract
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.TypedArrayUtils.getString
import com.example.notespasswordmanagerappandroid.MainActivity
import com.example.notespasswordmanagerappandroid.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class MyGoogleFirebaseAuthentication(val thiss: MainActivity):AppCompatActivity()
{
    lateinit var auth:FirebaseAuth
        lateinit var googleSignInClient: GoogleSignInClient
        init {
             auth=FirebaseAuth.getInstance()
        }

    var contract=object: ActivityResultContract<Intent, Intent>(){
        override fun createIntent(context: Context, input: Intent?): Intent {
            return input!!
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Intent {
            return intent!!
        }
    }//end of ActivityResultContract
    var launcher=registerForActivityResult(contract,object: ActivityResultCallback<Intent> {
        override fun onActivityResult(result: Intent?) {
            if(result?.getIntExtra("ResultCode",0)==0)
            {
                Log.d("SUCCESS",result.getIntExtra("ResultCode",0).toString())
                val task = GoogleSignIn.getSignedInAccountFromIntent(result)
                if(task.isSuccessful) {
                    // Google Sign In was successful, authenticate with Firebase
                    val signedInaccount = task.getResult(ApiException::class.java)!!
                    Log.d(ContentValues.TAG, "firebaseAuthWithGoogle:" + signedInaccount.id)
                    firebaseAuthWithGoogle(signedInaccount.idToken!!)
                }
                else{
                    Toast.makeText(applicationContext,task.exception.message, Toast.LENGTH_LONG).show()
                }
            }
        }

    })//end of registerforActivityResult


    @SuppressLint("RestrictedApi")
    fun settingGoogleMailSIGNIN(context:Context)
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

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                Log.d(ContentValues.TAG, "signInWithCredential:success")
                val user = auth.currentUser

            } else {
                // If sign in fails, display a message to the user.
                Log.w(ContentValues.TAG, "signInWithCredential:failure", task.exception)
            }
        }
    }
    }