package com.example.myrssfeedapp

import android.content.Intent
import android.util.Log
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class NetworkingClass {
    val backendURL = "http://10.85.136.38/RssFeed/backend.php"
    var jsonResult :JSONObject? = null

    fun login(email:String,password:String): JSONObject? {
        val client = OkHttpClient()
        val requestBody = FormBody.Builder()
            .add("signIn","")
            .add("email",email)
            .add("password",password)
            .build()
        val request = Request.Builder().url(backendURL).method("POST",requestBody).build()
        client.newCall(request).enqueue(object: Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("result","failure")
                Log.d("exception",e.toString())
            }

            override fun onResponse(call: Call, response: Response) {
                //Log.d("result","success")
                jsonResult = response.body?.string()?.let { it1 -> JSONObject(it1) }
            }
        })
        return jsonResult
    }
}