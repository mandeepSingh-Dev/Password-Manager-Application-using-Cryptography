package com.example.notespasswordmanagerappandroid.PswrdRecylerList

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.notespasswordmanagerappandroid.R
import com.example.notespasswordmanagerappandroid.Room.MyRoom
import com.example.notespasswordmanagerappandroid.Room.PDetailsEntites
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class MyAdapter(val context: Context,val arraylist: ArrayList<PDetailsEntites>,val activity:Activity):RecyclerView.Adapter<MyAdapter.MyViewHolder>()
{
    var context1=context
   public var customItemViewClickListnr:CustomItemViewClickListner?=null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            var view= LayoutInflater.from(context).inflate(R.layout.password_list,parent,false)
            var viewHolder=MyViewHolder(view,context)
        return viewHolder
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if(arraylist!=null) {
            var pDetailsEntites: PDetailsEntites? = arraylist?.get(position)

            holder.buttonAlphabet?.setText(pDetailsEntites?.id.toString())
            holder.title?.setText(pDetailsEntites?.title)
            holder.username?.setText(pDetailsEntites?.username)
        }
    }

    override fun getItemCount(): Int {
       return arraylist.size
    }

     inner class MyViewHolder(itemView: View,contextview: Context):RecyclerView.ViewHolder(itemView)
    {

        var title:TextView?=null
        var username:TextView?=null
        var buttonAlphabet:Button?=null
     init {
        title=itemView.findViewById<TextView>(R.id.title)
        username= itemView.findViewById<TextView>(R.id.username)
         buttonAlphabet=itemView.findViewById<Button>(R.id.alphabet)


         //onLongClick delete items by showing alertDialogbox
         itemView.setOnLongClickListener{
            deleteWithShowingAlertDialog(adapterPosition)
             return@setOnLongClickListener true
         }//end of itemView.OnlONGClICK

            itemView.setOnClickListener {
                customItemViewClickListnr?.onItemClick(adapterPosition,itemView)
            }



     }
    }
    fun setOnItemClickListener(customItemViewClickListner: CustomItemViewClickListner)
    {
        customItemViewClickListnr=customItemViewClickListner
    }

    interface CustomItemViewClickListner
    {
        fun onItemClick(position: Int,itemView:View)
    }

    //alertDailogbox with deletion functionality
    fun deleteWithShowingAlertDialog(adapterPosition:Int){
        var alertDialogBuilder=AlertDialog.Builder(activity)
        alertDialogBuilder.setMessage("Delete?")
            .setTitle("")
            .setPositiveButton("Yes", object:DialogInterface.OnClickListener{
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    Toast.makeText(activity?.applicationContext!!, "${arraylist?.get(adapterPosition)?.id!!}", Toast.LENGTH_SHORT).show()
                    CoroutineScope(Dispatchers.Default).launch {
                        var pDetailsEntites = MyRoom.getInstance(activity?.applicationContext!!).getDaoInterface().query(arraylist?.get(adapterPosition)?.id!!)
                        MyRoom.getInstance(activity?.applicationContext!!).getDaoInterface().delete(pDetailsEntites)

                        activity?.runOnUiThread {
                            arraylist?.removeAt(adapterPosition)
                            notifyItemRemoved(adapterPosition)
                        }
                    }
                }
            })
            .setNegativeButton("Cancell",null)
        var alertDialog=alertDialogBuilder.create()
        alertDialog.show()
    }

}