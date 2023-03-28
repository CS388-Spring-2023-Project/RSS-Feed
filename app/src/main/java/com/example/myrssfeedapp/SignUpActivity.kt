package com.example.myrssfeedapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class SignUpActivity : AppCompatActivity() {

    private lateinit var fnameContainer : EditText
    private lateinit var lnameContainer : EditText
    private lateinit var emailContainer : EditText
    private lateinit var passwordContainer : EditText
    private lateinit var cPasswordContainer : EditText
    private lateinit var haveAccount : TextView
    private lateinit var signUp : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        //Initialize our variables
        fnameContainer = findViewById(R.id.firstName)
        lnameContainer = findViewById(R.id.lastName)
        emailContainer = findViewById(R.id.email)
        passwordContainer = findViewById(R.id.password)
        cPasswordContainer = findViewById(R.id.cPassword)
        haveAccount = findViewById(R.id.haveAccountButton)
        signUp = findViewById(R.id.signUpButton)

        val helperObject = HelperClass()
        //Already have an account?
        haveAccount.setOnClickListener{
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }

        //signUp
        signUp.setOnClickListener{
            if(!helperObject.emptyField(fnameContainer) &&
                !helperObject.emptyField(lnameContainer) &&
                !helperObject.emptyField(emailContainer) &&
                !helperObject.emptyField(passwordContainer) &&
                !helperObject.emptyField(cPasswordContainer) &&
                helperObject.areIdentical(passwordContainer,cPasswordContainer) &&
                helperObject.isValidEmail(emailContainer)){

                println("hello")
                //Do networking
            }
        }
    }
}