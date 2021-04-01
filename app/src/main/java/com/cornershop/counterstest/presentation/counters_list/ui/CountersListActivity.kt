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
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cornershop.counterstest.R
import com.cornershop.counterstest.data.model.Counter
import com.cornershop.counterstest.databinding.ActivityCountersListBinding
import com.cornershop.counterstest.presentation.BaseActivity
import com.cornershop.counterstest.presentation.counter_add.ui.CounterAddActivity
import com.cornershop.counterstest.presentation.counters_list.adapter.CounterListAdapter
import com.cornershop.counterstest.presentation.counters_list.adapter.ICounterList
import com.cornershop.counterstest.presentation.counters_list.viewmodel.CountersListViewModel
import com.cornershop.counterstest.presentation.dialogs.ErrorDialog
import com.cornershop.counterstest.presentation.utils.extencion_functions.gone
import com.cornershop.counterstest.presentation.utils.extencion_functions.visibleOrGone
import com.cornershop.counterstest.presentation.utils.extencion_functions.visibleOrInvisible
import com.cornershop.counterstest.presentation.utils.vibrateOnTouch
import com.cornershop.counterstest.utils.Constants
import com.cornershop.counterstest.utils.Resource
import com.cornershop.counterstest.utils.States
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CountersListActivity : BaseActivity<ActivityCountersListBinding>(), ICounterList {

    override fun getViewBinding(): ActivityCountersListBinding = ActivityCountersListBinding.inflate(layoutInflater)

    private val viewModel by viewModels<CountersListViewModel>()
    private val countersListAdapter: CounterListAdapter = CounterListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupRecyclerView()
        setupObserveCountersList()
        setupFilterList()

        viewModel.attempGetData(States.INITIAL)
    }

    private fun setupFilterList() {
        binding.inputSearch.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                countersListAdapter.filter.filter(newText)
                return false
            }
        })
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
                is Resource.Failure -> { showLoading(false); showError(result.errorMessage) }
                is Resource.NetworkError -> {showLoading(false); networkErrorLayout()}
            }
        }
        //observe when another process altering the counters list ( refresh )
        viewModel.counterListChange.observe(this) { result ->
            setLayoutInformation(result.data)
        }
        //when deleting any counters
        viewModel.observeDeletedItems.observe(this) { result ->
            when (result) {
                is Resource.Loading -> showLoading(true)
                is Resource.Success -> setLayoutInformation(result.data)
                is Resource.Failure -> { showLoading(false); showError(result.errorMessage) }
                is Resource.NetworkError -> {showLoading(false);  showError(getString(R.string.connection_error_description)) }
            }
        }
        //observe when counter is modifing
        viewModel.observeCounterModification.observe(this) { result ->
            when (result) {
                is Resource.Loading -> showLoading(true)
                is Resource.Success -> setLayoutInformation(result.data)
                is Resource.Failure -> { showLoading(false); showError(result.errorMessage) }
                is Resource.NetworkError -> {showLoading(false); showError(getString(R.string.connection_error_description))}
            }
        }
    }

    private fun showError(errorMessage: String?) {
        ErrorDialog(errorMessage).show(supportFragmentManager, "ErrorDialogFragment")
    }

    private fun networkErrorLayout() {
        binding.networkLayout.content.visibleOrGone(true)
    }

    private fun setLayoutInformation(data: List<Counter>) {
        showLoading(false)
        initialState()
        if (data.isNotEmpty()) setCountersData(data) else showEmptyLayout(true)
    }

    private fun showLoading(loading: Boolean) {
        if (loading) binding.progressBar.visibleOrGone(true) else binding.progressBar.gone(false )
    }

    private fun showEmptyLayout(isVisible: Boolean) {
        binding.textNumberItems.visibleOrGone(!isVisible)
        binding.textNumberTimes.visibleOrGone(!isVisible)
        binding.emptyLayout.content.visibleOrGone(isVisible)
        binding.rvCounters.visibleOrGone(!isVisible,false)
    }

    private fun setCountersData(data: List<Counter>) {
        binding.networkLayout.content.visibleOrGone(false)
        showEmptyLayout(false)
        countersListAdapter.recyclerAdapter(data, this)
        binding.rvCounters.adapter = countersListAdapter
        binding.textNumberItems.text = String.format(getString(R.string.n_items), countersListAdapter.itemCount)
        binding.textNumberTimes.text = String.format(getString(R.string.n_times), countersListAdapter.getTimesCounters())
    }

    private fun initialState(){
        countersListAdapter.disableAllSelections()
        disableToolbar()
    }

    private fun enableAndUpdateToolbar(){
        showToolbar()
        binding.cardSeachView.visibleOrInvisible(false, animate = false)

        //update title description.
        val selectedCount = countersListAdapter.getTotalSelectedCounters()
        changeTitleToolbar(selectedCount)
    }

    private fun disableToolbar(){
        hideToolbar()
        binding.cardSeachView.visibleOrInvisible(true, animate = true)
    }

    override fun onItemClick(item: Counter, position: Int) {

        if (countersListAdapter.getTotalSelectedCounters() > 0) {
            countersListAdapter.singleSelection(item, position)
            binding.rvCounters.vibrateOnTouch()
        }

        if(countersListAdapter.getTotalSelectedCounters() == 0)
            disableToolbar()

        enableAndUpdateToolbar()
    }

    override fun onItemLongClick(item: Counter, position: Int) {
        countersListAdapter.setSelected(item, position)
        binding.rvCounters.vibrateOnTouch()
        enableAndUpdateToolbar()
    }

    override fun onIncreaseClick(item: Counter) {
        viewModel.modificationCounter(item, Constants.INCREASE)
    }

    override fun onDecreaseClick(item: Counter) {
        viewModel.modificationCounter(item, Constants.DECREASE)
    }

    override fun onBackPressed() {
        if(countersListAdapter.getTotalSelectedCounters() > 0){
            initialState()
        }else{
            super.onBackPressed()
        }
    }

    override fun onResume() {
        super.onResume()
        if(countersListAdapter.getTotalSelectedCounters() > 0){
            enableAndUpdateToolbar()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                initialState(); true
            }
            R.id.action_delete -> { deleteCounters(); true }
            R.id.action_share -> {
                Toast.makeText(this, "click on share", Toast.LENGTH_LONG).show(); true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun deleteCounters(){
        val countersSelected = countersListAdapter.getSelectedCounters()
        viewModel.deleteCountersList(ArrayList(countersSelected))
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    fun addNewCounterBtn(@Suppress("UNUSED_PARAMETER") view: View) {
        startForAddCounterResult.launch(Intent(this, CounterAddActivity::class.java))
    }

    fun retryBtn(@Suppress("UNUSED_PARAMETER") view: View) {
        viewModel.attempGetData(States.RETRY)
    }

    private val startForAddCounterResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = result.data
                intent?.let {
                    initialState()
                    val counters = it.getParcelableArrayListExtra<Counter>("counters_list")!!
                    viewModel.changeCountersList(ArrayList(counters))
                }
            }
        }
}