package com.example.causecretary.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.causecretary.databinding.ItemRecyclerBinding
import com.example.causecretary.ui.data.Uncertified

class AdminAdapter(list: MutableList<Uncertified>): RecyclerView.Adapter<AdminHolder>() {
    var adminList = list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminHolder {
        val binding = ItemRecyclerBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return AdminHolder(binding)
    }

    override fun onBindViewHolder(holder: AdminHolder, position: Int) {
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
    fun getList(position: Int):Uncertified{
        return adminList[position]
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
class  AdminHolder(val binding:ItemRecyclerBinding):RecyclerView.ViewHolder(binding.root)