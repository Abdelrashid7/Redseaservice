package com.example.redsea.service.ui.adapters.adapters

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainer
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.example.redsea.R
import com.example.redsea.network.Response.OpenRequest.OpenRequestItem
import com.example.redsea.network.Response.UserWells.UserWells
import com.example.redsea.network.Response.UserWells.UserWellsItem
import com.example.redsea.service.ui.fragments.AddWellFragment
import com.example.redsea.ui.fragments.OperationsFragment

class DraftsAdapter(private val fragmentTransaction: FragmentTransaction?,val userWells: MutableList<UserWellsItem>) :
    RecyclerView.Adapter<DraftsAdapter.DraftsAdapterViewHolder>() {

    class DraftsAdapterViewHolder(viewItem: View) : RecyclerView.ViewHolder(viewItem) {
        val name = viewItem.findViewById<TextView>(R.id.draftNameTV)
        val from = viewItem.findViewById<TextView>(R.id.fromDraftTV)
        val to = viewItem.findViewById<TextView>(R.id.toDraftTV)
        val edit_icon=viewItem.findViewById<ImageView>(R.id.draftIcon)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DraftsAdapterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_drafts, parent, false)
        return DraftsAdapterViewHolder(view)
    }

    override fun getItemCount(): Int {
        Log.d("DRAFTSTEST", userWells.size.toString())
        return userWells.size
    }

    override fun onBindViewHolder(holder: DraftsAdapterViewHolder, position: Int) {
        Log.d("DRAFTSTEST", userWells[position].name)
        holder.setIsRecyclable(false)
        if (userWells[position].published != "published") {
            val currentdraft:UserWellsItem?=userWells[position]
            holder.name.text = currentdraft?.name
            holder.from.text = currentdraft?.from
            holder.to.text = currentdraft?.to
            holder.edit_icon.setOnClickListener {
                navigateToAddWellFragment(currentdraft!!)

            }

            }

        }
    private fun navigateToAddWellFragment(editdrafttItem: UserWellsItem) {
        val viewAddwellFragment = AddWellFragment()
        val bundle = Bundle()
        bundle.putBoolean("iseditmode",true)
        bundle.putString("edittype","editdraft")
        bundle.putSerializable("draftitem",editdrafttItem)
        viewAddwellFragment.arguments = bundle
        fragmentTransaction?.replace(R.id.fragmentContainer, viewAddwellFragment)
//        fragmentTransaction?.addToBackStack(null)
        fragmentTransaction?.commit()


    }

    }


