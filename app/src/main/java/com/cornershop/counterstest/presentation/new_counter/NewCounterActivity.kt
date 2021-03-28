package com.cornershop.counterstest.presentation.new_counter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.cornershop.counterstest.R
import com.cornershop.counterstest.databinding.ActivityNewCounterBinding
import com.cornershop.counterstest.presentation.utils.makeLinks

class NewCounterActivity : AppCompatActivity() {

    lateinit var binding: ActivityNewCounterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNewCounterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.textExamples.makeLinks(
            Pair("examples", View.OnClickListener {
                Toast.makeText(applicationContext, "Examples clicked", Toast.LENGTH_SHORT).show()
            })
        )
    }
}