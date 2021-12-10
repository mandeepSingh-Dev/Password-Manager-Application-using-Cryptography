package com.example.notespasswordmanagerappandroid

import android.app.Activity
import android.content.Context
import android.util.Base64
import android.util.Log
import java.lang.Exception
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class MyCryptography
{

    companion object{
        fun generateSecretKey(): SecretKey
        {
            var keyGeneretor: KeyGenerator = KeyGenerator.getInstance("AES")
            keyGeneretor.init(256)
            var key: SecretKey =keyGeneretor.generateKey()
            return key
        }
        var keyey= generateSecretKey()


        fun encrypting(password:String,secretKey: SecretKey):String
        {
            try {
                var cipher = Cipher.getInstance("AES")
                var keySpec: SecretKeySpec = SecretKeySpec(secretKey.encoded, "AES")
                cipher.init(Cipher.ENCRYPT_MODE, keySpec)

                Log.d("kkkkkkkkEN",cipher.doFinal(password.toByteArray()).toString())
                var encryptedString = Base64.encodeToString(cipher.doFinal(password.toByteArray(Charsets.UTF_8)),Base64.DEFAULT)
                Log.d("kkkkkkkkEN",cipher.doFinal(password.toByteArray()).toString())

                return encryptedString
            }catch (e:Exception){}
            return null!!


        }
        fun decrypting(encryptedString:String, secretKey: SecretKey):String
        {

            val bytes = ByteArray(8192)
            var secureRandom=SecureRandom()
            secureRandom.nextBytes(bytes)
            try {
                var cipher = Cipher.getInstance("AES")
                var keyspec: SecretKeySpec = SecretKeySpec(secretKey.encoded, "AES")
                var IVspec=IvParameterSpec(bytes)
                cipher.init(Cipher.DECRYPT_MODE, keyspec,IVspec)

                var decryptedByteArray = String(cipher.doFinal(Base64.decode(encryptedString,Base64.DEFAULT)),Charsets.UTF_8)

                  return decryptedByteArray
            }catch (e:Exception){
                Log.d("cryptMESSAGE",e.printStackTrace().toString()+e.localizedMessage)
            }
            return null!!

        }

        fun encryptingDUPLICATE(password:String,secretKey: SecretKey):String
        {
            try {
                var cipher = Cipher.getInstance("AES")
                var keySpec: SecretKeySpec = SecretKeySpec(secretKey.encoded, "AES")
                cipher.init(Cipher.ENCRYPT_MODE, keySpec)
                  var encyptedByteArray=cipher.doFinal(password.toByteArray())
                return String(encyptedByteArray)
            }catch (e:Exception){}
            return null!!
        }
        fun decryptingDUPLICATE(encryptedString:String, secretKey: SecretKey):String
        {

            try {
                var cipher = Cipher.getInstance("AES")
                var keyspec: SecretKeySpec = SecretKeySpec(secretKey.encoded, "AES")
                cipher.init(Cipher.DECRYPT_MODE, keyspec)
                 var decryptedByteArray=cipher.doFinal(encryptedString.toByteArray())
                return String(decryptedByteArray)
            }catch (e:Exception){
                Log.d("cryptMESSAGE",e.printStackTrace().toString()+e.localizedMessage)
            }
            return null!!

        }
    }
}