package com.example.zazulinsepr_1_2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.zazulinsepr_1_2.apiPackage.RetrofitClient
import com.example.zazulinsepr_1_2.apiPackage.Todo
import com.example.zazulinsepr_1_2.apiPackage.TodoAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Tab1Fragment : Fragment() {

    // UI элементы
    private lateinit var progressBar: ProgressBar
    private lateinit var todosLayout: LinearLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var buttonLoad: Button
    private lateinit var textStatus: TextView

    // Адаптер для списка
    private lateinit var adapter: TodoAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tab1, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Находим UI элементы
        setupUI(view)

        // Настраиваем RecyclerView
        setupRecyclerView()

        // Настраиваем кнопку загрузки
        setupLoadButton()
    }

    private fun setupUI(view: View) {
        progressBar = view.findViewById(R.id.progressBar)
        todosLayout = view.findViewById(R.id.todosLayout)
        recyclerView = view.findViewById(R.id.recyclerView)
        buttonLoad = view.findViewById(R.id.buttonLoad)
        textStatus = view.findViewById(R.id.textStatus)
    }

    private fun setupRecyclerView() {
        adapter = TodoAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun setupLoadButton() {
        buttonLoad.setOnClickListener {
            loadTodos()
        }
    }

    private fun loadTodos() {
        showLoading(true)

        val call = RetrofitClient.todoApi.getTodos()

        call.enqueue(object : Callback<List<Todo>> {
            override fun onResponse(call: Call<List<Todo>>, response: Response<List<Todo>>) {
                showLoading(false)
                if (response.isSuccessful) {
                    val todos = response.body()
                    if (todos != null && todos.isNotEmpty()) {
                        val first10Todos = todos.take(10)
                        displayTodos(first10Todos)
                    } else {
                        showError("Не получилось загрузить задачи")
                    }
                } else {
                    showError("Ошибка сервера: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<Todo>>, t: Throwable) {
                showLoading(false)
                showError("Ошибка сети: ${t.message}")
            }
        })
    }

    private fun displayTodos(todos: List<Todo>) {
        adapter.submitList(todos)
        todosLayout.visibility = View.VISIBLE

        val completedCount = todos.count { it.completed }
        textStatus.text = "Загружено ${todos.size} задач\nВыполнено: $completedCount"

        Toast.makeText(requireContext(), "Задачи загружены!", Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(show: Boolean) {
        progressBar.visibility = if (show) View.VISIBLE else View.GONE
        buttonLoad.isEnabled = !show
    }

    private fun showError(message: String) {
        textStatus.text = message
        todosLayout.visibility = View.GONE
    }
}
