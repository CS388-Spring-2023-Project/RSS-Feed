package com.example.myrssfeedapp.homePackage


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.replace
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myrssfeedapp.R
import com.example.myrssfeedapp.Room.ArticleEntity
import com.example.myrssfeedapp.Room.ArticleViewModel
import com.example.myrssfeedapp.SharedViewModel
import okhttp3.*
import org.json.JSONObject
import java.io.IOException


class HomeFragment : Fragment() {
    private lateinit var homeRV:RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        homeRV = view.findViewById(R.id.homeRV)
        homeRV.layoutManager = LinearLayoutManager(requireContext())
        val articleViewModel = ViewModelProvider(requireActivity())[ArticleViewModel::class.java]
        //articleViewModel.deleteAll()
        val sharedVM = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        val homeAdapter = HomeAdapter{
            sharedVM.chosenArticle = it
            sharedVM.from = "homeFragment"
            val articleFragment = ArticleFragment()
            val fragmentTransaction = fragmentManager?.beginTransaction()
            fragmentTransaction?.replace(R.id.fragment,articleFragment)
            fragmentTransaction?.isAddToBackStackAllowed
            fragmentTransaction?.commit()

        }


        //get user subscriptions
        val services = sharedVM.getAvailableServices()
        //Log.d("services home ", sharedVM.getAvailableServices().toString())
        // keep in mind that we have isSubscribed parameter
        // that we will use to pick user subscriptions
        var flag = false
        for(service in services){
            if(service.isSubscribed) {
                val url = "${service.serviceURL}${service.serviceKEY}"
                val client = OkHttpClient()
                val request = Request.Builder().url(url)
                    .build()
                client.newCall(request).enqueue(object:Callback{
                    override fun onFailure(call: Call, e: IOException) {
                        Log.d("result","Failure")
                        Log.d("exception",e.toString())
                    }

                    override fun onResponse(call: Call, response: Response) {
                        Log.d("result", "Success")
                        val jsonResult = response.body?.string()?.let { it1 -> JSONObject(it1) }
                        //Log.d("jsonResult", jsonResult.toString())
                        //NY Times API
                        if(jsonResult != null && service.serviceID == 1){
                            val responseResult = jsonResult.getJSONObject("response")
                            val docs = responseResult.getJSONArray("docs")
                            for(i in 0 until docs.length()){
                                if(!flag){
                                    articleViewModel.deleteAll()
                                    flag = !flag
                                }
                                val title = docs.getJSONObject(i)
                                    .getJSONObject("headline")
                                    .getString("main")
                                val content = docs.getJSONObject(i)
                                    .getString("lead_paragraph")
                                val imageArr = docs.getJSONObject(i)
                                    .getJSONArray("multimedia")
                                var image = ""
                                if(imageArr.length() > 0){
                                    image = imageArr
                                        .getJSONObject(0)
                                        .getString("url")
                                }

                                val source: String = docs.getJSONObject(i)
                                    .getString("source")

                                val temp = docs.getJSONObject(i)
                                    .getJSONObject("byline")
                                    .getJSONArray("person")
                                var author = "unknown"
                                if(temp.length() >0){
                                    author = "${temp.getJSONObject(0).getString("firstname")} ${temp.getJSONObject(0).getString("lastname")}"
                                }
                                val publishedDate = docs.getJSONObject(i)
                                    .getString("pub_date")
                                val articleURL = docs.getJSONObject(i)
                                    .getString("web_url")
                                if(image.isNotEmpty()){
                                    image = "https://www.nytimes.com/$image"
                                }
                                //Log.d("image",image)
                                val articleEntity = ArticleEntity(0,title,content,image,author,publishedDate,source,articleURL)

                                articleViewModel.addArticle(articleEntity)
                            }
                        }
                        //CurrentsAPI
                        else if(jsonResult != null && service.serviceID == 3){
                            val docs = jsonResult.getJSONArray("news")
                            for(i in 0 until docs.length()){
                                if(!flag){
                                    articleViewModel.deleteAll()
                                    flag = !flag
                                }
                                val title = docs.getJSONObject(i)
                                    .getString("title")
                                val content = docs.getJSONObject(i)
                                    .getString("description")
                                var image = docs.getJSONObject(i).getString("image")
                                if(image == "None"){
                                    image = ""
                                }

                                val source = "CurrentsAPI"

                                val author = docs.getJSONObject(i)
                                    .getString("author")
                                val publishedDate = docs.getJSONObject(i)
                                    .getString("published")
                                val articleURL = docs.getJSONObject(i)
                                    .getString("url")

                                //Log.d("image",image)
                                val articleEntity = ArticleEntity(0,title,content,image,author,publishedDate,source,articleURL)

                                articleViewModel.addArticle(articleEntity)
                            }
                        }
                        //News API
                        else if(jsonResult != null && service.serviceID == 4){
                            val docs = jsonResult.getJSONArray("articles")
                            for(i in 0 until docs.length()){
                                if(!flag){
                                    articleViewModel.deleteAll()
                                    flag = !flag
                                }
                                val title = docs.getJSONObject(i)
                                    .getString("title")
                                val content = docs.getJSONObject(i)
                                    .getString("content")
                                val image = docs.getJSONObject(i).getString("urlToImage")

                                val source = docs.getJSONObject(i)
                                    .getJSONObject("source")
                                    .getString("name")

                                val author = docs.getJSONObject(i)
                                    .getString("author")
                                val publishedDate = docs.getJSONObject(i)
                                    .getString("unknown")
                                val articleURL = docs.getJSONObject(i)
                                    .getString("url")

                                //Log.d("image",image)
                                val articleEntity = ArticleEntity(0,title,content,image,author,publishedDate,source,articleURL)

                                articleViewModel.addArticle(articleEntity)
                            }
                        }
                    }
                })

            }
        }


        articleViewModel.allArticles.observe(viewLifecycleOwner){article ->
            homeAdapter.setData(article)
        }
        homeRV.adapter = homeAdapter

        return view
    }
}