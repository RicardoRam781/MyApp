package com.example.myapp.utils
import com.example.myapp.data.model.Employee
import com.google.gson.Gson

fun convertToEmployee(jsonString: String): Employee {
    val gson = Gson()
    return gson.fromJson(jsonString, Employee::class.java)
}
