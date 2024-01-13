package com.redsea.redsea.service.ui.adapters.adapters

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.redsea.redsea.R
import com.redsea.redsea.network.Response.OpenRequest.OpenRequestItem
import com.redsea.redsea.service.ui.fragments.AddWellFragment


class OPenRequestAdapter(private val fragmentTransaction: FragmentTransaction?, var acceptedrequest:ArrayList<com.redsea.redsea.network.Response.OpenRequest.OpenRequestItem>):RecyclerView.Adapter<OPenRequestAdapter.OpenRequestViewHolder>() {
    class OpenRequestViewHolder(itemview: View):RecyclerView.ViewHolder(itemview){
        val viewWellName = itemview.findViewById<TextView>(R.id.wellNameViewWellTV)
        val dateIn = itemview.findViewById<TextView>(R.id.dateInTV)
        val dateOut = itemview.findViewById<TextView>(R.id.dateOutTV)
        val requestWellItem = itemview.findViewById<ConstraintLayout>(R.id.requestWellItem)


    }
    fun setfilterrequest(openrequest:ArrayList<OpenRequestItem>){
        this.acceptedrequest=openrequest
        notifyDataSetChanged()

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OpenRequestViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.item_open_request,parent,false)
        return OpenRequestViewHolder(view)
    }

    override fun getItemCount(): Int {
        return acceptedrequest.size
    }

    override fun onBindViewHolder(holder: OpenRequestViewHolder, position: Int) {
        val currentrequest: OpenRequestItem?= acceptedrequest[position]
        holder.viewWellName.text=currentrequest?.well?.name
        holder.dateIn.text=currentrequest?.well?.from
        holder.dateOut.text=currentrequest?.well?.to

        holder.requestWellItem.setOnClickListener {
            navigateToAddWellFragment(currentrequest!!)
        }
    }
    private fun navigateToAddWellFragment(requestItem: OpenRequestItem) {
        val viewAddwellFragment = AddWellFragment()
        val bundle = Bundle()
        bundle.putBoolean("iseditmode",true)
        bundle.putString("edittype","editrequest")
        bundle.putSerializable("openrequest",requestItem)
        Log.d("welldata","$requestItem")
        viewAddwellFragment.arguments = bundle
        fragmentTransaction?.replace(R.id.fragmentContainer, viewAddwellFragment)
        fragmentTransaction?.addToBackStack(null)
        fragmentTransaction?.commit()

    }
}