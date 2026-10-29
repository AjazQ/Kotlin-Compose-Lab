package com.ajaz.kotlinlab.data.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import java.util.concurrent.TimeUnit

data class TodoDto(
    val userId: Int,
    val id: Int,
    val title: String,
    val completed: Boolean
)

interface JsonPlaceholderApi {
    @GET("todos/{id}")
    suspend fun getTodo(@Path("id") id: Int): TodoDto
}

object ApiClient {
    private const val BASE_URL = "https://jsonplaceholder.typicode.com/"

    val api: JsonPlaceholderApi by lazy {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .build()

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(JsonPlaceholderApi::class.java)
    }
}
