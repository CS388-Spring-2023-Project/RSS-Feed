package com.example.myrssfeedapp.homePackage

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.myrssfeedapp.HelperClass
import com.example.myrssfeedapp.R
import com.example.myrssfeedapp.SharedViewModel
import com.example.myrssfeedapp.favoritesPackage.FavoriteFragment
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class ArticleFragment :Fragment(){
    private lateinit var articleImage : ImageView
    private lateinit var articleContent : TextView
    private lateinit var articleAuthor : TextView
    private lateinit var articlePub_date : TextView
    private lateinit var articleTitle : TextView
    private lateinit var articleURL : TextView
    private lateinit var articleSource : TextView
    private lateinit var backButton : ImageView
    private lateinit var favorite : ImageView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_article, container, false)
        articleImage = view.findViewById(R.id.articleImage)
        articleTitle = view.findViewById(R.id.articleTitle)
        articleContent = view.findViewById(R.id.articleContent)
        articlePub_date = view.findViewById(R.id.articlePub_date)
        articleURL = view.findViewById(R.id.articleURL)
        articleSource = view.findViewById(R.id.articleSource)
        articleAuthor = view.findViewById(R.id.articleAuthor)
        favorite = view.findViewById(R.id.favorite)
        backButton = view.findViewById(R.id.backButton)
        val sharedVM = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        val helperClass = HelperClass()
        val articleEntity = sharedVM.chosenArticle

        if(articleEntity != null){
            Glide.with(this)
                .load(articleEntity.articleImage)
                .centerCrop()
                .placeholder(R.drawable.loading_image)
                .into(articleImage)
            articleTitle.text = articleEntity.articleTitle
            articleContent.text = articleEntity.articleContent
            articlePub_date.text = articleEntity.publishedDate.subSequence(0,10)
            articleSource.text = articleEntity.publisher
            articleURL.text = articleEntity.articleURL
            articleAuthor.text = articleEntity.articleAuthor
        }

        //Back Button pressed
        backButton.setOnClickListener {

            val fragmentTransaction = fragmentManager?.beginTransaction()
            val homeFragment = HomeFragment()
            fragmentTransaction?.replace(R.id.fragment, homeFragment)
            if(sharedVM.from == "favoritesFragment") {
                val favoritesFragment = FavoriteFragment()
                fragmentTransaction?.replace(R.id.fragment, favoritesFragment)
            }
            fragmentTransaction?.commit()
        }

        favorite.setOnClickListener {
            val client = OkHttpClient()
            val bodyRequest = FormBody.Builder()
                .add("addToFavorite","")
                .add("userID",sharedVM.getUserID().toString())
                .add("articleTitle",articleTitle.text.toString())
                .add("articleContent",articleContent.text.toString())
                .add("articleImage",articleEntity?.articleImage.toString())
                .add("articleAuthor",articleAuthor.text.toString())
                .add("articleURL",articleURL.text.toString())
                .add("articlePub_date",articlePub_date.text.toString())
                .add("articleSource",articleSource.text.toString())
                .build()
            val request = Request.Builder().url(helperClass.backendURL)
                .method("POST",bodyRequest)
                .build()
            client.newCall(request).enqueue(object: Callback{
                override fun onFailure(call: Call, e: IOException) {
                    Log.d("result","Failure")
                    Log.d("exception",e.toString())
                }

                override fun onResponse(call: Call, response: Response) {
                    Log.d("result", "Success")
                    val jsonResult = response.body?.string()?.let { it1 -> JSONObject(it1) }
                    if (jsonResult != null) {
                        if (jsonResult.getInt("error") == 0) {
                            Log.d("error","Error adding this article to favorites")
                        } else if (jsonResult.getInt("error") == 1) {
                            Log.d("error", "Article is added to favorites successfully")
                        }
                    }
                }
            })
        }

        articleURL.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(articleEntity?.articleURL))
            startActivity(intent,null)
        }
        return view
    }
}