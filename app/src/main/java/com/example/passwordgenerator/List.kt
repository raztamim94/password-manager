package com.example.passwordgenerator

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*


class List : AppCompatActivity() {

    private lateinit var back: Button

    private  lateinit var dbref: DatabaseReference
    private  lateinit var userRecyclerView: RecyclerView
    private  lateinit var userArrayList: ArrayList<User>
    private lateinit var adapter: MyAdapter

    private fun getUserData(){
        dbref = FirebaseDatabase.getInstance().getReference("Users")
        dbref.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                if(snapshot.exists()){
                    for(userSnapshot in snapshot.children){
                        val user = userSnapshot.getValue(User::class.java)
                        if (user != null) {
                            if(!userArrayList.contains(user.id)){
                                userArrayList.add(user!!)
                            }
                        }

                    }

                    userRecyclerView.adapter = MyAdapter(userArrayList)
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }


        })

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)


        userArrayList = arrayListOf<User>()

        userRecyclerView = findViewById(R.id.userlist)
        userRecyclerView.layoutManager = LinearLayoutManager(this)
        userRecyclerView.setHasFixedSize(true)
        adapter = MyAdapter(userArrayList)
        userRecyclerView.adapter = adapter


        getUserData()
        back = findViewById(R.id.back)
        back.setOnClickListener() {
            val intent = Intent(this, Generator::class.java)
            startActivity(intent)
        }


    }

}