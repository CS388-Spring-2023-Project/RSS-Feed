package com.example.myrssfeedapp.homePackage

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.myrssfeedapp.R
import com.example.myrssfeedapp.SharedViewModel
import okhttp3.*
import org.json.JSONObject
import java.io.IOException


class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val sharedVM = ViewModelProvider(this.requireActivity())[SharedViewModel::class.java]
        //get user subscriptions
        val services = sharedVM.getAvailableServices()
        Log.d("services home ", sharedVM.getAvailableServices().toString())
        // keep in mind that we have isSubscribed parameter
        // that we will use to pick user subscriptions
        for(service in services){
            if(service.isSubscribed) {
                val url = "${service.serviceURL}${service.serviceKEY}"
                val client = OkHttpClient()
                val requestBody = FormBody.Builder().build()
                val request = Request.Builder().url(url)
                    .build()
                client.newCall(request).enqueue(object:Callback{
                    override fun onFailure(call: Call, e: IOException) {
                        Log.d("result","Failure")
                        Log.d("exception",e.toString())
                    }

                    override fun onResponse(call: Call, response: Response) {
                        Log.d("result", "Success")
                        val jsonResult = response.body?.string()?.let { it1 -> JSONObject(it1) }
                        Log.d("jsonResult", jsonResult.toString())
                        if(jsonResult != null){

                        }
                    }
                })
            }
            else{
                Log.d("status","I am not in")
            }
        }
        return view
    }
}