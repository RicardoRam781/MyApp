package com.example.myapp.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

object NetworkModule {
    private const val BASE_URL = "https://636a9855c07d8f936da2ad92.mockapi.io/api/v1/"

    // Configuración del cliente HTTP
    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    // Configuración de Retrofit
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create()) // Convertidor JSON a objetos
            .build()
    }

    // Proporcionar el servicio
    fun provideEmployeeService(): IEmployeeService {
        return provideRetrofit().create(IEmployeeService::class.java)
    }
}