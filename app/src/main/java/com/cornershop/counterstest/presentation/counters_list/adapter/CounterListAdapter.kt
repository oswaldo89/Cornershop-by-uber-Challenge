package com.cornershop.counterstest.presentation.counters_list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cornershop.counterstest.R
import com.cornershop.counterstest.data.model.Counter
import com.cornershop.counterstest.databinding.ItemCounterListBinding
import com.cornershop.counterstest.presentation.utils.extencion_functions.visibleOrInvisible

class CounterListAdapter : RecyclerView.Adapter<CounterListAdapter.ViewHolder>() {

    private var items: List<Counter> = ArrayList()
    private lateinit var iCounterList: ICounterList

    fun recyclerAdapter(items: List<Counter>, iCounterList: ICounterList) {
        this.items = items
        this.iCounterList = iCounterList
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item, iCounterList, position)
    }

    fun getSelectedItems() =  items.filter { it.selected }.size

    fun singleSelection( item: Counter, position: Int){
        if(getSelectedItems() > 0){
            changeItemState(item,position)
        }
    }

    fun setSelected(item: Counter, position: Int){
        changeItemState(item,position)
    }

    private fun changeItemState(item: Counter, position: Int){
        val it = items.find { it.title == item.title }
        it?.let {
            it.selected = !it.selected
            notifyItemChanged(position)
        }
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
        fun bind( item: Counter, iCounterList: ICounterList, position: Int ) {
            itemBinding.txtTitle.text = item.title
            itemBinding.txtCountNumber.text = "${item.count}"

            if(item.selected){
                itemBinding.itemContent.setBackgroundResource(R.drawable.selected_counter)
            }else{
                itemBinding.itemContent.setBackgroundResource(0)
            }

            itemBinding.decreaseBtn.visibleOrInvisible(!item.selected, animate = false)
            itemBinding.txtCountNumber.visibleOrInvisible(!item.selected, animate = false)
            itemBinding.increaseBtn.visibleOrInvisible(!item.selected, animate = false)
            itemBinding.checkBtn.visibleOrInvisible(item.selected, animate = false)

            itemBinding.itemContent.setOnClickListener {
                iCounterList.onItemClick(item,position)
            }
            itemBinding.itemContent.setOnLongClickListener {
                iCounterList.onItemLongClick(item,position)
                true
            }
        }
    }
}