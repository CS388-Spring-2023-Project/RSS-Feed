package com.example.myrssfeedapp

import android.widget.EditText

class HelperClass {
    val backendURL = "http://10.85.136.17/RssFeed/backend.php"
    //check if a field is empty
    fun emptyField(field: EditText) : Boolean{
        if(field.text.isEmpty()){
            field.error = "${field.hint} cannot be empty"
            return true
        }
        return false
    }
    //Check if email is valid
    fun isValidEmail(emailContainer:EditText) : Boolean{
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.[a-z]+"
        return if(emailContainer.text.matches(emailPattern.toRegex())) true
        else{
            emailContainer.error = "${emailContainer.hint} is not a valid email"
            false
        }
    }
    //check if password and cPassword are matching
    fun areIdentical(passwordContainer:EditText,
                    cPasswordContainer:EditText) :Boolean{
        return if(passwordContainer.text.toString() == cPasswordContainer.text.toString()) true
        else{
            cPasswordContainer.error = "password and its confirmation are not identical"
            return false
        }
    }
}