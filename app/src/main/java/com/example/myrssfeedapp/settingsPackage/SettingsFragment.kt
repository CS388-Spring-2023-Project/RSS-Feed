package com.example.myrssfeedapp.settingsPackage


import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myrssfeedapp.HelperClass
import com.example.myrssfeedapp.MainActivity
import com.example.myrssfeedapp.R
import com.example.myrssfeedapp.Room.ArticleViewModel
import com.example.myrssfeedapp.SharedViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class SettingsFragment : Fragment() {
    private lateinit var userFullName:TextView
    private lateinit var addService:ImageView
    private lateinit var updateUserName:TextView
    private lateinit var subscriptionRV :RecyclerView
    private lateinit var deleteAccount:Button
    private lateinit var signOut : Button


    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_settings, container, false)
        val helperClass = HelperClass()
        userFullName = view.findViewById(R.id.username)
        addService = view.findViewById(R.id.addService)
        updateUserName = view.findViewById(R.id.edit)
        subscriptionRV = view.findViewById(R.id.subscriptionRV)
        deleteAccount = view.findViewById(R.id.deleteButton)
        signOut = view.findViewById(R.id.signOutButton)
        subscriptionRV.layoutManager = LinearLayoutManager(requireContext())
        val sharedVM = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        //Log.d("services settings ", sharedVM.getAvailableServices().toString())

        //create a subscription Adapter instance + unsubscribe(Listener)
        val subscriptionAdapter = SubscriptionAdapter{
            sharedVM.unsubscribe(it.subscriptionID,it.serviceName)

            //update database
            val client = OkHttpClient()
            val requestBody = FormBody.Builder()
                .add("unsubscribe","")
                .add("subscriptionID",it.subscriptionID.toString())
                .build()
            val request = Request.Builder()
                .url(helperClass.backendURL)
                .method("POST",requestBody)
                .build()
            client.newCall(request).enqueue(object: Callback{
                override fun onFailure(call: Call, e: IOException) {
                    Log.d("result_unsubscribe","Failure")
                    Log.d("exception_unsubscribe",e.toString())
                }

                override fun onResponse(call: Call, response: Response) {
                    //Log.d("result_unsubscribe", "Success")
                    val jsonResult = response.body?.string()?.let { it1 -> JSONObject(it1) }
                    //Log.d("jsonResult_unsubscribe", jsonResult.toString())
                    if (jsonResult != null) {
                        if (jsonResult.getInt("error") == 0) {
                            //Log.d("error", "Error unsubscribing the service")
                            requireActivity().runOnUiThread{
                                Toast.makeText(requireActivity(),
                                    "Error unsubscribing the service",Toast.LENGTH_LONG).show()
                            }
                        } else if (jsonResult.getInt("error") == 1) {
                            requireActivity().runOnUiThread{
                                Toast.makeText(requireActivity(),
                                    "service unsubscribed successfully",Toast.LENGTH_LONG).show()
                            }
                            //Log.d("error", "service unsubscribed")
                        }
                    }
                }
            })
        }

        //Update the username
        userFullName.text = "${sharedVM.getUserFirstName()} ${sharedVM.getUserLastName()}"

        //update subscriptions
        subscriptionAdapter.setSubscriptions(sharedVM.getUserSubscriptions())
        subscriptionRV.adapter = subscriptionAdapter
        //add service button is clicked
        addService.setOnClickListener{
            //displaying the bottom sheet
            val dialog = BottomSheetDialog(this.requireContext())
            val serviceView = layoutInflater.inflate(R.layout.fragment_services,null)
            val update:Button = serviceView.findViewById(R.id.update)
            val cancel:Button = serviceView.findViewById(R.id.cancel)
            val servicesRV: RecyclerView = serviceView.findViewById(R.id.servicesRV)
            servicesRV.layoutManager = LinearLayoutManager(this.requireContext())
            //val sharedVM = ViewModelProvider(this)[SharedViewModel::class.java]
            val serviceAdapter = ServiceAdapter{
                sharedVM.updateService(it.serviceName)
                sharedVM.updateSubscription()
            }
            serviceAdapter.setData(sharedVM.getAvailableServices())
            //Log.d("data",sharedVM.getAvailableServices().toString())
            servicesRV.adapter = serviceAdapter

            //cancel is pressed
            cancel.setOnClickListener {
                dialog.dismiss()
            }
            //update is pressed
            update.setOnClickListener{
                //networking and update data & ui
                for(i in sharedVM.getAvailableServices()){
                    val client = OkHttpClient()
                    val bodyRequest : FormBody = if(i.isSubscribed){
                        FormBody.Builder()
                            .add("subscribe","")
                            .add("userID",sharedVM.getUserID().toString())
                            .add("serviceID",i.serviceID.toString())
                            .build()

                    } else{
                        FormBody.Builder()
                            .add("unsubscribe2","")
                            .add("userID",sharedVM.getUserID().toString())
                            .add("serviceID",i.serviceID.toString())
                            .build()
                    }

                    val request = Request.Builder()
                        .url(helperClass.backendURL)
                        .method("POST",bodyRequest)
                        .build()
                    client.newCall(request).enqueue(object:Callback{
                        override fun onFailure(call: Call, e: IOException) {
                            Log.d("result","Failure")
                            Log.d("exception",e.toString())
                        }

                        override fun onResponse(call: Call, response: Response) {
                            Log.d("result", "Success")
                            val jsonResult = response.body?.string()?.let { it1 -> JSONObject(it1) }
                            Log.d("jsonResult", jsonResult.toString())
                            if (jsonResult != null) {
                                if (jsonResult.getInt("error") == 0) {
                                    if(i.isSubscribed) Log.d("error", "Error subscribing the service")
                                    else Log.d("error", "Error unsubscribing the service")

                                } else if (jsonResult.getInt("error") == 1) {
                                    if(i.isSubscribed){
                                        Log.d("error", "Success subscribing the service")
                                        val subscriptionID = jsonResult.getInt("subscriptionID")
                                        val serviceName = jsonResult.getString("serviceName")
                                        sharedVM.setUserSubscription(Subscription(subscriptionID,serviceName))
                                    }
                                    else Log.d("error", "success unsubscribing the service")
                                }
                            }
                        }
                    })
                }
                //dismiss the bottom-sheet and update UI
                dialog.dismiss()
                subscriptionAdapter.notifyDataSetChanged()
            }
            dialog.setCancelable(false)
            dialog.setContentView(serviceView)
            dialog.show()
        }

        //update username
        updateUserName.setOnClickListener {
            val dialog = BottomSheetDialog(this.requireContext(),R.style.AppBottomSheetDialogTheme)
            val updateView = layoutInflater.inflate(R.layout.name_update_fragment,null)

            val firstname: EditText = updateView.findViewById(R.id.firstName)
            val lastname: EditText = updateView.findViewById(R.id.lastName)
            val updateBtn :Button = updateView.findViewById(R.id.update)
            val cancel :Button = updateView.findViewById(R.id.cancel)
            //cancel clicked
            cancel.setOnClickListener {
                dialog.dismiss()
            }
            //update clicked
            updateBtn.setOnClickListener {
                val firstName = firstname.text.toString()
                val lastName = lastname.text.toString()
                if(firstName.isNotEmpty() && lastName.isNotEmpty()){
                    //update shared-ViewModel
                    val userID = sharedVM.getUserID()
                    sharedVM.setUserInfos(userID, firstName, lastName)
                    //update settings UI
                    userFullName.text = "$firstName $lastName"

                    //update DB
                    val client = OkHttpClient()
//                    Log.d("firstname : ",firstName)
//                    Log.d("lastname : ",lastName)
//                    Log.d("userID : ",userID.toString())
                    val requestBody = FormBody.Builder()
                        .add("updateUserName","")
                        .add("userID",userID.toString())
                        .add("firstName",firstName)
                        .add("lastName",lastName)
                        .build()
                    val request = Request
                                .Builder()
                                .url(helperClass.backendURL)
                                .method("POST",requestBody)
                                .build()
                    client.newCall(request).enqueue(object : Callback{
                        override fun onFailure(call: Call, e: IOException) {
                            Log.d("result","Failure")
                            Log.d("exception",e.toString())
                        }

                        override fun onResponse(call: Call, response: Response) {
                            Log.d("result", "Success")
                            val jsonResult = response.body?.string()?.let { it1 -> JSONObject(it1) }
                            Log.d("jsonResult", jsonResult.toString())
                            if (jsonResult != null) {
                                if (jsonResult.getInt("error") == 0) {
                                    //Log.d("error", "Error updating Record")
                                    requireActivity().runOnUiThread{
                                        Toast.makeText(requireActivity(),
                                            "Error updating Record",Toast.LENGTH_LONG).show()
                                    }
                                } else if (jsonResult.getInt("error") == 1) {
                                    //Log.d("error", "Record Updated")
                                    requireActivity().runOnUiThread{
                                        Toast.makeText(requireActivity(),
                                            "Record updated successfully",Toast.LENGTH_LONG).show()
                                    }
                                }
                            }
                        }
                    })
                }
                dialog.dismiss()
            }
            dialog.setCancelable(false)
            dialog.setContentView(updateView)
            dialog.show()
        }

        //delete account
        deleteAccount.setOnClickListener {
            //set alert
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Alert !!")
            builder.setMessage("Do you really want to disable this account?")
            //if user wants to delete his account
            val positiveButtonClick = { _: DialogInterface, _: Int ->
                val userID = sharedVM.getUserID()
                val client = OkHttpClient()
                val requestBody = FormBody.Builder()
                    .add("deleteAccount","")
                    .add("userID",userID.toString())
                    .build()
                val request = Request.Builder()
                    .url(helperClass.backendURL)
                    .method("POST",requestBody)
                    .build()
                client.newCall(request).enqueue(object: Callback{
                    override fun onFailure(call: Call, e: IOException) {
                        Log.d("result","Failure")
                        Log.d("exception",e.toString())
                    }

                    override fun onResponse(call: Call, response: Response) {
                        Log.d("result", "Success")
                        val jsonResult = response.body?.string()?.let { it1 -> JSONObject(it1) }
                        Log.d("jsonResult", jsonResult.toString())
                        if (jsonResult != null) {
                            if (jsonResult.getInt("error") == 0) {
                                //Log.d("error", "Error deleting the account")
                                requireActivity().runOnUiThread{
                                    Toast.makeText(requireActivity(),
                                        "Error deleting the account",Toast.LENGTH_LONG).show()
                                }
                            } else if (jsonResult.getInt("error") == 1) {
                                //Log.d("error", "Record deleted")
                                requireActivity().runOnUiThread{
                                    Toast.makeText(requireActivity(),
                                        "Record is deleted successfully",Toast.LENGTH_LONG).show()
                                }
                                //Toast.makeText(,"Account successfully deleted",Toast.LENGTH_LONG).show()
                                val intent = Intent(requireContext(),MainActivity::class.java)
                                startActivity(intent)
                                requireActivity().finish()
                            }
                        }
                    }
                })
            }
            //if user decides to cancel
            val negativeButtonClick = { _: DialogInterface, _: Int -> }

            builder.setPositiveButton("OK", DialogInterface.OnClickListener(function = positiveButtonClick))
            builder.setNegativeButton("Cancel", DialogInterface.OnClickListener(function = negativeButtonClick))
            builder.show()
        }

        //Sign out
        signOut.setOnClickListener {
            val intent = Intent(requireContext(),MainActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

        return view
    }
}