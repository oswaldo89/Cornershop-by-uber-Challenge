package com.cornershop.counterstest.presentation.counter_add

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.cornershop.counterstest.databinding.ActivityCounterAddBinding
import com.cornershop.counterstest.presentation.utils.makeLinks

class CounterAddActivity : AppCompatActivity() {

    lateinit var binding: ActivityCounterAddBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCounterAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.textExamples.makeLinks(
            Pair("examples", View.OnClickListener {
                Toast.makeText(applicationContext, "Examples clicked", Toast.LENGTH_SHORT).show()
            })
        )
    }
}