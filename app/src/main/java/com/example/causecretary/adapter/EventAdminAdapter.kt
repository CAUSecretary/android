package com.example.causecretary.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.causecretary.databinding.EventadminRecyclerBinding
import com.example.causecretary.databinding.EventdetailRecyclerBinding
import com.example.causecretary.ui.data.EventUserListResult

class EventAdminAdapter(userList: MutableList<EventUserListResult>): RecyclerView.Adapter<EventAdminHolder>()  {
    var list = userList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventAdminHolder {
        val binding = EventadminRecyclerBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return EventAdminHolder(binding)
    }

    override fun onBindViewHolder(holder: EventAdminHolder, position: Int) {
        val event = list[position]
        holder.binding.tvTitle.text=event.eventName
        holder.binding.tvPeriod.text=event.period

        //row 리스너
        holder.binding.root.setOnClickListener {
            itemClickListener.onClick(it,position)
        }
    }

    fun getList(position: Int): EventUserListResult {
        return list[position]
    }

    override fun getItemCount(): Int {
        return list.size
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
class  EventAdminHolder(val binding: EventadminRecyclerBinding):RecyclerView.ViewHolder(binding.root)