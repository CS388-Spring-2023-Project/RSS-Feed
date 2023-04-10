package com.example.myrssfeedapp.settingsPackage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myrssfeedapp.R

class ServiceAdapter(private var listener : (Service) -> Unit):RecyclerView.Adapter<ServiceAdapter.ServiceViewHolder>() {
    private var serviceList = ArrayList<Service>()

    class ServiceViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val serviceName: TextView = view.findViewById(R.id.serviceName)
        val isSubscribed :CheckBox = view.findViewById(R.id.isSubscribed)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.service_cell,parent,false)
        return ServiceViewHolder(view)
    }

    override fun onBindViewHolder(holder: ServiceViewHolder, position: Int) {
        val currentService = serviceList[position]
        holder.serviceName.text = currentService.serviceName
        holder.isSubscribed.isChecked = currentService.isSubscribed
        holder.isSubscribed.setOnCheckedChangeListener { _, _ ->
            listener(currentService)
        }
    }

    override fun getItemCount(): Int {
        return serviceList.size
    }

    fun setData(l:ArrayList<Service>){
        serviceList = l
        notifyDataSetChanged()
    }
}