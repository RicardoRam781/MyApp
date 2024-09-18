package com.example.myapp.data.model
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "employees")
data class Employee(
   @PrimaryKey(autoGenerate = true) var id: Int,
    var fullName: String,
    var dateOfBirth: String,
    val avatar: String,
    val latitude: Double,
    val longitude: Double,
    val utcDate: String,
    val createdAt: Long,
    var local:Boolean?
)
