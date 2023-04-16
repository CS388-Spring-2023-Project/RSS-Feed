package com.example.myrssfeedapp

import androidx.lifecycle.ViewModel
import com.example.myrssfeedapp.Room.ArticleEntity
import com.example.myrssfeedapp.settingsPackage.Service
import com.example.myrssfeedapp.settingsPackage.Subscription

class SharedViewModel : ViewModel() {
    private var userID:Int=0
    private var firstName : String = ""
    private var lastName : String = ""
    private var availableServices = ArrayList<Service>()
    private var userSubscriptions = ArrayList<Subscription>()

    var chosenArticle : ArticleEntity? = null
    var from : String? = null

    fun setUserInfos(id:Int,fName:String,lName:String){
        userID = id
        firstName = fName
        lastName = lName
    }

    fun getUserID():Int{return userID}

    fun getUserFirstName():String{return firstName}

    fun getUserLastName():String{return lastName}

    //add available service
    fun setAvailableServices(s: Service){
        availableServices.add(s)
    }
    fun getAvailableServices(): ArrayList<Service>{
        return availableServices
    }

    //add user subscription
    fun setUserSubscription(s: Subscription){
        userSubscriptions.add(s)
    }
    fun getUserSubscriptions(): ArrayList<Subscription>{
        return userSubscriptions
    }
    fun updateService(s:String){
        for(i in availableServices){
            if(i.serviceName == s) {
                i.isSubscribed = !i.isSubscribed
                break
            }
        }
    }
    fun updateSubscription(){
        for(i in userSubscriptions){
            for(j in availableServices){
                if(i.serviceName == j.serviceName && !j.isSubscribed){
                    userSubscriptions.remove(i)
                }
            }
        }
    }

    fun unsubscribe(subscription_id:Int,service_name:String){
        //update subscriptions
        for(i in userSubscriptions){
            if(i.subscriptionID == subscription_id) {
                userSubscriptions.remove(i)
                break
            }
        }
        var service_id = 0
        for(i in availableServices){
            if(i.serviceName == service_name) {
                service_id = i.serviceID
                break
            }
        }
        //update available services
        for(i in availableServices){
            if(i.serviceID == service_id) {
                i.isSubscribed = false
                break
            }
        }
    }

    fun getServiceID(serviceName:String) : Int{
        for(i in availableServices){
            if(i.serviceName == serviceName)
                return i.serviceID
        }
        return -1
    }
}