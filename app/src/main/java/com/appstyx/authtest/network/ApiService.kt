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
            val request = chain.request()
            val requestBody = request.body


            if (requestBody != null) {
                request.newBuilder()
                    .header("Content-Type", "application/json")
                    .post(requestBody)
                    .build()
            }

            return@Interceptor chain.proceed(request)
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
                    .client(httpClient)
                    .build()

                return retrofit.create(ApiService::class.java)
            }
        }

    @GET("genders")
    suspend fun getGenders(): Gender

    @Headers("Content-Type: application/json")
    @POST("users")
    fun login(
        @Body request: LoginRequest
    ): Call<LoginResponse>
}