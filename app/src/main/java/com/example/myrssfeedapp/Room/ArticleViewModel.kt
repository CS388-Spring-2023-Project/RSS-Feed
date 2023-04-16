package com.example.myrssfeedapp.Room

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ArticleViewModel(application: Application) : AndroidViewModel(application){
    val allArticles : LiveData<List<ArticleEntity>>
    //val allFavorites : List<FavoriteEntity>
    private var articleRepository : ArticleRepository

    init {
        val articleDao = ArticleDatabase.getDataBase(application).articleDao()
        articleRepository = ArticleRepository(articleDao)
        allArticles = articleRepository.allArticles
    }

    fun addArticle(article: ArticleEntity){
        viewModelScope.launch(Dispatchers.IO){
            articleRepository.addArticle(article)
        }
    }
    fun deleteAll(){
        viewModelScope.launch (Dispatchers.IO){
            articleRepository.deleteAll()
        }
    }

//    fun deleteAllServiceArticles(serviceID:Int){
//        viewModelScope.launch (Dispatchers.IO){
//            articleRepository.deleteAllServiceArticles(serviceID)
//        }
//    }
}