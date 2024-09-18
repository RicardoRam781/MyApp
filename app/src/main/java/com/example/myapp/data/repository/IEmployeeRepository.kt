package com.example.myapp.data.repository

import com.example.myapp.data.model.Employee

interface IEmployeeRepository {
    suspend fun getEmployees():List<Employee>?
    suspend fun getById(id:Number): Employee?
    suspend fun addEmploye(employee: Employee):Employee?
    suspend fun updateEmployee(employee: Employee): Employee?
    suspend fun deleteEmploye(employee: Employee)
    suspend fun syncData():Boolean
}