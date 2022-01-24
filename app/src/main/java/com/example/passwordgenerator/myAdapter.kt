package com.example.passwordgenerator

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.recyclerview.widget.RecyclerView

class MyAdapter (private val userList: ArrayList<User>): RecyclerView.Adapter<MyAdapter.MyViewHolder>(){





    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_list,parent,false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentitem = userList[position]

        holder.username.text = currentitem.name
        holder.app.text = currentitem.app
        holder.password.text = currentitem.password
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    class  MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val username : TextView = itemView.findViewById(R.id.username)
        val app : TextView = itemView.findViewById(R.id.app)
        val password : TextView = itemView.findViewById(R.id.password)


    }

}