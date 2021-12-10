package com.example.notespasswordmanagerappandroid.NavigationFragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.security.crypto.MasterKeys
import com.example.notespasswordmanagerappandroid.*
import com.example.notespasswordmanagerappandroid.PswrdRecylerList.MyAdapter
import com.example.notespasswordmanagerappandroid.Room.MyRoom
import com.example.notespasswordmanagerappandroid.Room.PDetailsEntites
import com.example.notespasswordmanagerappandroid.databinding.FragmentPasswordListsBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.DisposableHandle
import kotlinx.coroutines.launch
import java.security.KeyPair
import java.security.MessageDigest
import javax.crypto.SecretKey


class PasswordListsFragment : Fragment()
{
    var binding:FragmentPasswordListsBinding?=null
    var secretKey:SecretKey?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding= FragmentPasswordListsBinding.inflate(inflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

       /* var keypair=MyCryptoGraphy2.generateKeyPair()
        var encryptedRSA=MyCryptoGraphy2.RSAEncrypt("HELLO MANDEEP",keypair)
        Log.d("fkgnfk",encryptedRSA.toString())
        var decryptedsTRIF=MyCryptoGraphy2.RSADecrypt(encryptedRSA,keypair)
        Log.d("fkgnfk",decryptedsTRIF)*/


        val masterkey=KeyStoreWrapper.getAndroidKeyStoreAsymmetricKeyPair("MASTER_KEY")
        Log.d("dfdfd",masterkey?.private.toString())


        val navController = Navigation.findNavController(view)
        /* if(secretKey!=null) {
              secretKey = MyCryptography.generateSecretKey()
              var sharedPreferences = activity?.getSharedPreferences("KEEY", Context.MODE_PRIVATE)
              var editor = sharedPreferences?.edit()
              editor?.putString("SCERETkey", secretKey.toString())
              editor?.apply()
          }*/

        //move to PasswordDetailsFragment
        binding?.floatingButton?.setOnClickListener {
            navController.navigate(R.id.passwordDetailsFragment)
            LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(Intent("SEND").putExtra("string","STRING"))

        }
        var myadapter:MyAdapter?=null
        var arraylist: ArrayList<PDetailsEntites>? = null
        CoroutineScope(Dispatchers.Default).launch {
            arraylist = MyRoom.getInstance(activity?.applicationContext!!).getDaoInterface().query() as ArrayList<PDetailsEntites>

            activity?.runOnUiThread {
                myadapter =
                    MyAdapter(activity?.applicationContext!!, arraylist!!, requireActivity()!!)
                binding?.passwordRecylerList?.layoutManager =
                    LinearLayoutManager(activity?.applicationContext!!)
                binding?.passwordRecylerList?.adapter = myadapter
                myadapter?.notifyDataSetChanged()

                myadapter?.setOnItemClickListener(object : MyAdapter.CustomItemViewClickListner {
                    override fun onItemClick(position: Int, itemView: View) {

                        var bundle:Bundle= Bundle()
                       // bundle.putParcelable("Passworddetails",arraylist?.get(position))
                       // navController.navigate(R.id.passwordDetailsFragment,bundle)

                        Log.d("aizze","${arraylist?.size!!} mfd")

                        val decrypt=CipherWrapper.decrypt(arraylist?.get(position)?.password!!,masterkey?.private)
                        Log.d("CIPHER",arraylist?.get(position)?.password!!)
                        Log.d("decryptedSIZE","${arraylist?.get(position)?.password!!.toByteArray().size}   l")
                        Log.d("dfdfd",decrypt)

                    }
                })

              //  Toast.makeText(requireContext(),arraylist?.size!!,Toast.LENGTH_SHORT).show()

            }//end of runOnUiThread

        }


            }//end of onViewCreated
        }








