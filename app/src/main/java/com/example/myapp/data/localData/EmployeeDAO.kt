package com.example.myapp.data.localData

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.myapp.data.model.Employee

@Dao
interface EmployeeDAO {
    @Insert
    suspend fun insert(employee: Employee):Void

    @Query("SELECT * FROM employees")
    suspend fun getAll(): List<Employee>

    @Query("SELECT * FROM employees WHERE id = :employeeId")
    suspend fun getById(employeeId:Int):Employee

    @Update
    suspend fun updateEmployee(employee: Employee):Void

    @Delete
    suspend fun deleteEmployee(employee: Employee):Void

}