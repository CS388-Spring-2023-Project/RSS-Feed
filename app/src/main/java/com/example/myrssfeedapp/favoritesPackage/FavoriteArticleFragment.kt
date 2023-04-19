package com.example.myrssfeedapp.favoritesPackage

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.example.myrssfeedapp.HelperClass
import com.example.myrssfeedapp.R
import com.example.myrssfeedapp.SharedViewModel
import okhttp3.*
import org.json.JSONObject
import java.io.IOException


class FavoriteArticleFragment : Fragment() {
    private lateinit var articleImage : ImageView
    private lateinit var articleContent : TextView
    private lateinit var articleAuthor : TextView
    private lateinit var articlePub_date : TextView
    private lateinit var articleTitle : TextView
    private lateinit var articleURL : TextView
    private lateinit var articleSource : TextView
    private lateinit var backButton : ImageView
    private lateinit var delete : ImageView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_favorite_article, container, false)
        articleImage = view.findViewById(R.id.articleImage)
        articleTitle = view.findViewById(R.id.articleTitle)
        articleContent = view.findViewById(R.id.articleContent)
        articlePub_date = view.findViewById(R.id.articlePub_date)
        articleURL = view.findViewById(R.id.articleURL)
        articleSource = view.findViewById(R.id.articleSource)
        articleAuthor = view.findViewById(R.id.articleAuthor)
        delete = view.findViewById(R.id.delete)
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
            Navigation.findNavController(view).navigate(R.id.to_favoriteFragment)
        }

        delete.setOnClickListener {
            val client = OkHttpClient()
            val bodyRequest = FormBody.Builder()
                .add("deleteFavoriteArticle","")
                .add("favoriteID",articleEntity?.articleID.toString())
                .build()
            val request = Request.Builder().url(helperClass.backendURL)
                .method("POST",bodyRequest)
                .build()
            client.newCall(request).enqueue(object: Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.d("result","Failure")
                    Log.d("exception",e.toString())
                }

                override fun onResponse(call: Call, response: Response) {
                    Log.d("result", "Success")
                    val jsonResult = response.body?.string()?.let { it1 -> JSONObject(it1) }
                    if (jsonResult != null) {
                        if (jsonResult.getInt("error") == 0) {
                            requireActivity().runOnUiThread {
                                Toast.makeText(requireContext(),"Error deleting this article from favorites",
                                    Toast.LENGTH_LONG).show()
                            }
                            //Log.d("error","Error deleting this article from favorites")
                        } else if (jsonResult.getInt("error") == 1) {
                            requireActivity().runOnUiThread {
                                Toast.makeText(requireContext(),"Article is deleted successfully",
                                    Toast.LENGTH_LONG).show()
                            }
                            //Log.d("error", "Article is deleted successfully")
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