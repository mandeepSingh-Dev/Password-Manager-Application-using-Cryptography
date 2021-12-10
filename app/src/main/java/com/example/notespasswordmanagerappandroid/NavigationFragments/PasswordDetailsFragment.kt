package com.example.notespasswordmanagerappandroid.NavigationFragments

import android.content.*
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.Navigation
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import androidx.security.crypto.MasterKeys.AES256_GCM_SPEC
import com.example.notespasswordmanagerappandroid.*
import com.example.notespasswordmanagerappandroid.MyCryptography.Companion.encrypting
import com.example.notespasswordmanagerappandroid.MyCryptography.Companion.generateSecretKey
import com.example.notespasswordmanagerappandroid.Room.MyRoom
import com.example.notespasswordmanagerappandroid.Room.PDetailsEntites
import com.example.notespasswordmanagerappandroid.databinding.FragmentPasswordDetailsBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec


class PasswordDetailsFragment : Fragment()
{
    var title:String?=null
    var account:String?=null
    var username:String?=null
    var password:String?=null
    var weblink:String?=null

    var binding:FragmentPasswordDetailsBinding?=null
    var pDetailsEntites:PDetailsEntites?=null
    var key:SecretKey?=null


    val receiver=object:BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {

            Log.d("nkdnfkdfd",intent?.getStringExtra("string")+"ff")
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding=FragmentPasswordDetailsBinding.inflate(inflater)
         return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var navController=Navigation.findNavController(view)

        KeyStoreWrapper.createAndroidKeyStoreAsymmetricKey("MASTER_KEY")
        var masterKey = KeyStoreWrapper.getAndroidKeyStoreAsymmetricKeyPair("MASTER_KEY")
        masterKey?.public

        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(receiver, IntentFilter("SEND"))

        try {
         CoroutineScope(Dispatchers.Default).launch {
             var encryptedSharedPreferences = encryptedSharedprefrencees()

             Log.d("jdjnfndjfddd", encryptedSharedPreferences.getString("user_name", "nuull").toString()
               )
               Log.d("jdjnfndjfddd", encryptedSharedPreferences.getString("user_pin", "nuull").toString()
               )
           }
        }catch (e:Exception){Log.d("knkdmfnkdfmkdfmkdmfkd",e.message+"\n"+e.cause)}

       var stringPREFRENCE=activity?.getSharedPreferences("PREF",Context.MODE_PRIVATE)?.getString("passw",null)


           //AES encryption
        var key:SecretKey=MyCryptography.generateSecretKey()
        //RSA encryption
        var keypair=MyCryptoGraphy2.generateKeyPair()
       // var encryptedBytearray=MyCryptography.encrypting("Ramgarhia2@",key)
       // var key:SecretKey=MyCryptography.generateSecretKey()

        Log.d("KKEEY",key.encoded.toString())


        if(arguments!=null)
        {
             pDetailsEntites=arguments?.get("Passworddetails") as PDetailsEntites
            Log.d("ddkfkdfd",pDetailsEntites?.password!!+"d")
            binding?.textInputLayout1?.editText?.setText(pDetailsEntites?.title)
            binding?.textInputLayout2?.editText?.setText(pDetailsEntites?.Account)
            binding?.textInputLayout3?.editText?.setText(pDetailsEntites?.username)

            Log.d("KKEEY2",key.encoded.toString())
            Log.d("helloPPASDSKFN",pDetailsEntites?.password!!)
            Log.d("jgnkfngjkfgn",pDetailsEntites?.passwordArray.toString())

           /* var decrypted=CipherWrapper.decrypt(pDetailsEntites?.password!!,masterKey?.private!!)
            Log.d("decryptes",decrypted)*/
            Log.d("decryptedSIZE","${pDetailsEntites?.password!!.toByteArray().size}   l")


           /* var decryptes=MyCryptoGraphy2.RSADecrypt(pDetailsEntites?.passwordArray,keypair)
            Log.d("gnfkgnkf",decryptes+"fkgnfjngjfjgfjgjfgfjghfj")*/

           // Log.d("KKEEY3",MyCryptography.decrypting(pDetailsEntites?.password!!,key))
             // Toast.makeText(context, MyCryptography.decrypting(pDetailsEntites?.password?.trim()!!,key)+"dfdf", Toast.LENGTH_LONG).show()


            binding?.textInputLayout4?.editText?.setText(pDetailsEntites?.password!!)
            binding?.textInputLayout5?.editText?.setText(pDetailsEntites?.weblink)
        }



        binding?.saveButton?.setOnClickListener {
            title=binding?.textInputLayout1?.editText?.text.toString()
            account=binding?.textInputLayout2?.editText?.text.toString()
            username=binding?.textInputLayout3?.editText?.text.toString()
            password=binding?.textInputLayout4?.editText?.text.toString()
            weblink=binding?.textInputLayout5?.editText?.text.toString()


            var encrypted_PASSWORD= CipherWrapper.encrypt(password!!/*"HelloWorld"*/,masterKey?.public)
            Log.d("CIPHER ENCRYPTION",encrypted_PASSWORD+"fdfd")

            Log.d("encryptedSIZE","${encrypted_PASSWORD.toByteArray().size}   l")

            /** var encrypted_PASSWORD=MyCryptography.encrypting(password!!,key)
            Log.d("helloPPASDSKFN",encrypted_PASSWORD)

            Toast.makeText(context,password+encrypted_PASSWORD,Toast.LENGTH_LONG).show()
            Toast.makeText(context,MyCryptography.decrypting(encrypted_PASSWORD,key),Toast.LENGTH_LONG).show()*/
           // Log.d("KKEEY3",MyCryptography.decrypting(encrypted_PASSWORD,key))

          //  saveintoSharedPref(encrypted_PASSWORD)
            //encrypt by RSA
           /* var encrypted=MyCryptoGraphy2.RSAEncrypt(password,keypair)
            MyCryptoGraphy2.RSADecrypt(encrypted,keypair)
            Log.d("kgnfgnf", MyCryptoGraphy2.RSADecrypt(encrypted,keypair))*/



            var pDetailsEntites=PDetailsEntites(title!!,account!!,username!!,encrypted_PASSWORD,weblink!!,
                byteArrayOf(15))
             CoroutineScope(Dispatchers.Default).launch {
                 MyRoom.getInstance(activity?.applicationContext!!).getDaoInterface().insert(pDetailsEntites)
             }
            navController.navigate(R.id.passwordListsFragment)
        }

    }
   /* fun generateSecretKey():SecretKey
    {
        var keyGeneretor:KeyGenerator=KeyGenerator.getInstance("AES")
        keyGeneretor.init(256)
        var key:SecretKey=keyGeneretor.generateKey()
        return key
    }

    fun encrypting(password:String,secretKey: SecretKey):ByteArray
    {
        var cipher=Cipher.getInstance("AES")
        var keySpec:SecretKeySpec=SecretKeySpec(secretKey.encoded,"AES")
        cipher.init(Cipher.ENCRYPT_MODE,keySpec)
        var byteArrayy=password.toByteArray()
        var encryptedByteArray=cipher.doFinal(byteArrayy)
        return byteArrayy
    }*/

    fun encryptedSharedprefrencees():SharedPreferences
    {
        try {
            val keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC
            val masterKeyAlias = MasterKeys.getOrCreate(keyGenParameterSpec)

            var encryptedSharedPreferences = EncryptedSharedPreferences.create(
                "encrypted_preferenceeeees", // fileName
                masterKeyAlias, // masterKeyAlias
                requireContext(), // context
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV, // prefKeyEncryptionScheme
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM // prefvalueEncryptionScheme
            )
            encryptedSharedPreferences.edit().apply {
                putString("user_name", "Joseph Artega")
                putString("user_pin", "1450")
            }.apply()


            return encryptedSharedPreferences
        }catch (e:Exception){Log.d("dfkdnfjfgjfjgnf",e.message+"\n"+e.cause
        )
        }
        return null!!

    }
    fun saveintoSharedPref(string:String)
    {
        var sharedPreferences=activity?.getSharedPreferences("PREF",Context.MODE_PRIVATE)
        sharedPreferences?.edit()?.putString("passw",string)?.apply()
    }
}