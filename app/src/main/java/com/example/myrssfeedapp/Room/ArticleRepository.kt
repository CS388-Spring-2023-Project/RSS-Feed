package com.example.myrssfeedapp.Room

import androidx.lifecycle.LiveData
import com.example.myrssfeedapp.Room.ArticleDao
import com.example.myrssfeedapp.Room.ArticleEntity

class ArticleRepository(private val articleDao: ArticleDao) {
    val allArticles : LiveData<List<ArticleEntity>> = articleDao.getAllArticles()

    fun addArticle(article: ArticleEntity){
        articleDao.addArticle(article)
    }

    fun deleteAll(){
        articleDao.deleteAllArticles()
    }

//    fun deleteAllServiceArticles(serviceID:Int){
//        articleDao.deleteAllServiceArticles(serviceID)
//    }

}