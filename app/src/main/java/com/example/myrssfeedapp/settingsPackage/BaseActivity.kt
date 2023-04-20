package com.example.myrssfeedapp.settingsPackage

import android.app.ActivityManager
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.appcompat.app.AppCompatActivity
import com.example.myrssfeedapp.R

abstract class BaseActivity : AppCompatActivity() {

    private var currentTheme = YELLOW

    override fun onCreate(savedInstanceState: Bundle?) {
        currentTheme = PreferenceManager.getDefaultSharedPreferences(this).getInt(KEY_THEME, YELLOW)
        super.onCreate(savedInstanceState)
    }

    protected fun setTheme() { setTheme(currentTheme) }

    protected fun switchTheme() {
        currentTheme = when(currentTheme) {
            YELLOW-> BLUE
            BLUE -> YELLOW
            else -> -1
        }

        PreferenceManager.getDefaultSharedPreferences(this).edit().putInt(KEY_THEME, currentTheme).apply()
    }

    companion object {
        private const val KEY_THEME = "Theme"
        private const val YELLOW = R.style.Theme_MyRssFeedApp
        private const val BLUE = R.style.Theme_MyRssFeedApp_BlueTheme
    }
}