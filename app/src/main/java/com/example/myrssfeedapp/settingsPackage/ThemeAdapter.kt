package com.example.myrssfeedapp.settingsPackage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myrssfeedapp.R

class ThemeAdapter (private var themesList: ArrayList<Themes>): RecyclerView.Adapter<ThemeAdapter.ThemeViewHolder>(){
    private var themeList = ArrayList<Themes>()

    class ThemeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val themeName: TextView = view.findViewById(R.id.serviceName)
        val checked : CheckBox = view.findViewById(R.id.isSubscribed)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ThemeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.service_cell,parent,false)
        return ThemeViewHolder(view)
    }

    override fun onBindViewHolder(holder: ThemeViewHolder, position: Int) {
        val currentTheme = themeList[position]
        holder.themeName.text = currentTheme.themeName
        holder.checked.isChecked = currentTheme.checked
//        holder.checked.setOnCheckedChangeListener { _, _ ->
//            listener(currentTheme)
//        }
    }

    override fun getItemCount(): Int {
        return themeList.size
    }



}