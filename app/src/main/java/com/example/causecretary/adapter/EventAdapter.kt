package com.example.causecretary.adapter

import android.view.LayoutInflater
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
}
class  EventHolder(val binding: EventRecyclerBinding):RecyclerView.ViewHolder(binding.root)