package com.cornershop.counterstest.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.cornershop.counterstest.R


abstract class BaseActivity<B : ViewBinding> : AppCompatActivity() {

    lateinit var binding: B
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = getViewBinding()
        setContentView(binding.root)

        setSupportActionBar(binding.root.findViewById(R.id.countersMenuToolbar))
        supportActionBar?.let {
            it.hide()
            it.setHomeAsUpIndicator(R.drawable.ic_close)
            it.setDisplayHomeAsUpEnabled(true)
        }
    }

    fun changeTitleToolbar(numberOfItemsSelected : Int){
        supportActionBar?.title = String.format(getString(R.string.n_selected), numberOfItemsSelected)
    }

    fun showToolbar() {
        supportActionBar?.show()
    }

    fun hideToolbar() {
        supportActionBar?.hide()
    }

    abstract fun getViewBinding(): B

}
