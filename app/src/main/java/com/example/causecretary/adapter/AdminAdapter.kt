package com.example.causecretary.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.causecretary.databinding.ItemRecyclerBinding
import com.example.causecretary.ui.data.Uncertified

class AdminAdapter(list: MutableList<Uncertified>): RecyclerView.Adapter<Holder>() {
    var adminList = list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemRecyclerBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.binding.apply {
            item = adminList[position]
        }

        //row 리스너
        holder.binding.root.setOnClickListener {
            itemClickListener.onClick(it,position)
        }
    }

    override fun getItemCount(): Int {
        return adminList.size
    }

    fun deleteList(position: Int){
        adminList.removeAt(position)
        notifyDataSetChanged()
    }

    //리스너 등록
    interface OnItemClickListener{
        fun onClick(v: View, position: Int)
    }
    private lateinit var itemClickListener : OnItemClickListener

    fun setItemClickListener(itemClickListener: OnItemClickListener) {
        this.itemClickListener = itemClickListener
    }
}
class  Holder(val binding:ItemRecyclerBinding):RecyclerView.ViewHolder(binding.root)