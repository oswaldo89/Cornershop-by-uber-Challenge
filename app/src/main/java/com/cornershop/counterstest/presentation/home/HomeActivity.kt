package com.cornershop.counterstest.presentation.home

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.cornershop.counterstest.R
import com.cornershop.counterstest.databinding.ActivityHomeBinding
import com.cornershop.counterstest.presentation.BaseActivity

class HomeActivity : BaseActivity<ActivityHomeBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
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

    fun addCounter(view: View) {
        showToolbar()
    }

    override fun getViewBinding(): ActivityHomeBinding = ActivityHomeBinding.inflate(layoutInflater)
}