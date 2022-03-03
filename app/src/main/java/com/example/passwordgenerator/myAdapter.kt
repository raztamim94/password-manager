package com.example.passwordgenerator

import android.app.AlertDialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase

class MyAdapter (private val userList: ArrayList<User>): RecyclerView.Adapter<MyAdapter.MyViewHolder>(){



    var isEdit = true

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_list,parent,false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentitem = userList[position]

        holder.username.text = currentitem.name
        holder.app.text = currentitem.app
        holder.password.text = currentitem.password

        holder.copy.setOnClickListener(){
            val clipboardManager = holder.itemView.context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText("text", currentitem.password)
            clipboardManager.setPrimaryClip(clipData)
            Toast.makeText(holder.itemView.context, "Text copied to clipboard", Toast.LENGTH_LONG).show()
        }
        holder.edit.setOnClickListener(){
            if(isEdit){
                holder.edit.text = "Update"
                holder.username.isEnabled = true
                holder.app.isEnabled = true
                holder.password.transformationMethod = HideReturnsTransformationMethod.getInstance()
                holder.password.isEnabled = true


                isEdit = false
            }

            else if(!isEdit){
                holder.edit.text = "Edit"
                holder.username.isEnabled = false
                holder.app.isEnabled = false
                holder.password.transformationMethod = PasswordTransformationMethod.getInstance()
                holder.password.isEnabled = false

                isEdit = true


                //update in firebase
                val user = mapOf(
                    "app" to currentitem.app.toString(),
                    "name" to currentitem.name.toString(),
                    "password" to currentitem.password.toString()


                )
                val  db = FirebaseDatabase.getInstance()
                val ref = db.getReference("Users")
                ref.child(currentitem.id.toString()).updateChildren(user)






            }

        }
        holder.delete.setOnClickListener(){
            val builder = AlertDialog.Builder(holder.itemView.context)
            builder.setMessage("Are you sure you want to Delete?")
                .setCancelable(false)
                .setPositiveButton("Yes") { _, _ ->
                    // Delete from firebase
                    val  db = FirebaseDatabase.getInstance()
                    val ref = db.getReference("Users")
                    ref.child(currentitem.id.toString()).removeValue().addOnCompleteListener(){
                        userList.removeAt(position)
                        notifyDataSetChanged()

                    }
                }
                .setNegativeButton("No") { dialog, _ ->
                    // Dismiss the dialog
                    dialog.dismiss()
                }
            val alert = builder.create()
            alert.show()
        }

    }

    override fun getItemCount(): Int {
        return userList.size
    }

    class  MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){


        val username : TextView = itemView.findViewById(R.id.username)
        val app : TextView = itemView.findViewById(R.id.app)
        val password : TextView = itemView.findViewById(R.id.password)

        val copy: Button = itemView.findViewById(R.id.copy)
        val notes: Button = itemView.findViewById(R.id.notes)
        val edit: Button = itemView.findViewById(R.id.edit)
        val delete: Button = itemView.findViewById(R.id.delete)



    }

}