package com.example.redsea.service.ui.adapters.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.redsea.R
import com.example.redsea.network.PostData.Publish
import com.example.redsea.network.Response.WellOptions.WellOptionsResponse
import com.example.redsea.service.shared.NextBackInteraction


class AddWellAdapter(val addWellResponse: WellOptionsResponse,private val interaction: NextBackInteraction) :
    RecyclerView.Adapter<AddWellAdapter.AddWellViewHolder>() {
    lateinit var adapter : ChildAddWellAdapter
    private var data:Publish? = null

    class AddWellViewHolder(viewItem: View) : RecyclerView.ViewHolder(viewItem) {
        val wellNumberTV = viewItem.findViewById<TextView>(R.id.addWellNumTV)
        val childRecyclerView = viewItem.findViewById<RecyclerView>(R.id.childRecyclerView)
        val constraintLayout = viewItem.findViewById<ConstraintLayout>(R.id.constraintLayoutAddWellBtn)
        val childStructureNameTV = viewItem.findViewById<TextView>(R.id.structureNameTV)
        val nextStructureBtn = viewItem.findViewById<AppCompatButton>(R.id.nextStructureBtn)
        val backStructureBtn = viewItem.findViewById<AppCompatButton>(R.id.backStructureBtn)
        var pos: Int = 0

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddWellViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_add_well, parent, false)
        return AddWellViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: AddWellViewHolder,
        position: Int
    ) {
        try {
            val currentOption = addWellResponse
            Log.d("POSITION", position.toString())
            holder.wellNumberTV.text = currentOption[position].name
            holder.childStructureNameTV.text =
                currentOption[position].structures[holder.pos].name
            Log.d("HELLOADAPTER3", currentOption[position].structures[holder.pos].name)

            holder.childRecyclerView.layoutManager = LinearLayoutManager(holder.itemView.context)
            if (currentOption[position].structures[holder.pos].structure_descriptions != null) {
                adapter =
                    ChildAddWellAdapter(addWellResponse[position].structures[holder.pos].structure_descriptions,null)
                holder.childRecyclerView.adapter = adapter
            }
            Log.d("POSITIONEXPANDABLE", currentOption[position].name)
            val isExpandable = currentOption[position].isExpandable
            Log.d("POSITIONEXPANDABLE", currentOption[position].isExpandable.toString())
            if (isExpandable) {
                holder.childRecyclerView.visibility = View.VISIBLE
                holder.childStructureNameTV.visibility = View.VISIBLE
                holder.nextStructureBtn.visibility = View.VISIBLE
                holder.backStructureBtn.visibility = View.VISIBLE
                holder.childRecyclerView.visibility = View.VISIBLE
                holder.wellNumberTV.setBackgroundResource(android.R.color.transparent)
            } else {
                holder.childRecyclerView.visibility = View.GONE
                holder.childStructureNameTV.visibility = View.GONE
                holder.nextStructureBtn.visibility = View.GONE
                holder.backStructureBtn.visibility = View.GONE
                holder.childRecyclerView.visibility = View.GONE
                holder.wellNumberTV.setBackgroundResource(R.drawable.btn_white)
            }


            holder.nextStructureBtn.setOnClickListener {
                Log.d("Expanded Next", "TRUE")
                if (holder.pos < currentOption[position].structures.size - 1) {
                    holder.pos++
                    interaction.onCLickNext(holder.pos)
                    holder.childStructureNameTV.text =
                        currentOption[position].structures[holder.pos].name
                    //Log.d("Child", holder.pos.toString())
                    adapter = if(data!=null){
                        ChildAddWellAdapter(addWellResponse[position].structures[holder.pos].structure_descriptions,data)
                    } else{
                        ChildAddWellAdapter(addWellResponse[position].structures[holder.pos].structure_descriptions,null)
                    }
                    holder.childRecyclerView.adapter = adapter
//                    holder.childRecyclerView.adapter =
//                        ChildAddWellAdapter(addWellResponse[position].structures[holder.pos].structure_descriptions,null)
                    holder.wellNumberTV.setBackgroundResource(android.R.color.transparent)
                    if (holder.pos == currentOption[position].structures.size - 1) {
                        holder.nextStructureBtn.visibility = View.GONE

                    }

                }

            }

            holder.backStructureBtn.setOnClickListener {
                if (holder.pos > 0) {
                    holder.pos--
                    interaction.onCLickBack(holder.pos)
                    holder.childStructureNameTV.text = currentOption[position].structures[holder.pos].name
                    holder.wellNumberTV.setBackgroundResource(android.R.color.transparent)
                    //Log.d("Child", data.toString())
                    //Log.d("Child", holder.pos.toString())

                    adapter =if(data!=null){
                        ChildAddWellAdapter(addWellResponse[position].structures[holder.pos].structure_descriptions,data)
                    } else{
                        ChildAddWellAdapter(addWellResponse[position].structures[holder.pos].structure_descriptions,null)
                    }
                    holder.childRecyclerView.adapter = adapter
//                    holder.childRecyclerView.adapter =
//                        ChildAddWellAdapter(currentOption[position].structures[holder.pos].structure_descriptions,
//                            data
//                        )
                    holder.wellNumberTV.setBackgroundResource(android.R.color.transparent)
                    holder.nextStructureBtn.visibility=View.VISIBLE

                } else {

                    Log.d("LASTITEM", "NONE")
                }
            }
            holder.constraintLayout.setOnClickListener {
                currentOption[position].isExpandable = !currentOption[position].isExpandable
                Log.d("optionChanged", position.toString())
                interaction.onClickOption(position)
                notifyItemChanged(position)
            }
        }catch (e : Exception)
        {
            Log.d("CRASHADAPTER", e.message.toString())
            Log.d("CRASHADAPTER", position.toString())
        }
    }

    override fun getItemCount(): Int {


        Log.d("SizeItem", addWellResponse.size.toString())
        Log.d("SizeItem","FIRST")
        return addWellResponse.size
    }

    fun setData(d: Publish?){
        data = d
    }

}

