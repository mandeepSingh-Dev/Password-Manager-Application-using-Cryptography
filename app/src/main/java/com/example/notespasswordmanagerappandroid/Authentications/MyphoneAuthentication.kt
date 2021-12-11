package com.example.notespasswordmanagerappandroid.Authentications

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.example.notespasswordmanagerappandroid.MainActivity
import com.example.notespasswordmanagerappandroid.NavigationFragments.NavigationFragmentsActivity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import java.util.concurrent.TimeUnit

class MyphoneAuthentication
{
    companion object{
        var storedVerificationId:String?=null
        val auth=FirebaseAuth.getInstance()
        public fun settingPhoneAuth(thiss: MainActivity, phoneNumber:String)
        {
            Log.d("phonenumber", phoneNumber)

            val phoneauthoptions = PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber("+91"+phoneNumber)       // Phone number to verify
                .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                .setActivity(thiss)                 // Activity (for callback binding)
                .setCallbacks(object: PhoneAuthProvider.OnVerificationStateChangedCallbacks(){

                    //when app automatically recognize otp therefore this method called .
                    override fun onVerificationCompleted(credentials: PhoneAuthCredential) {
                        Log.d("onVerifiedAutomaticall ","onVerificationCompleted called")
                       signInWithPhoneAuthCredential(thiss,credentials)

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
        //This method used at two places but call only one time automatically or manually
        // AUTOMATICALLY- calls in onVeriicationCompleted when phone automatically fetched otp
        //MANUALLY- calls on click getOTPBUTTON when phone isnt able to fetch otp automatically
        public fun signInWithPhoneAuthCredential(thiss: MainActivity, credential: PhoneAuthCredential)
        {

            auth.signInWithCredential(credential).addOnCompleteListener(thiss) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("SIGNINSUCCEESS", "signInWithCredential:success")
                    Toast.makeText(thiss, task.result.user?.phoneNumber, Toast.LENGTH_LONG).show()
                } else {
                    // Sign in failed, display a message and update the UI
                    Log.w("sigininFAIL", "signInWithCredential:failure", task.exception)
                    Toast.makeText(thiss, task.exception.toString(), Toast.LENGTH_LONG).show()

                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                    }
                    // Update UI

                }
            }

        }
    }
}