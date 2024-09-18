package com.example.myapp.activities

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.myapp.R
import com.example.myapp.ViewModels.EmployeeFormViewModel
import com.example.myapp.ViewModels.EmployeeFormViewModelFactory
import com.example.myapp.ViewModels.EmployeeViewModel
import com.example.myapp.ViewModels.EmployeeViewModelFactory
import com.example.myapp.activities.ui.theme.MyAppTheme
import com.example.myapp.data.model.Employee
import com.example.myapp.data.repository.EmployeeRepository
import com.example.myapp.utils.convertToEmployee
import java.util.Calendar

class FormActivity : ComponentActivity() {
    val repository = EmployeeRepository.getInstance(this)
    lateinit var dateEdt: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.form_layout)

        dateEdt = findViewById<EditText>(R.id.idEdtDate)


        dateEdt.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)
            val datePickerDialog = DatePickerDialog(
                this,
                { view, year, monthOfYear, dayOfMonth ->

                    val dat = (dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year)
                    dateEdt.setText(dat)
                },
                year,
                month,
                day
            )
            datePickerDialog.show()
        }

        val eFullName = findViewById<EditText>(R.id.FullName)
        val eDateBirth = findViewById<EditText>(R.id.idEdtDate)
        val btnSubmit = findViewById<Button>(R.id.btnSubmit)
        var editEmploye: Employee? = null
        val  extras = intent.extras
        if(extras != null){
            var json =extras.getString("employee")
            if(json != null) {
                editEmploye = convertToEmployee(json)
                eFullName.setText(editEmploye.fullName)
                eDateBirth.setText(editEmploye.dateOfBirth)
            }
        }


        val viewModel : EmployeeFormViewModel by viewModels{EmployeeFormViewModelFactory(repository, application)}


        btnSubmit.setOnClickListener{
            if(editEmploye != null){
                editEmploye.fullName =  eFullName.text.toString()
                editEmploye.dateOfBirth = eDateBirth.text.toString()
                viewModel.updateEmployee(editEmploye)
                finish()
            } else {
                val fullName = eFullName.text.toString()
                val dateBirth = eDateBirth.text.toString()
                viewModel.submitEmpoloyee(fullName,dateBirth,application)
                finish()
            }

        }
    }



}

