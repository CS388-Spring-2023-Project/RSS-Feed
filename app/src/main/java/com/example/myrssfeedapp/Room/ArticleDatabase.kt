package com.example.myrssfeedapp.Room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ArticleEntity::class], version = 1, exportSchema = false)
abstract class ArticleDatabase :RoomDatabase(){
    abstract fun articleDao() : ArticleDao

    companion object{
        @Volatile
        private var INSTANCE : ArticleDatabase? = null
        fun getDataBase(context: Context) : ArticleDatabase {
            var tempInstance = INSTANCE
            if(tempInstance != null) {
                return tempInstance
            }
            synchronized(this){
                val temp = Room.databaseBuilder(
                    context.applicationContext,
                    ArticleDatabase::class.java,
                    "article_database"
                ).build()
                INSTANCE = temp
                return temp
            }
        }
    }
}