package com.example.myrssfeedapp.Room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "article_table")
data class ArticleEntity (
    @PrimaryKey(autoGenerate = true)
    val articleID : Int,
    val articleTitle:String,
    val articleContent:String,
    val articleImage:String,
    val articleAuthor:String,
    val publishedDate:String,
    val publisher:String,
    val articleURL:String
    )