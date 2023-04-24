package com.example.myrssfeedapp.settingsPackage

import android.app.ActivityManager
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.myrssfeedapp.R
import java.io.CharArrayWriter

abstract class BaseActivity : AppCompatActivity() {

    var currentTheme = YELLOW

    override fun onCreate(savedInstanceState: Bundle?) {
        currentTheme = PreferenceManager.getDefaultSharedPreferences(this).getInt(KEY_THEME, YELLOW)
        super.onCreate(savedInstanceState)
    }

    fun setTheme() {
        setTheme(currentTheme)
    }

    fun switchTheme(themeID : Int) {
        Log.d("Theme:",currentTheme.toString())
        if(themeID == BLUE){
            currentTheme = BLUE
        }
        else if(themeID == YELLOW){
            currentTheme = YELLOW
        }
        else if(themeID == PINK){
            currentTheme = PINK
        }
        else if(themeID == GREEN){
            currentTheme = GREEN
        }
        else{
            -1
        }

        PreferenceManager.getDefaultSharedPreferences(this).edit().putInt(KEY_THEME, currentTheme).apply()
    }

    companion object {
        private const val KEY_THEME = "theme"
        private const val YELLOW = R.style.Theme_MyRssFeedApp
        private const val BLUE = R.style.Theme_MyRssFeedAppBlue
        private const val PINK = R.style.Theme_MyRssFeedApp2
        private const val GREEN = R.style.Theme_MyRssFeedAppGreen
    }
}