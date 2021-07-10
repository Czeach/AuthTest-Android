package com.appstyx.authtest.network

import com.appstyx.authtest.models.Gender
import com.appstyx.authtest.models.LoginRequest
import com.appstyx.authtest.models.LoginResponse
import com.appstyx.authtest.models.UserData
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

const val BASE_URL = "https://asx-auth-test.herokuapp.com/api/v1/"

private val httpClient = OkHttpClient.Builder()
    .connectTimeout(500, TimeUnit.SECONDS)
    .readTimeout(500, TimeUnit.SECONDS)
    .writeTimeout(500, TimeUnit.SECONDS)
    .addInterceptor(
        HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)
    )
    .build()

interface ApiService {

        companion object {

            private val gsonBuilder: GsonBuilder = GsonBuilder().serializeNulls()

            fun getService(): ApiService {
                val retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addCallAdapterFactory(CoroutineCallAdapterFactory())
                    .addConverterFactory(GsonConverterFactory.create(gsonBuilder.create()))
                    .client(httpClient)
                    .build()

                return retrofit.create(ApiService::class.java)
            }
        }

    @GET("genders")
    suspend fun getGenders(): Gender

    @Headers("Content-Type: application/json")
    @POST("users")
    suspend fun login(
        @Body body: LoginRequest
    ): LoginResponse

    @GET("users/me")
    suspend fun getUserData(
        @Header("Authorization") token: String
    ): UserData
}