package com.example.myrssfeedapp.Room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ArticleDao {
    @Insert
    fun addArticle(article: ArticleEntity)

    @Query("SELECT * FROM article_table ORDER BY articleID")
    fun getAllArticles() : LiveData<List<ArticleEntity>>

    @Query("DELETE FROM article_table")
    fun deleteAllArticles()

//    @Query("DELETE FROM article_table WHERE serviceID = :service_ID")
//    fun deleteAllServiceArticles(service_ID:Int)

}