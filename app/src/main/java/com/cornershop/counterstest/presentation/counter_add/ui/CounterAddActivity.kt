package com.cornershop.counterstest.presentation.counter_add.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.observe
import com.cornershop.counterstest.data.model.Counter
import com.cornershop.counterstest.data.repository.counter.CounterDataSourceImpl
import com.cornershop.counterstest.databinding.ActivityCounterAddBinding
import com.cornershop.counterstest.domain.usecases.counter.CounterUseCasesImpl
import com.cornershop.counterstest.presentation.counter_add.viewmodel.CounterAddViewModel
import com.cornershop.counterstest.presentation.counters_list.viewmodel.VMFactory
import com.cornershop.counterstest.presentation.utils.closeKeyboard
import com.cornershop.counterstest.presentation.utils.extencion_functions.makeLinks
import com.cornershop.counterstest.presentation.utils.extencion_functions.visibleOrGone
import com.cornershop.counterstest.presentation.utils.extencion_functions.visibleOrInvisible
import com.cornershop.counterstest.utils.Resource


class CounterAddActivity : AppCompatActivity() {

    private val viewModel by viewModels<CounterAddViewModel> { VMFactory( CounterUseCasesImpl( CounterDataSourceImpl() ) ) }
    lateinit var binding: ActivityCounterAddBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCounterAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.textExamples.makeLinks(
            Pair("examples", View.OnClickListener {
                Toast.makeText(applicationContext, "Go to Example activity.!", Toast.LENGTH_SHORT).show()
            })
        )

        setupObserveAddCounter()
    }

    private fun setupObserveAddCounter() {
        viewModel.addCounterObservable.observe(this) { result ->
            when (result) {
                is Resource.Loading -> saving()
                is Resource.Success -> addCounterSuccess(result.data)
                is Resource.Failure -> finish()
            }
        }
    }

    private fun addCounterSuccess(data: List<Counter>) {
        val intent = Intent()
        intent.putParcelableArrayListExtra("counters_list", ArrayList(data))
        setResult(RESULT_OK, intent)
        finish()
    }

    private fun saving(){
        binding.saveBtn.visibleOrInvisible(false, animate = false)
        binding.progressBar.visibleOrGone(true)
    }

    fun closeScreenBtn(@Suppress("UNUSED_PARAMETER") view: View) {
        finish()
    }

    fun saveCounterBtn(@Suppress("UNUSED_PARAMETER") view: View) {
        closeKeyboard(binding.textCounterName)
        if(binding.textCounterName.text.isNotEmpty()){
            viewModel.addCounter(binding.textCounterName.text.toString())
        }else{
            Toast.makeText(this,"Enter a name, please.", Toast.LENGTH_LONG).show()
        }
    }
}