package com.cornershop.counterstest.presentation.counters_list.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cornershop.counterstest.data.model.Counter
import com.cornershop.counterstest.databinding.ItemCounterListBinding

class CounterListAdapter : RecyclerView.Adapter<CounterListAdapter.ViewHolder>() {

    private var items: List<Counter> = ArrayList()

    fun recyclerAdapter(items: List<Counter>) {
        this.items = items
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCounterListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(
            binding
        )
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder(private val itemBinding: ItemCounterListBinding) : RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(item: Counter) {
            itemBinding.txtTitle.text = item.title
            itemBinding.txtCountNumber.text = "${item.count}"
        }
    }
}