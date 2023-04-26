package com.example.credential_manager.todo

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.credential_manager.databinding.ActivityTodoBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.random.Random

@AndroidEntryPoint
class TodoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTodoBinding

    private val viewModel by lazy { ViewModelProvider(this)[TodoViewModel::class.java] }
    private val adapter by lazy {
        TodoAdapter(
            onClickDelete = { deleteTodo(it) }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTodoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        binding.rvTodos.adapter = adapter
        getTodos()

        binding.btnAdd.setOnClickListener {
            val description = binding.description.text.toString()

            lifecycleScope.launch {
                if (description.isNotBlank()) {
                    addTodo(description)
                }
            }
        }

        binding.btnGetTodos.setOnClickListener {
            getTodos()
        }


    }

    private fun addTodo(description: String) {
        viewModel.addTodo(Todo(Random.nextInt(0, 999), description))
        binding.description.text.clear()
        hideKeyboard()
        getTodos()
    }

    private fun getTodos() {
        binding.loadingProgress.visibility = View.VISIBLE
        binding.rvTodos.visibility = View.GONE
        viewModel.getTodos().observe(this) {
            if (!it.isNullOrEmpty()) {
                binding.loadingProgress.visibility = View.GONE
                binding.rvTodos.visibility = View.VISIBLE
            }

            adapter.setTodos(it)
        }
    }

    private fun deleteTodo(id: Int) {
        viewModel.deleteTodo(id)
        getTodos()
    }


    fun hideKeyboard() {
        try {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}