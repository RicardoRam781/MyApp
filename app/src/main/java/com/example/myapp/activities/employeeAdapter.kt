package com.example.myapp.activities

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapp.R
import com.example.myapp.data.model.Employee
import com.google.android.material.floatingactionbutton.FloatingActionButton

class EmployeeAdapter(private var employees: MutableList<Employee>, private val onDeleteClick:(Employee) -> Unit, private val onUpdateClick:(Employee) -> Unit): RecyclerView.Adapter<EmployeeAdapter.EmployeeViewHolder>() {


        class EmployeeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val fullNameTextView: TextView = itemView.findViewById(R.id.fullNameTextView)
            private val btnDelete: FloatingActionButton = itemView.findViewById(R.id.btnDelete)
            private val btnUpdate: FloatingActionButton  = itemView.findViewById(R.id.btnEdit)
            private val avatarImageView: ImageView = itemView.findViewById(R.id.header_image)


                

            fun bind(employee: Employee, onDeleteClick:(Employee) -> Unit, onUpdateClick:(Employee) -> Unit) {
                fullNameTextView.text = employee.fullName
                Glide.with(itemView.context)
                    .load(employee.avatar)
                    .placeholder(R.drawable.baseline_downloading_24)
                    .error(R.drawable.baseline_error_24)
                    .into(avatarImageView)

                btnDelete.setOnClickListener{
                    onDeleteClick(employee)

                }
                btnUpdate.setOnClickListener{
                    onUpdateClick(employee)
                }
            }


        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmployeeViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_employee, parent, false)
            return EmployeeViewHolder(view)
        }

        override fun onBindViewHolder(holder: EmployeeViewHolder, position: Int) {
            holder.bind(employees[position], onDeleteClick, onUpdateClick)
        }

        override fun getItemCount() = employees.size

    fun updateEmployees(newEmployees: List<Employee>) {
        employees = newEmployees.toMutableList()
        notifyDataSetChanged()
    }
    fun removeEmployee(employee: Employee){
        val index = employees.indexOf(employee)
        employees.removeAt(index)
        notifyItemRemoved(index);
    }
}