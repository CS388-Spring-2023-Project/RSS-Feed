package com.example.myrssfeedapp.settingsPackage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myrssfeedapp.R

class SubscriptionAdapter(private val listener: (Subscription) -> Unit): RecyclerView.Adapter<SubscriptionAdapter.SubscriptionViewHolder>() {
    private var subscriptionsList = ArrayList<Subscription>()

    class SubscriptionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val serviceName : TextView = view.findViewById(R.id.serviceName)
        val unsubscribe : Button = view.findViewById(R.id.unsubscribe)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubscriptionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.subscription_cell,parent,false)
        return SubscriptionViewHolder(view)
    }

    override fun onBindViewHolder(holder: SubscriptionViewHolder, position: Int) {
        val currentSubscription = subscriptionsList[position]
        holder.serviceName.text = currentSubscription.serviceName
        holder.unsubscribe.setOnClickListener {
            subscriptionsList.remove(subscriptionsList[position])
            notifyDataSetChanged()
            listener(currentSubscription)
        }
    }

    override fun getItemCount(): Int {
        return subscriptionsList.size
    }
    fun setSubscriptions(l:ArrayList<Subscription>){
        subscriptionsList = l
        notifyDataSetChanged()
    }

//    fun addElement()
}