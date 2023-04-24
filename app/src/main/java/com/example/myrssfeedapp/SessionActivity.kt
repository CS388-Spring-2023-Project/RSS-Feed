package com.example.myrssfeedapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.myrssfeedapp.settingsPackage.BaseActivity
import com.example.myrssfeedapp.settingsPackage.Service
import com.example.myrssfeedapp.settingsPackage.Subscription
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.json.JSONArray

class SessionActivity : BaseActivity() {
    private lateinit var bottomNavigation: BottomNavigationView
    private lateinit var navigationController: NavController
    private lateinit var btTheme: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme()
        setContentView(R.layout.activity_session)
        switchTheme(curTheme())
        bottomNavigation = findViewById(R.id.bottom_navigation)
        navigationController = findNavController(R.id.fragment)
        bottomNavigation.setupWithNavController(navigationController)

        val sharedViewModel = ViewModelProvider(this)[SharedViewModel::class.java]
        val userID = intent.getIntExtra("userID",0)
        val userFirstName = intent.getStringExtra("firstName")
        val userLastName = intent.getStringExtra("lastName")
        //Available services
        val availableServicesStr = intent.getStringExtra("availableServices")
        val availableServices = JSONArray(availableServicesStr)
        //Log.d("avlServicesSession",availableServices.toString())
        for (i in 0 until availableServices.length()){
            val serviceID = availableServices.getJSONObject(i).getInt("serviceID")
            //Log.d("serviceID",serviceID.toString())
            val serviceName = availableServices.getJSONObject(i).getString("serviceName")
           // Log.d("serviceName",serviceName.toString())
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