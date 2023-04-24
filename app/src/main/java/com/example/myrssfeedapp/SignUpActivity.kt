package com.example.myrssfeedapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.myrssfeedapp.settingsPackage.BaseActivity
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class SignUpActivity : BaseActivity() {

    private lateinit var fnameContainer : EditText
    private lateinit var lnameContainer : EditText
    private lateinit var emailContainer : EditText
    private lateinit var passwordContainer : EditText
    private lateinit var cPasswordContainer : EditText
    private lateinit var haveAccount : TextView
    private lateinit var signUp : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme()
        setContentView(R.layout.activity_sign_up)
        switchTheme(curTheme())
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

                //Do networking
                val client = OkHttpClient()
                val requestBody = FormBody.Builder()
                    .add("signUp","")
                    .add("firstName",fnameContainer.text.toString())
                    .add("lastName",lnameContainer.text.toString())
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
                                //Toast.makeText(this@SignUpActivity,"This Email is already exist",Toast.LENGTH_LONG).show()
                            } else if(jsonResult.getInt("error") == 2){
                                Log.d("error","Error saving this record")
                                //Toast.makeText(applicationContext,"Error saving this record",Toast.LENGTH_LONG).show()
                            }
                            else if(jsonResult.getInt("error") == 1){
                                val intent = Intent(this@SignUpActivity,SessionActivity::class.java)
                                intent.putExtra("myID",jsonResult.getInt("userID"))
                                Log.d("MyID signIN",jsonResult.getInt("userID").toString())
                                //Toast.makeText(applicationContext,"User created successfully",Toast.LENGTH_LONG).show()
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