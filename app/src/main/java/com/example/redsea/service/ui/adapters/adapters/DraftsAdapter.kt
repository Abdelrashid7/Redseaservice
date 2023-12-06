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
import com.example.redsea.network.Response.UserWells.UserWells
import com.example.redsea.network.Response.UserWells.UserWellsItem
import com.example.redsea.service.ui.fragments.AddWellFragment
import com.example.redsea.ui.fragments.OperationsFragment

class DraftsAdapter(val userWells: MutableList<UserWellsItem>) :
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
        return minOf(2,userWells.size)
    }

    override fun onBindViewHolder(holder: DraftsAdapterViewHolder, position: Int) {
        Log.d("DRAFTSTEST", userWells[position].name)
        holder.setIsRecyclable(false)
        if (userWells[position].published != "published") {
            holder.name.text = userWells[position].name
            holder.from.text = userWells[position].from
            holder.to.text = userWells[position].to
            holder.edit_icon.setOnClickListener {
                val addWellFragment = AddWellFragment()
                val bundle = Bundle()
                bundle.putString("Wellname", userWells[position].name)
                bundle.putString("wellFrom", userWells[position].from)
                bundle.putString("wellTo", userWells[position].to)
                addWellFragment.arguments = bundle
                val fragmentManager: FragmentManager = (holder.itemView.context as AppCompatActivity).supportFragmentManager
                val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.fragmentContainer, addWellFragment)
//                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.commit()

            }

            }

        }

    }


