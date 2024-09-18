package com.example.myapp.data.remote
import com.example.myapp.data.model.Employee
import retrofit2.Response
import retrofit2.http.*

interface IEmployeeService {
    @GET("employees")
    suspend fun getEmployees(): Response<List<Employee>>

    @GET("employees/{id}")
    suspend fun getEmployeeById(@Path("id") id: Number): Employee

    @POST("employees")
    suspend fun createEmployee(@Body employee: Employee): Response<Employee>

    @PUT("employees/{id}")
    suspend fun updateEmployee(@Path("id") id: Number, @Body employee: Employee): Employee

    @DELETE("employees/{id}")
    suspend fun deleteEmployee(@Path("id") id: Number)
}