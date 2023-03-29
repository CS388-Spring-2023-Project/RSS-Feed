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

class MainActivity : AppCompatActivity() {

    private lateinit var emailContainer : EditText
    private lateinit var passwordContainer : EditText
    private lateinit var noAccount : TextView
    private lateinit var login : Button
    private lateinit var forgotPassword : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Initialize our variables
        emailContainer = findViewById(R.id.email)
        passwordContainer = findViewById(R.id.password)
        noAccount = findViewById(R.id.noAccountButton)
        login = findViewById(R.id.loginButton)
        forgotPassword = findViewById(R.id.forgotPassword)

        val helperObject = HelperClass()
        //Do not have account
        noAccount.setOnClickListener{
            val intent = Intent(this,SignUpActivity::class.java)
            startActivity(intent)
        }

        login.setOnClickListener{
            //if all fields are not empty
            if(!helperObject.emptyField(emailContainer) &&
                !helperObject.emptyField(passwordContainer) &&
                helperObject.isValidEmail(emailContainer)){

                //do networking
                val client = OkHttpClient()
                val requestBody = FormBody.Builder()
                    .add("signIn","")
                    .add("email",emailContainer.text.toString())
                    .add("password",passwordContainer.text.toString())
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
                                //Log.d("error","Account not found")
                               // Toast.makeText(this@MainActivity.applicationContext,"record is not found",Toast.LENGTH_LONG)
                                 //   .show()
                            } else if(jsonResult.getInt("error") == 2){
                                Log.d("error","email or password incorrect")
//                                Toast.makeText(this@MainActivity,"Email or password incorrect",Toast.LENGTH_LONG)
//                                    .show()
                            }
                            else if(jsonResult.getInt("error") == 1){
                                val intent = Intent(this@MainActivity,SessionActivity::class.java)
                                intent.putExtra("myID",jsonResult.getInt("userID"))
                                Log.d("MyID signIN",jsonResult.getInt("userID").toString())
                                //sharedViewModel.setMyID(jsonObject.getInt("userID"))
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