package com.example.myapp.data.localData

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.myapp.data.model.Employee
import com.example.myapp.data.model.PendingQuery
import java.util.concurrent.Executors

@Database(entities = [Employee::class, PendingQuery::class], version = 1, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {
    abstract fun employeeDao(): EmployeeDAO
    abstract fun pendingQueryDao(): PendingQueryDAO

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {

            val db = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java, "app_database"
            )
                .setJournalMode(RoomDatabase.JournalMode.TRUNCATE)
                .fallbackToDestructiveMigration()
                .setQueryCallback({ sqlQuery, bindArgs ->
                    Log.d("RoomLog", "SQL Query: $sqlQuery SQL Args: ${bindArgs.joinToString()}")
                }, Executors.newSingleThreadExecutor())
                .build()

            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).build()
                INSTANCE = instance
                instance
            }

        }
    }

}