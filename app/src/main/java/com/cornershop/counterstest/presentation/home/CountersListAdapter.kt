package com.cornershop.counterstest.presentation.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cornershop.counterstest.data.model.Counter
import com.cornershop.counterstest.databinding.ItemCounterListBinding

class CountersListAdapter : RecyclerView.Adapter<CountersListAdapter.ViewHolder>() {

    private var items: List<Counter> = ArrayList()
    private lateinit var context: Context

    fun recyclerAdapter(items: List<Counter>, context: Context) {
        this.items = items
        this.context = context
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item, context)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCounterListBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)

    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder(private val itemBinding: ItemCounterListBinding) : RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(item: Counter, context: Context) {
            //itemBinding.txtTitle.text = item.name
        }

    }
}