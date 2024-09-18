package com.example.myapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "pendingQuerys")
data class PendingQuery(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val operationType: String,
    val data: String,

)
