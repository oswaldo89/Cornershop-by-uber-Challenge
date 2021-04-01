package com.cornershop.counterstest.presentation.counters_list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cornershop.counterstest.R
import com.cornershop.counterstest.data.model.Counter
import com.cornershop.counterstest.databinding.ItemCounterListBinding
import com.cornershop.counterstest.presentation.utils.extencion_functions.visibleOrInvisible
import android.widget.Filter
import android.widget.Filterable
import java.util.*
import kotlin.collections.ArrayList

class CounterListAdapter : RecyclerView.Adapter<CounterListAdapter.ViewHolder>(), Filterable {

    private var countersFilterList = ArrayList<Counter>()
    private var counters: List<Counter> = ArrayList()
    private lateinit var iCounterList: ICounterList



    fun recyclerAdapter(counters: List<Counter>, iCounterList: ICounterList) {
        this.counters = counters
        this.iCounterList = iCounterList
        this.countersFilterList = ArrayList(counters)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = countersFilterList[position]
        holder.bind(item, iCounterList, position)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                countersFilterList = if (charSearch.isEmpty()) {
                    ArrayList(counters)
                } else {
                    val resultList = ArrayList<Counter>()
                    for (row in counters) {
                        if (row.title.toLowerCase(Locale.ROOT).contains(charSearch.toLowerCase(Locale.ROOT))) {
                            resultList.add(row)
                        }
                    }
                    resultList
                }
                val filterResults = FilterResults()
                filterResults.values = countersFilterList
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                countersFilterList = results?.values as ArrayList<Counter>
                notifyDataSetChanged()
            }
        }
    }

    fun getSelectedCounters() : List<Counter>{
        return countersFilterList.filter { it.selected }
    }

    fun getTotalSelectedCounters() =  countersFilterList.filter { it.selected }.size

    override fun getItemCount(): Int = countersFilterList.size

    fun getTimesCounters() : Int{
        return countersFilterList.map { it.count }.sum()
    }

    fun disableAllSelections(){
        countersFilterList.map {  it.selected = false }
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
        val it = countersFilterList.find { it.title == item.title }
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