package com.example.myrssfeedapp.SettingsPackage

data class Service ( val serviceID:Int,
                     val serviceName:String,
                     val serviceURL:String,
                     val serviceKEY:String,
                     var isSubscribed:Boolean)