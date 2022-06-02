package com.example.causecretary.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.causecretary.databinding.EventdetailRecyclerBinding

class EventDetailAdapter(imgs: MutableList<String>): RecyclerView.Adapter<EventDetailHolder>() {

    var imgList = imgs

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventDetailHolder {
        val binding = EventdetailRecyclerBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return EventDetailHolder(binding)
    }

    override fun onBindViewHolder(holder: EventDetailHolder, position: Int) {
        val img = imgList[position]
        Glide.with(holder.binding.root)
            .load(img)
            .into(holder.binding.ibEvent)
    }

    override fun getItemCount(): Int {
        return imgList.size
    }

    fun deleteList(position: Int){
        imgList.removeAt(position)
        notifyDataSetChanged()
    }
    fun getList(position: Int): String {
        return imgList[position]
    }
}

class  EventDetailHolder(val binding: EventdetailRecyclerBinding):RecyclerView.ViewHolder(binding.root)