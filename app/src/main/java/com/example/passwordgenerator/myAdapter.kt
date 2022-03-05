package com.example.passwordgenerator

import android.annotation.SuppressLint
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
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase
import kotlin.collections.ArrayList


class MyAdapter (private val userList: ArrayList<User>): RecyclerView.Adapter<MyAdapter.MyViewHolder>(){



    private val  db = FirebaseDatabase.getInstance()
    val ref = db.getReference("Users")
    private var isEdit = true

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_layout,parent,false)
        return MyViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentUser = userList[position]

        holder.username.text = currentUser.name
        holder.app.text = currentUser.app
        holder.password.text = currentUser.password

        holder.copy.setOnClickListener(){
            val clipboardManager = holder.itemView.context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText("text", currentUser.password)
            clipboardManager.setPrimaryClip(clipData)
            Toast.makeText(holder.itemView.context, "Text copied to clipboard", Toast.LENGTH_LONG).show()
        }
        holder.notes.setOnClickListener(){
            val builder = AlertDialog.Builder(holder.itemView.context)
            val inflater: LayoutInflater = LayoutInflater.from(holder.itemView.context)
            val dialogLayout: View = inflater.inflate(R.layout.notes_dialogue,null)
            var editNotes: EditText = dialogLayout.findViewById(R.id.editNotes)

            editNotes.setText(currentUser.notes)

            with(builder){
                setTitle("Notes")
                setPositiveButton("OK"){_,_->
                    currentUser.notes = editNotes.text.toString()
                    ref.child(currentUser.id.toString()).child("notes").setValue(currentUser.notes)
                }
                setView(dialogLayout)
                show()
            }



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
                    "app" to holder.app.text.toString(),
                    "name" to holder.username.text.toString(),
                    "password" to holder.password.text.toString()
                )
                ref.child(currentUser.id.toString()).updateChildren(user).addOnSuccessListener(){
                    notifyItemChanged(position)
                }






            }

        }
        holder.delete.setOnClickListener(){
            val builder = AlertDialog.Builder(holder.itemView.context)
            builder.setMessage("Are you sure you want to Delete?")
                .setCancelable(false)
                .setPositiveButton("Yes") { _, _ ->
                    // Delete from firebase
                    ref.child(currentUser.id.toString()).removeValue().addOnCompleteListener(){
                        userList.removeAt(position)
                        notifyItemRemoved(position)



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