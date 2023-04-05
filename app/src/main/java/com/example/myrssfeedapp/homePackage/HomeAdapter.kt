package com.example.myrssfeedapp.homePackage

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myrssfeedapp.R

class HomeAdapter : RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {
    private var articlesList = emptyList<Article>()

    class HomeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title : TextView = view.findViewById(R.id.title)
        val author :TextView = view.findViewById(R.id.author)
        val dateTime :TextView = view.findViewById(R.id.dateTime)
        val content:TextView = view.findViewById(R.id.content)
        val image:ImageView = view.findViewById(R.id.articleImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.home_cell,parent,false)
        return HomeViewHolder(view)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val currentArticle = articlesList[position]

        holder.author.text = currentArticle.author
        holder.title.text = currentArticle.title
        holder.dateTime.text = currentArticle.dateTime
        holder.content.text = currentArticle.content

        Glide.with(holder.itemView.context)
            .load(currentArticle.image)
            .into(holder.image)
    }

    override fun getItemCount(): Int {
        return articlesList.size
    }
    @SuppressLint("NotifyDataSetChanged")
    fun setData(l:List<Article>){
        articlesList=l
        notifyDataSetChanged()
    }
}