package com.appstyx.authtest.network

import com.appstyx.authtest.models.Gender
import com.appstyx.authtest.models.LoginRequest
import com.appstyx.authtest.models.LoginResponse
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

const val BASE_URL = "https://asx-auth-test.herokuapp.com/api/v1/"

private val httpClient = OkHttpClient.Builder()
    .addInterceptor(
        Interceptor { chain ->
            val builder = chain.request()
                .newBuilder()
                .header("Server", "Cowboy")
                .header("Connection", "keep-alive")
                .header("X-Powered-By", "Express")
                .header("Content-Type", "application/json")
                .header("Content-Length", "383")
                .header("Etag", "W/\"17f-jTSkWw1bi52EIuLETpr7XLj1UPs\"")
                .header("Vary", "Accept-Encoding")
                .header("Date", "Thu, 24 Jun 2021 13:29:09 GMT")
                .header("Via", "1.1 vegur")
                .build()

            return@Interceptor chain.proceed(builder)
        }
    )
    .build()

interface ApiService {

        companion object {
            fun getService(): ApiService {
                val retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addCallAdapterFactory(CoroutineCallAdapterFactory())
                    .addConverterFactory(GsonConverterFactory.create())
//                    .client(httpClient)
                    .build()

                return retrofit.create(ApiService::class.java)
            }
        }

    @GET("genders")
    suspend fun getGenders(): Gender

    @POST("users")
    @Headers("Content-Type: application/json")
    fun login(
        @Body request: LoginRequest
    ): Call<LoginResponse>
}