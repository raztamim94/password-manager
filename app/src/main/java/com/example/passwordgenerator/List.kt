package com.example.passwordgenerator

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class List : AppCompatActivity() {

    lateinit var back: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        back = findViewById<Button>(R.id.back)
        back.setOnClickListener(){
            val intent = Intent(this, Generator::class.java)
            startActivity(intent)
        }



    }

    override fun  onBackPressed() { }
}