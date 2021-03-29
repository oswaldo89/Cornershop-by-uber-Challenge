package com.cornershop.counterstest.presentation.counters_list.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cornershop.counterstest.R
import com.cornershop.counterstest.data.model.Counter
import com.cornershop.counterstest.data.repository.counter.CounterDataSourceImpl
import com.cornershop.counterstest.databinding.ActivityCountersListBinding
import com.cornershop.counterstest.domain.usecases.counter.CounterUseCasesImpl
import com.cornershop.counterstest.presentation.BaseActivity
import com.cornershop.counterstest.presentation.counter_add.ui.CounterAddActivity
import com.cornershop.counterstest.presentation.counters_list.adapter.CounterListAdapter
import com.cornershop.counterstest.presentation.counters_list.adapter.ICounterList
import com.cornershop.counterstest.presentation.counters_list.viewmodel.CountersListViewModel
import com.cornershop.counterstest.presentation.counters_list.viewmodel.VMFactory
import com.cornershop.counterstest.presentation.utils.extencion_functions.visible
import com.cornershop.counterstest.presentation.utils.extencion_functions.visibleOrGone
import com.cornershop.counterstest.presentation.utils.extencion_functions.visibleOrInvisible
import com.cornershop.counterstest.presentation.utils.vibrateOnTouch
import com.cornershop.counterstest.utils.Resource


class CountersListActivity : BaseActivity<ActivityCountersListBinding>(), ICounterList {

    override fun getViewBinding(): ActivityCountersListBinding =
        ActivityCountersListBinding.inflate(layoutInflater)

    private val viewModel by viewModels<CountersListViewModel> { VMFactory( CounterUseCasesImpl( CounterDataSourceImpl() ) ) }
    private val countersListAdapter: CounterListAdapter = CounterListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupRecyclerView()
        setupObserveCountersList()
    }

    private fun setupRecyclerView() {
        binding.rvCounters.setHasFixedSize(true)
        binding.rvCounters.layoutManager = LinearLayoutManager(this)
        recyclerScrollListener()
    }

    private fun recyclerScrollListener() {
        binding.rvCounters.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0 && binding.btnAddNewCounter.visibility == View.VISIBLE) {
                    binding.btnAddNewCounter.hide()
                } else if (dy < 0 && binding.btnAddNewCounter.visibility != View.VISIBLE) {
                    binding.btnAddNewCounter.show()
                }
            }
        })
    }

    private fun setupObserveCountersList() {
        viewModel.counterList.observe(this) { result ->
            when (result) {
                is Resource.Loading -> showLoading(true)
                is Resource.Success -> setLayoutInformation(result.data)
                is Resource.Failure -> showLoading(false)
            }
        }
        //observe when another process altering the counters list ( refresh )
        viewModel.counterListChange.observe(this) { result ->
            setLayoutInformation(result.data)
        }
    }

    private fun setLayoutInformation(data: List<Counter>) {
        showLoading(false)
        if (data.isNotEmpty()) setCountersData(data) else emptyLayout(true)
    }

    private fun showLoading(loading: Boolean) {
        if (loading) binding.progressBar.visibleOrGone(true) else binding.progressBar.visibleOrGone(false )
    }

    private fun emptyLayout(isVisible: Boolean) {
        binding.textNumberItems.visibleOrGone(!isVisible)
        binding.textNumberTimes.visibleOrGone(!isVisible)
        binding.emptyLayout.content.visibleOrGone(isVisible)
    }

    private fun setCountersData(data: List<Counter>) {
        emptyLayout(false)
        countersListAdapter.recyclerAdapter(data, this)
        binding.rvCounters.adapter = countersListAdapter
        binding.textNumberItems.text = String.format(getString(R.string.n_items), countersListAdapter.itemCount)
    }

    override fun onItemClick(item: Counter, position: Int) {

        Log.v("tamaño", "${countersListAdapter.getSelectedItems()}")
        if (countersListAdapter.getSelectedItems() > 0) {
            countersListAdapter.singleSelection(item, position)
            binding.rvCounters.vibrateOnTouch()
        }

        if(countersListAdapter.getSelectedItems() == 0)
            disableToolbar()
    }

    override fun onItemLongClick(item: Counter, position: Int) {
        countersListAdapter.setSelected(item, position)
        binding.rvCounters.vibrateOnTouch()
        enableToolbar()
    }

    private fun enableToolbar(){
        showToolbar()
        binding.cardSeachView.visibleOrInvisible(false, animate = false)
    }

    private fun disableToolbar(){
        hideToolbar()
        binding.cardSeachView.visibleOrInvisible(true, animate = true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                hideToolbar(); true
            }
            R.id.action_delete -> {
                Toast.makeText(this, "click on delete", Toast.LENGTH_LONG).show(); true
            }
            R.id.action_share -> {
                Toast.makeText(this, "click on share", Toast.LENGTH_LONG).show(); true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    fun addNewCounterBtn(@Suppress("UNUSED_PARAMETER") view: View) {
        startForAddCounterResult.launch(Intent(this, CounterAddActivity::class.java))
    }

    private val startForAddCounterResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = result.data
                intent?.let {
                    val counters = it.getParcelableArrayListExtra<Counter>("counters_list")!!
                    viewModel.changeCountersList(ArrayList(counters))
                }
            }
        }
}