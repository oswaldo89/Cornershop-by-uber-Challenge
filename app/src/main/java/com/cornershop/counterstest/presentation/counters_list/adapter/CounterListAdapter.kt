package com.cornershop.counterstest.presentation.counters_list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cornershop.counterstest.R
import com.cornershop.counterstest.data.model.Counter
import com.cornershop.counterstest.databinding.ItemCounterListBinding
import com.cornershop.counterstest.presentation.utils.extencion_functions.visibleOrInvisible

class CounterListAdapter : RecyclerView.Adapter<CounterListAdapter.ViewHolder>() {

    private var counters: List<Counter> = ArrayList()
    private lateinit var iCounterList: ICounterList

    fun recyclerAdapter(counters: List<Counter>, iCounterList: ICounterList) {
        this.counters = counters
        this.iCounterList = iCounterList
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = counters[position]
        holder.bind(item, iCounterList, position)
    }

    fun getSelectedCounters() : List<Counter>{
        return counters.filter { it.selected }
    }

    fun getTotalSelectedCounters() =  counters.filter { it.selected }.size

    override fun getItemCount(): Int = counters.size

    fun getTimesCounters() : Int{
        return counters.map { it.count }.sum()
    }

    fun disableAllSelections(){
        counters.map {  it.selected = false }
        notifyDataSetChanged()
    }

    fun singleSelection( item: Counter, position: Int){
        if(getTotalSelectedCounters() > 0){
            changeCounterState(item,position)
        }
    }

    fun setSelected(item: Counter, position: Int){
        changeCounterState(item,position)
    }

    private fun changeCounterState(item: Counter, position: Int){
        val it = counters.find { it.title == item.title }
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

            itemBinding.increaseBtn.setOnClickListener {
                iCounterList.onIncreaseClick(item)
            }
            itemBinding.decreaseBtn.setOnClickListener {
                iCounterList.onDecreaseClick(item)
            }
        }
    }
}