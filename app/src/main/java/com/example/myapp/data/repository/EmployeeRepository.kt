package com.example.myapp.data.repository

import android.content.Context
import com.example.myapp.data.localData.AppDatabase
import com.example.myapp.data.localData.EmployeeDAO
import com.example.myapp.data.localData.PendingQueryDAO
import com.example.myapp.data.model.Employee
import com.example.myapp.data.model.PendingQuery
import com.example.myapp.data.remote.IEmployeeService
import com.example.myapp.data.remote.NetworkModule
import com.example.myapp.utils.convertToEmployee
import com.google.gson.Gson
import java.util.concurrent.Executors


class EmployeeRepository(private val employeeService: IEmployeeService, private val localData: EmployeeDAO, private val PendingQueryDAO: PendingQueryDAO  ): IEmployeeRepository {
    val gson = Gson()
    override suspend fun syncData(): Boolean {
        val querys = PendingQueryDAO.getAllQuerys()
        println("querys"+querys)
        try {
            if (querys.isNotEmpty()) {
                querys.forEach {
                    println("EMPLOYE_SYNCINC" + it.data)
                    var employee = convertToEmployee(it.data)

                    if (it.operationType == "POST") {
                        println("POST_SYNC")
                        addEmploye(employee)
                    } else if (it.operationType == "PUT") {
                        println("PUT_SYNC")
                        updateEmployee(employee)
                    } else {
                        println("DELETING_SYNC")
                        deleteEmploye(employee)
                    }

                }
                PendingQueryDAO.clear()
                return true

            } else {
                return false
            }
        } catch (e: Exception) {
            println(e)
            return false
        }

    }


    suspend fun getEmployeesOffline():List<Employee>? {
        val employee = localData.getAll();
        println(employee)
        return employee
    }
    suspend fun addEmployeeOffline(employee: Employee){
        println("ADDING_OFFLINE" + employee)
        try{
            if(employee.local == true){
                val response =  localData.insert(employee)
                val operation = PendingQuery(
                    operationType = "POST",
                    data = gson.toJson(employee)
                )
                PendingQueryDAO.insert(operation)
            } else {
                val response =  localData.insert(employee)
            }
        }catch (e:Exception){
            println("EXCEPTION ADDING OFFLINE" + e)
        }
    }
    suspend fun getByIdOffline(id: Int):Employee?{
        val response = localData.getById(id)
        if(response != null){
            return response
        } else{
            return null
        }
    }
    suspend fun updateOffline(employee: Employee){
        try{
            val response = localData.updateEmployee(employee)
            val operation = PendingQuery(
                operationType = "PUT",
                data = gson.toJson(employee)
            )
            PendingQueryDAO.insert(operation)
        }catch (e:Exception){
            println("UPDATE_EXCEPTION" + e)
        }

    }
    suspend fun deleteOfline(employee:Employee){

        try{
            val response = localData.deleteEmployee(employee)
            val operation = PendingQuery(
                operationType = "DELETE",
                data = gson.toJson(employee)
            )
            PendingQueryDAO.insert(operation)

        }catch (e:Exception){
            println("DELETE_EXCEPTION" + e)
        }
    }

    override suspend fun getEmployees(): List<Employee>? {
        val response = employeeService.getEmployees()
            //Verificar cuales registros existen para no solapar id locales con remotos
        return try {
            println(response.body())
            val employees = response.body() // Devuelve la lista de empleados si la respuesta fue exitosa
            val localList = localData.getAll()
            if(localList.isEmpty()){
                if(employees != null){
                    employees.forEach{ employee: Employee ->
                        employee.local = false
                        addEmployeeOffline(employee)
                    }
                }
            }
            employees
        } catch (e: Exception) {
            localData.getAll()
        }
        }
    override suspend fun getById(id: Number): Employee? {
        return try{
            val employee = employeeService.getEmployeeById(id)
            return employee
        }catch (e:Exception){
            return null
        }
    }

    override suspend fun addEmploye(employee: Employee): Employee? {
        return try  {
            val response = employeeService.createEmployee(employee)
            println(response.body())
            response.body()
        } catch(e:Exception){
            localData.insert(employee)
            return employee
        }


    }

    override suspend fun updateEmployee(employee: Employee): Employee? {
        return try{
            val employee = employeeService.updateEmployee(employee.id,employee)
            return employee
        }catch (e:Exception){
            return null

        }
    }

    override suspend fun deleteEmploye(employee: Employee) {
      try{
          val result = employeeService.deleteEmployee(employee.id)
      }catch (e:Exception){
          println("ERROR_DELETE"+e)
      }
    }

    companion object {

        private val service = NetworkModule.provideEmployeeService()

        @Volatile
        private var INSTANCE: EmployeeRepository? = null

        fun getInstance(context: Context): EmployeeRepository {

            return INSTANCE ?: synchronized(this) {

                val database = AppDatabase.getDatabase(context)
                val instance = EmployeeRepository(service, database.employeeDao(), database.pendingQueryDao())
                INSTANCE = instance
                instance
            }
        }
    }


}
