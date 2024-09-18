package com.example.myapp.ViewModels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.myapp.data.model.Employee
import com.example.myapp.data.repository.EmployeeRepository
import com.example.myapp.utils.isNetworkAvailable
import com.example.myapp.utils.locationService
import kotlinx.coroutines.launch
import java.time.Instant

class EmployeeFormViewModel(private val repository: EmployeeRepository, application: Application): ViewModel() {
    private val locationService: locationService = locationService()
    val app = application
    fun setEmployee(fullname: String, date: String, application: Application) {

    }

    fun submitEmpoloyee(fullname: String, date: String, application: Application) {
        viewModelScope.launch {
            println("ADDIGN")
            val employees: List<Employee>? = repository.getEmployeesOffline()
            val location = locationService.getLocation(application)
            if (location != null) {
                val employee = Employee(
                    id = 0,
                    fullName = fullname,
                    dateOfBirth = date,
                    avatar = "https://static.vecteezy.com/system/resources/previews/019/879/186/non_2x/user-icon-on-transparent-background-free-png.png",
                    latitude = location.latitude,
                    longitude = location.longitude,
                    utcDate = Instant.now().toString(),
                    createdAt = System.currentTimeMillis(),
                    local = null
                )
                if (isNetworkAvailable(application)) {
                    println("NETWORK")
                    employee.local = false
                    val result = repository.addEmploye(employee)
                } else {
                    println("NO NETWORK")
                    employee.local = true
                    if (employees != null) {
                        employee.id = employees.last().id + 1

                    }

                    val result = repository.addEmployeeOffline(employee)
                }


            } else {
                val employee = Employee(
                    id = 0,
                    fullName = fullname,
                    dateOfBirth = date,
                    avatar = "https://static.vecteezy.com/system/resources/previews/019/879/186/non_2x/user-icon-on-transparent-background-free-png.png",
                    latitude = 0.0,
                    longitude = 0.0,
                    utcDate = Instant.now().toString(),
                    createdAt = System.currentTimeMillis(),
                    local = null
                )

                if (isNetworkAvailable(application)) {
                    employee.local = false
                    val result = repository.addEmploye(employee)
                } else {
                    employee.local = true
                    if (employees != null) {
                        employee.id = employees.last().id + 1

                    }
                    val result = repository.addEmployeeOffline(employee)
                }
            }

        }


    }

    fun updateEmployee(editEmployee: Employee) {
        viewModelScope.launch {
            val location = locationService.getLocation(app)

            if (location != null) {
                val employee = Employee(
                    id = editEmployee.id,
                    fullName = editEmployee.fullName,
                    dateOfBirth = editEmployee.dateOfBirth,
                    avatar = "https://static.vecteezy.com/system/resources/previews/019/879/186/non_2x/user-icon-on-transparent-background-free-png.png",
                    latitude = location.latitude,
                    longitude = location.longitude,
                    utcDate = Instant.now().toString(),
                    createdAt = editEmployee.createdAt,
                    local = null
                )
                if (isNetworkAvailable(app)) {
                    println("NETWORK")
                    employee.local = false
                    val result = repository.addEmploye(employee)
                } else {
                    println("NO NETWORK")
                    employee.local = true
                    var result = repository.updateOffline(employee)
                }
            } else {
                val employee = Employee(
                    id = editEmployee.id,
                    fullName = editEmployee.fullName,
                    dateOfBirth = editEmployee.dateOfBirth,
                    avatar = "https://static.vecteezy.com/system/resources/previews/019/879/186/non_2x/user-icon-on-transparent-background-free-png.png",
                    latitude = 0.0,
                    longitude = 0.0,
                    utcDate = Instant.now().toString(),
                    createdAt = editEmployee.createdAt,
                    local = null
                )
                if (isNetworkAvailable(app)) {
                    employee.local = false
                    val result = repository.updateEmployee(employee)
                } else {
                    employee.local = true
                    val result = repository.updateOffline(employee)
                }

            }

        }
    }
}



class EmployeeFormViewModelFactory(private val repository : EmployeeRepository, private val application: Application): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return EmployeeFormViewModel(repository,application ) as T
    }
}