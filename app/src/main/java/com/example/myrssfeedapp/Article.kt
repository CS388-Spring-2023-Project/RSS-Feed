package com.example.myrssfeedapp

data class Article(val articleID: Int,
                   val author:String,
                   val title:String,
                   val content:String,
                   val image:String,
                   val dateTime:String)