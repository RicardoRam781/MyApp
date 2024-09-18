package com.example.myapp.ViewModels

import android.app.Application
import android.provider.CalendarContract.SyncState
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.myapp.data.model.Employee
import com.example.myapp.data.repository.EmployeeRepository
import com.example.myapp.utils.isNetworkAvailable
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class EmployeeViewModel(private val repository : EmployeeRepository , application: Application,): AndroidViewModel(application) {

    private val _employees = MutableLiveData<List<Employee>>()
    val employees: MutableLiveData<List<Employee>> get() = _employees


    private val _state = MutableLiveData(false)
    val state:  MutableLiveData<Boolean>  get() = _state




    fun fetchEmployees() {
        viewModelScope.launch {
            if(isNetworkAvailable(getApplication())){
                println("FETCHING_ONLINE")
                _employees.value = repository.getEmployees()
            } else{
                println("NO_CONNECTION")
                _employees.value = repository.getEmployeesOffline()
            }

        }

    }
    fun deleteEmploye(employee: Employee){
        viewModelScope.launch {
            if(isNetworkAvailable(getApplication())){
                println("FETCHING_ONLINE")
               repository.deleteEmploye(employee)
            } else{
                println("NO_CONNECTION")
                repository.deleteOfline(employee)
            }

        }
    }



    fun syncData(){

        viewModelScope.launch {
            if(isNetworkAvailable(getApplication())){
                println("FETCHING_ONLINE")
                val result = repository.syncData()

                _state.value = result
            } else{
                println("NO_CONNECTION")

                _state.value = false
            }

        }

    }

}
class EmployeeViewModelFactory(private val repository : EmployeeRepository, private val application: Application): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return EmployeeViewModel(repository, application ) as T
    }
}


