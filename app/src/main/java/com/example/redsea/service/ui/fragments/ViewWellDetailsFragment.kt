package com.example.redsea.service.ui.fragments

import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.redsea.R
import com.example.redsea.databinding.FragmentViewWellDetailsBinding
import com.example.redsea.network.ViewWellsResponse.ViewWellsItem
import com.example.redsea.service.ui.TitleInterface
import com.example.redsea.service.ui.UserID

class ViewWellDetailsFragment : Fragment() {
    lateinit var binding: FragmentViewWellDetailsBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentViewWellDetailsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val wellItem = arguments?.getSerializable("wellItem") as? ViewWellsItem
        (activity as? TitleInterface)?.onTextChange("View", getString(R.string.view_toolbar))
        Log.d("HELLODATA", wellItem?.name.toString())
        Log.d("USERCHECK", UserID.userId.toString())
        Log.d("USERCHECK", UserID.userAccessToken.toString())
        if(wellItem?.user?.id != UserID.userId)
        {
            binding.editRequestBtn.visibility = View.GONE
        }
        else {
            binding.editRequestBtn.setOnClickListener {

                val editRequestFragment = EditRequestFragment()
                val bundle = Bundle()
                bundle.putSerializable("wellItemRequest", wellItem)
                editRequestFragment.arguments = bundle

                val transaction: FragmentTransaction? = fragmentManager?.beginTransaction()
                transaction?.replace(R.id.fragmentContainer, editRequestFragment)
                transaction?.addToBackStack(null)
                transaction?.commit()
            }
        }

    }
}