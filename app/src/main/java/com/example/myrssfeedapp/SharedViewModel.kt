package com.example.myrssfeedapp

import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    private var userID:Int = 0
    private var firstName : String = ""
    private var lastName : String = ""
    private var availableServices = ArrayList<Service>()
    private var userSbscriptions = ArrayList<Subscription>()

    fun setUserInfos(id:Int,fName:String,lName:String){
        userID = id
        firstName = fName
        lastName = lName
    }

    fun getUserID():Int{return userID}

    fun getUserFirstName():String{return firstName}

    fun getUserLastName():String{return lastName}

    //add available service
    fun setAvailableServices(s:Service){
        availableServices.add(s)
    }
    fun getAvailableServices(): ArrayList<Service>{
        return availableServices
    }

    //add user subscription
    fun setUserSubscription(s:Subscription){
        userSbscriptions.add(s)
    }
    fun getUserSubscriptions(): ArrayList<Subscription>{
        return userSbscriptions
    }
}