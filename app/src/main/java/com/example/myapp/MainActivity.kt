package com.example.myapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle

import android.widget.Toast

import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity

import androidx.lifecycle.Observer

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapp.ViewModels.EmployeeViewModel
import com.example.myapp.ViewModels.EmployeeViewModelFactory
import com.example.myapp.activities.EmployeeAdapter

import com.example.myapp.activities.FormActivity

import com.example.myapp.data.model.Employee

import com.example.myapp.data.repository.EmployeeRepository

import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson


class MainActivity() : AppCompatActivity() {

    val repository by lazy { EmployeeRepository.getInstance(this)}
    private val gson = Gson()
    private lateinit var employeeAdapter: EmployeeAdapter
    val viewModel : EmployeeViewModel by viewModels{EmployeeViewModelFactory(repository, application)}
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_layout)
        val button = findViewById<FloatingActionButton>(R.id.btnNav)
        val btnSync = findViewById<FloatingActionButton>(R.id.btnSync)
        button.setOnClickListener {
            val intent = Intent(this, FormActivity::class.java)
            startActivity(intent)
        }
        btnSync.setOnClickListener{
            viewModel.syncData()
            viewModel.state.observe(this) {
                it
                if (it) {
                    Toast.makeText(this, "Sync", Toast.LENGTH_SHORT).show()
                    viewModel.fetchEmployees()
                } else {
                    Toast.makeText(this, "Nothing to sync", Toast.LENGTH_SHORT).show()
                }
            }
        }

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)



        viewModel.employees.observe(this, Observer { employees ->

            employeeAdapter = EmployeeAdapter(employees.toMutableList(), onDeleteClick = { employee: Employee ->
                viewModel.deleteEmploye(employee)
                employeeAdapter.removeEmployee(employee)
            }, onUpdateClick = {
                val intent = Intent(this, FormActivity::class.java)
                intent.putExtra("employee", gson.toJson(it))
                startActivity(intent)

            })
            recyclerView.adapter = employeeAdapter
            employeeAdapter.updateEmployees(employees)
        })
        viewModel.fetchEmployees()


    }
    override fun onResume() {
        super.onResume()
        viewModel.fetchEmployees()
    }

    override fun onStart() {
        super.onStart()

    }


}


