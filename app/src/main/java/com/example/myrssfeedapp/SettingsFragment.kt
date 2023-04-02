package com.example.myrssfeedapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import okhttp3.FormBody
import okhttp3.OkHttpClient

class SettingsFragment : Fragment() {
    private lateinit var userFullName:TextView
    private lateinit var addService:ImageView
    private lateinit var subscriptionRV :RecyclerView
    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_settings, container, false)
        userFullName = view.findViewById(R.id.username)
        addService = view.findViewById(R.id.addService)
        subscriptionRV = view.findViewById(R.id.subscriptionRV)
        subscriptionRV.layoutManager = LinearLayoutManager(requireContext())
        val subscriptionAdapter = SubscriptionAdapter()
        val sharedVM = ViewModelProvider(requireActivity())[SharedViewModel::class.java]

        //Update the username
        userFullName.text = "${sharedVM.getUserFirstName()} ${sharedVM.getUserLastName()}"

        //update subscriptions
        subscriptionAdapter.setSubscriptions(sharedVM.getUserSubscriptions())
        subscriptionRV.adapter = subscriptionAdapter
        //add service button is clicked
        addService.setOnClickListener{

        }


        return view
    }
}