package com.example.causecretary.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.causecretary.databinding.EventadminRecyclerBinding
import com.example.causecretary.databinding.EventdetailRecyclerBinding

class EventAdminAdapter: RecyclerView.Adapter<EventAdminHolder>()  {
    lateinit var test : MutableList<Test>
    fun setList(){
        val a=Test("신나는 캡스톤","2022년 5월20일 ~ 2022년 6일 20일")
        test.add(a)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventAdminHolder {
        val binding = EventadminRecyclerBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return EventAdminHolder(binding)
    }

    override fun onBindViewHolder(holder: EventAdminHolder, position: Int) {
        val a = test[position]
        holder.binding.tvTitle.text=a.title
        holder.binding.tvPeriod.text=a.period

        //row 리스너
        holder.binding.root.setOnClickListener {
            itemClickListener.onClick(it,position)
        }
    }

    override fun getItemCount(): Int {
        return test.size
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