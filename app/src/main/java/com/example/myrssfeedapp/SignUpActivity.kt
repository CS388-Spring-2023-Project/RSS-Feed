package com.example.myrssfeedapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class SignUpActivity : AppCompatActivity() {

    private lateinit var fnameContainer : EditText
    private lateinit var lnameContainer : EditText
    private lateinit var emailContainer : EditText
    private lateinit var passwordContainer : EditText
    private lateinit var cPasswordContainer : EditText
    private lateinit var secretCode : EditText
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
        secretCode = findViewById(R.id.secretCode)
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
                !helperObject.emptyField(secretCode) &&
                helperObject.areIdentical(passwordContainer,cPasswordContainer) &&
                helperObject.isValidEmail(emailContainer)){

                //Do networking
                val client = OkHttpClient()
                val requestBody = FormBody.Builder()
                    .add("signUp","")
                    .add("firstName",fnameContainer.text.toString())
                    .add("lastName",lnameContainer.text.toString())
                    .add("email",emailContainer.text.toString())
                    .add("password",passwordContainer.text.toString())
                    .add("secretCode",secretCode.text.toString())
                    .build()
                val request = Request.Builder().url(helperObject.backendURL).method("POST",requestBody).build()
                client.newCall(request).enqueue(object: Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        Log.d("result","failure")
                        Log.d("exception",e.toString())
                    }

                    override fun onResponse(call: Call, response: Response) {
                        //Log.d("result","success")
                        val jsonResult = response.body?.string()?.let { it1 -> JSONObject(it1) }
                        Log.d("jsonResult",jsonResult.toString())
                        if (jsonResult != null) {
                            if(jsonResult.getInt("error") == 0){
                                this@SignUpActivity.runOnUiThread {
                                    Toast.makeText(this@SignUpActivity,"This Email is already exist",
                                        Toast.LENGTH_LONG).show()
                                }
                            } else if(jsonResult.getInt("error") == 2){
                                Log.d("error","Error saving this record")
                                this@SignUpActivity.runOnUiThread {
                                    Toast.makeText(this@SignUpActivity,"Error saving this record",
                                        Toast.LENGTH_LONG).show()
                                }
                            }
                            else if(jsonResult.getInt("error") == 1){
                                this@SignUpActivity.runOnUiThread {
                                    Toast.makeText(this@SignUpActivity,"Record is created successfully",
                                        Toast.LENGTH_LONG).show()
                                }
                                val intent = Intent(this@SignUpActivity,SessionActivity::class.java)
                                intent.putExtra("myID",jsonResult.getInt("userID"))
                                Log.d("MyID signIN",jsonResult.getInt("userID").toString())
                                startActivity(intent)
                                finish()
                            }
                        }
                    }
                })
            }
        }
    }
}