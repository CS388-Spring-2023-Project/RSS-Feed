package com.example.myrssfeedapp.favoritesPackage

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myrssfeedapp.HelperClass
import com.example.myrssfeedapp.R
import com.example.myrssfeedapp.Room.ArticleEntity
import com.example.myrssfeedapp.SharedViewModel
import okhttp3.*
import org.json.JSONObject
import java.io.IOException


class FavoriteFragment : Fragment(){
    private lateinit var favoritesRV :RecyclerView
    private lateinit var searchButton : Button
    private lateinit var searchedWord : EditText
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_favorite, container, false)
        val helperClass = HelperClass()
        val sharedVM = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        favoritesRV = view.findViewById(R.id.favoritesRV)
        searchButton = view.findViewById(R.id.searchButton)
        searchedWord = view.findViewById(R.id.searchedWord)
        favoritesRV.layoutManager = LinearLayoutManager(this.requireContext())
        val favoritesAdapter = FavoritesAdapter{
            sharedVM.chosenArticle = it
            Navigation.findNavController(view).navigate(R.id.to_favoriteArticleFragment)
        }
        var favoritesList = ArrayList<ArticleEntity>()


        //val articleVM = ViewModelProvider(requireActivity())[ArticleViewModel::class.java]

        val userID = sharedVM.getUserID()

        val client = OkHttpClient()
        val requestBody = FormBody.Builder()
            .add("getFavorite","")
            .add("userID",userID.toString())
            .build()
        val request = Request.Builder().url(helperClass.backendURL)
            .method("POST",requestBody)
            .build()
        client.newCall(request).enqueue(object: Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("result", "Failure")
                Log.d("exception", e.toString())
            }

            override fun onResponse(call: Call, response: Response) {
                Log.d("result", "Success")
                val jsonResult = response.body?.string()?.let { it1 -> JSONObject(it1) }
                //Log.d("favorites", jsonResult.toString())
                if(jsonResult != null){
                    val error = jsonResult.getInt("error")
                    if(error == 0) Log.d("Error", "Error getting user favorites articles")
                    else if(error == 1){
                        val favorites = jsonResult.getJSONArray("favorites")
                        Log.d("favorites",favorites.toString())
                        for(i in 0 until  favorites.length()){
                            val favoriteID = favorites.getJSONObject(i).getInt("favoriteID")
                            val articleTitle = favorites.getJSONObject(i).getString("articleTitle")
                            val articleAuthor = favorites.getJSONObject(i).getString("articleAuthor")
                            val articleSource = favorites.getJSONObject(i).getString("articleSource")
                            val articleContent = favorites.getJSONObject(i).getString("articleContent")
                            val articleURL = favorites.getJSONObject(i).getString("articleURL")
                            val articleDateTime = favorites.getJSONObject(i).getString("articleDateTime")
                            val articleImage = favorites.getJSONObject(i).getString("articleImage")

                            val favoriteEntity = ArticleEntity(favoriteID,articleTitle,articleContent,articleImage,articleAuthor,articleDateTime,articleSource,articleURL)
                            favoritesList.add(favoriteEntity)
                        }
                        requireActivity().runOnUiThread {
                            favoritesAdapter.setData(favoritesList.toList())
                        }
                    }
                }
            }
        })
        favoritesRV.adapter = favoritesAdapter
        searchButton.setOnClickListener {
            var foundList = ArrayList<ArticleEntity>()
            val word = searchedWord.text.toString()
            if(word.isEmpty()) {
                Toast.makeText(requireContext(), "Nothing to be searched", Toast.LENGTH_LONG).show()
                favoritesAdapter.setData(favoritesList.toList())
            }
            else if(favoritesList.isNotEmpty()){
                for(article in favoritesList){
                    if(word.lowercase() in article.articleTitle.lowercase() ||
                        word.lowercase() in article.articleContent.lowercase()||
                            word.lowercase() in article.articleURL.lowercase())
                        foundList.add(article)
                }
                if(foundList.isNotEmpty())
                    favoritesAdapter.setData(foundList.toList())
            }
        }

        return view
    }
}