package com.example.myrssfeedapp.favoritesPackage

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myrssfeedapp.R
import com.example.myrssfeedapp.Room.ArticleEntity

class FavoritesAdapter(private val listener: (ArticleEntity)->Unit) : RecyclerView.Adapter<FavoritesAdapter.FavoritesViewHolder>() {
    private var articlesList = emptyList<ArticleEntity>()

    class FavoritesViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title : TextView = view.findViewById(R.id.articleTitle)
        val author : TextView = view.findViewById(R.id.author)
        val dateTime : TextView = view.findViewById(R.id.dateTime)
        val source : TextView = view.findViewById(R.id.source)
        val image : ImageView = view.findViewById(R.id.articleImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.favorite_cell,parent,false)
        return FavoritesViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoritesViewHolder, position: Int) {
        val currentArticle = articlesList[position]

        holder.author.text = currentArticle.articleAuthor
        holder.title.text = currentArticle.articleTitle
        holder.dateTime.text = currentArticle.publishedDate
        holder.source.text = currentArticle.publisher

        Log.d("image link",currentArticle.articleImage)
        Glide.with(holder.itemView.context)
            .load(currentArticle.articleImage)
            .centerCrop()
            .placeholder(R.drawable.loading_image)
            .into(holder.image)

        holder.itemView.setOnClickListener{
            listener(currentArticle)
        }
    }

    override fun getItemCount(): Int {
        return articlesList.size
    }

    fun setData(l:List<ArticleEntity>){
        articlesList=l
        notifyDataSetChanged()
    }
}