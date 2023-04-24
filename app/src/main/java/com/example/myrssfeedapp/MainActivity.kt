package com.example.myrssfeedapp

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import com.example.myrssfeedapp.Room.ArticleViewModel
import com.example.myrssfeedapp.settingsPackage.BaseActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class MainActivity : BaseActivity(){

    private lateinit var emailContainer : EditText
    private lateinit var passwordContainer : EditText
    private lateinit var noAccount : TextView
    private lateinit var login : Button
    private lateinit var changeThemeBT: Button
    private lateinit var forgotPassword : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // BASEACTIVITY TRY...
        setTheme()
        setContentView(R.layout.activity_main)
        //Initialize our variables
        emailContainer = findViewById(R.id.email)
        passwordContainer = findViewById(R.id.password)
        noAccount = findViewById(R.id.noAccountButton)
        login = findViewById(R.id.loginButton)
        forgotPassword = findViewById(R.id.forgotPassword)
        changeThemeBT = findViewById(R.id.btChangeTheme)

        val helperObject = HelperClass()
//------------------------------------- CHANGE THEME-----------------------------------------------
        changeThemeBT.setOnClickListener{
            val dialog1 = BottomSheetDialog(this)
            val themeView = layoutInflater.inflate(R.layout.change_theme,null)
            val blueTheme:Button = themeView.findViewById(R.id.blueTheme)
            val pinkTheme:Button = themeView.findViewById(R.id.pinkTheme)
            val yellowTheme:Button = themeView.findViewById(R.id.yellowTheme)
            val greenTheme:Button = themeView.findViewById(R.id.greenTheme)
            val cancelTheme:Button = themeView.findViewById(R.id.cancelTheme)
            blueTheme.setOnClickListener{
                Log.d("BlueTheme","Blue Theme buttom was clicked")
                switchTheme(R.style.Theme_MyRssFeedAppBlue)
                //forgotPassword.setTextColor(getResources().getColor(R.color.brightBlue));
                recreate()
                dialog1.dismiss()
                Log.d("ThemeChange", "after dismiss")
            }
            yellowTheme.setOnClickListener{
                Log.d("BlueTheme","Blue Theme buttom was clicked")
                switchTheme(R.style.Theme_MyRssFeedApp)
                recreate()
                dialog1.dismiss()
                Log.d("ThemeChange", "after dismiss")
            }
            pinkTheme.setOnClickListener{
                Log.d("BlueTheme","Blue Theme buttom was clicked")
                switchTheme(R.style.Theme_MyRssFeedApp2)
                recreate()
                dialog1.dismiss()
                Log.d("ThemeChange", "after dismiss")
            }
            greenTheme.setOnClickListener{
                Log.d("BlueTheme","Blue Theme buttom was clicked")
                switchTheme(R.style.Theme_MyRssFeedAppGreen)
                recreate()
                dialog1.dismiss()
                Log.d("ThemeChange", "after dismiss")
            }
            cancelTheme.setOnClickListener {
                dialog1.dismiss()
            }
            dialog1.setCancelable(false)
            dialog1.setContentView(themeView)
            dialog1.show()
        }

//------------------------------------END CHANGE THEME----------------------------------------

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
                                val userID = jsonResult.getInt("userID")
                                val userFirstName = jsonResult.getString("firstName")
                                val userLastName = jsonResult.getString("lastName")
                                val availableServices = jsonResult.getJSONArray("availableServices")
                                val userSubscriptions = jsonResult.getJSONArray("userSubscriptions")
                                Log.d("availableServices",availableServices.toString())
                                intent.putExtra("userID",userID)
                                intent.putExtra("firstName",userFirstName)
                                intent.putExtra("lastName",userLastName)
                                intent.putExtra("availableServices",availableServices.toString())
                                intent.putExtra("userSubscriptions",userSubscriptions.toString())
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