package com.example.causecretary.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.causecretary.databinding.EventRecyclerBinding
import com.example.causecretary.ui.data.EventOffResult
import com.example.causecretary.ui.data.Uncertified


class EventAdapter(eventBuildList: MutableList<EventOffResult>) : RecyclerView.Adapter<EventHolder>() {

    var eventList = eventBuildList


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventHolder {
        val binding = EventRecyclerBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return EventHolder(binding)
    }

    override fun onBindViewHolder(holder: EventHolder, position: Int) {
        val event = eventList[position]
        holder.binding.apply {
            tvEventBelong.text=event.belong
            tvEventLocation.text=event.location
            tvEventName.text=event.eventName
        }

        //row 리스너
        holder.binding.root.setOnClickListener {
            itemClickListener.onClick(it,position)
        }
    }

    override fun getItemCount(): Int {
        return eventList.size
    }

    fun deleteList(position: Int){
        eventList.removeAt(position)
        notifyDataSetChanged()
    }
    fun getList(position: Int): EventOffResult {
        return eventList[position]
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


class  EventHolder(val binding: EventRecyclerBinding):RecyclerView.ViewHolder(binding.root)