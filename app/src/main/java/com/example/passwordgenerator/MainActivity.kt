package com.example.passwordgenerator

import android.content.Intent
import androidx.biometric.BiometricPrompt
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import java.util.concurrent.Executor

class MainActivity : AppCompatActivity() {

    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo
    private lateinit var screen: EditText;
    private lateinit var login: TextView;
    private lateinit var password: Button;
    private  var masterPassword = "1234"

    //TODO("add password login")

    private lateinit var verify: Button


    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // password login process
        screen = findViewById(R.id.screen)
        login = findViewById(R.id.login)
        password = findViewById(R.id.password)

        password.setOnClickListener(){
            if(screen.text.toString()==masterPassword){
                goToGenerator()
            }

        }

        //fingerprint login process
        verify = findViewById(R.id.verify)
        executor = ContextCompat.getMainExecutor(this)
        biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int,
                                                   errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    Toast.makeText(applicationContext,
                        "Authentication error: $errString", Toast.LENGTH_SHORT)
                        .show()
                }
                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    goToGenerator()
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Toast.makeText(applicationContext, "Authentication failed",
                        Toast.LENGTH_SHORT)
                        .show()
                }
                // password login process


        })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric login")
            .setSubtitle("Log in using your biometric credential")
            .setNegativeButtonText("Use account password")
            .build()

        verify.setOnClickListener{
            biometricPrompt.authenticate(promptInfo)
        }
    }

    fun goToGenerator()
    {
        val intent = Intent(this, Generator::class.java)
        startActivity(intent)

    }




}