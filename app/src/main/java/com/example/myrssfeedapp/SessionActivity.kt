package com.example.myrssfeedapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.myrssfeedapp.SettingsPackage.Service
import com.example.myrssfeedapp.SettingsPackage.Subscription
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.json.JSONArray

class SessionActivity : AppCompatActivity() {
    private lateinit var bottomNavigation: BottomNavigationView
    private lateinit var navigationController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_session)

        bottomNavigation = findViewById(R.id.bottom_navigation)
        navigationController = findNavController(R.id.fragment)

        bottomNavigation.setupWithNavController(navigationController)

        val sharedViewModel = ViewModelProvider(this)[SharedViewModel::class.java]
        val userID = intent.getIntExtra("myID",0)
        val userFirstName = intent.getStringExtra("firstName")
        val userLastName = intent.getStringExtra("lastName")
        //Available services
        val availableServicesStr = intent.getStringExtra("availableServices")
        val availableServices = JSONArray(availableServicesStr)
        Log.d("avlServicesSession",availableServices.length().toString())
        for (i in 0 until availableServices.length()){
            val serviceID = availableServices.getJSONObject(i).getInt("serviceID")
            val serviceName = availableServices.getJSONObject(i).getString("serviceName")
            val serviceURL = availableServices.getJSONObject(i).getString("serviceURL")
            val serviceKEY = availableServices.getJSONObject(i).getString("serviceKEY")
            val service = Service(serviceID,serviceName,serviceURL,serviceKEY,false)
            sharedViewModel.setAvailableServices(service)
        }
        //user subscriptions
        val userSubscriptionsStr = intent.getStringExtra("userSubscriptions")
        val userSubscriptions = JSONArray(userSubscriptionsStr)
        //Log.d("userSubscriptions",userSubscriptions.length().toString())
        for (i in 0 until userSubscriptions.length()){
            val subscriptionID = userSubscriptions.getJSONObject(i).getInt("subscriptionID")
            val serviceName = userSubscriptions.getJSONObject(i).getString("serviceName")
            val subscription = Subscription(subscriptionID,serviceName)
            sharedViewModel.updateService(serviceName)
            sharedViewModel.setUserSubscription(subscription)
        }
        if(userFirstName != null && userLastName != null){
            sharedViewModel.setUserInfos(userID,userFirstName,userLastName)
        }
    }
}