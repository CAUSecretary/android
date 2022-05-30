package com.example.causecretary.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.causecretary.databinding.ItemRecyclerBinding
import com.example.causecretary.ui.data.Uncertified

class AdminAdapter(list: List<Uncertified>): RecyclerView.Adapter<Holder>() {
    var adminList = list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemRecyclerBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.binding.apply {
            item = adminList[position]
        }
    }

    override fun getItemCount(): Int {
        return adminList.size
    }
}
class  Holder(val binding:ItemRecyclerBinding):RecyclerView.ViewHolder(binding.root)