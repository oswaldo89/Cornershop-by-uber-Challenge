package com.cornershop.counterstest.presentation.home

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cornershop.counterstest.R
import com.cornershop.counterstest.data.model.Counter
import com.cornershop.counterstest.databinding.ActivityHomeBinding
import com.cornershop.counterstest.presentation.BaseActivity
import com.cornershop.counterstest.presentation.new_counter.NewCounterActivity


class HomeActivity : BaseActivity<ActivityHomeBinding>() {

    override fun getViewBinding(): ActivityHomeBinding = ActivityHomeBinding.inflate(layoutInflater)

    private val countersListAdapter: CounterListAdapter = CounterListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupRecyclerView()
        setDummyData()

        //dumy
        binding.emptyLayout.content.visibility = View.GONE
        binding.progressBar.visibility = View.GONE
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

    private fun setDummyData() {

        val data = ArrayList<Counter>()

        data.add(Counter(1,"Cups of coffee", 5))
        data.add(Counter(2,"Records played", 10))
        data.add(Counter(3,"Number of times I’ve forgotten my mother’s name because I was high on Frugelés.", 2))
        data.add(Counter(4,"Apples eaten", 0))
        data.add(Counter(1,"Cups of coffee", 5))
        data.add(Counter(2,"Records played", 10))
        data.add(Counter(3,"Number of times I’ve forgotten my mother’s name because I was high on Frugelés.", 2))
        data.add(Counter(4,"Apples eaten", 0))
        data.add(Counter(1,"Cups of coffee", 5))
        data.add(Counter(2,"Records played", 10))
        data.add(Counter(3,"Number of times I’ve forgotten my mother’s name because I was high on Frugelés.", 2))
        data.add(Counter(4,"Apples eaten", 0))

        countersListAdapter.recyclerAdapter(data, this)
        binding.rvCounters.adapter = countersListAdapter

        binding.textNumberItems.text = String.format(getString(R.string.n_items), countersListAdapter.itemCount)
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

    fun addNewCounter(view: View) {
        Intent(this, NewCounterActivity::class.java).apply {
            startActivity(this)
        }
    }

}