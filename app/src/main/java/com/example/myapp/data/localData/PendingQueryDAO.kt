package com.example.myapp.data.localData

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.myapp.data.model.PendingQuery


@Dao
interface PendingQueryDAO {
    @Insert
    suspend fun insert(query: PendingQuery)

    @Query("DELETE FROM pendingQuerys")
    suspend fun clear()

    @Query("SELECT * FROM pendingQuerys")
    suspend fun getAllQuerys():List<PendingQuery>
}