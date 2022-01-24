package com.example.passwordgenerator

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import android.widget.SeekBar
import com.google.firebase.database.FirebaseDatabase
import java.lang.StringBuilder

class Generator : AppCompatActivity() {

    val char_string = "abcdefghijklmnopqrstuvwxyz"
    val caps_string = "ABCDEFGHIJKMNLOPQRSTUVWXYZ"
    val number_string = "0123456789"
    val symbol_string = "~!@#$%^&*()_+-"


    // declare texts
    lateinit var username: EditText
    lateinit var app: EditText
    lateinit var password: TextView
    //declare switches
    lateinit var caps: Switch
    lateinit var numbers: Switch
    lateinit var symbols: Switch
    //declare seekbar
    lateinit var seekbarvalue: TextView
    lateinit var length: SeekBar
    //declare buttons
    lateinit var generate: Button
    lateinit var copy: Button
    lateinit var save: Button
    lateinit var list: Button
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_generator)
        // init texts
        username = findViewById<EditText>(R.id.username)
        app = findViewById<EditText>(R.id.app)
        password = findViewById<TextView>(R.id.password)
        // init switches
        caps = findViewById<Switch>(R.id.caps)
        numbers = findViewById<Switch>(R.id.numbers)
        symbols = findViewById<Switch>(R.id.symbols)
        // init seekbar
        seekbarvalue = findViewById<TextView>(R.id.seekbarvalue)
        length = findViewById<SeekBar>(R.id.length)
        // init buttons
        generate = findViewById<Button>(R.id.generate)
        copy = findViewById<Button>(R.id.copy)
        save = findViewById<Button>(R.id.save)
        list = findViewById<Button>(R.id.list)

        // seekbar listener
        length.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                seekbarvalue.text = length.progress.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}


        })
        //buttons logic
        generate.setOnClickListener(){
            var charset = StringBuilder()
            val string = StringBuilder(length.progress)

            charset.append(char_string)
            if(caps.isChecked)
                charset.append(caps_string)
            if(numbers.isChecked)
                charset.append(number_string)
            if(symbols.isChecked)
                charset.append(symbol_string)

            for(c in 0 until length.progress){
                val random = (charset.indices).random()
                string.append(charset[random])
            }

            password.text = string


        }
        save.setOnClickListener(){
            val  db = FirebaseDatabase.getInstance()
            val ref = db.getReference("Users")
            val user = User(username.text.toString(),app.text.toString(),password.text.toString(),"")

            ref.child(user.name.toString()).setValue(user).addOnSuccessListener {
                Toast.makeText(this, "password saved", Toast.LENGTH_LONG).show()
            }
                .addOnFailureListener{
                    Toast.makeText(this, "failed to save password", Toast.LENGTH_LONG).show()
                }

            //TODO("implement password encryption")



        }
        copy.setOnClickListener(){
            val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText("text", password.text)
            clipboardManager.setPrimaryClip(clipData)
            Toast.makeText(this, "Text copied to clipboard", Toast.LENGTH_LONG).show()
        }
        list.setOnClickListener(){
            val intent = Intent(this, List::class.java)
            startActivity(intent)
        }


    }



}




