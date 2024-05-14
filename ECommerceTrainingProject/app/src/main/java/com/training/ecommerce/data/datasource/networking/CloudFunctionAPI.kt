package com.training.ecommerce.data.datasource.networking

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.training.ecommerce.data.models.GenericResponse
import com.training.ecommerce.data.models.auth.RegisterRequestModel
import com.training.ecommerce.data.models.auth.RegisterResponseModel
import com.training.ecommerce.data.models.user.AuthProvider
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import java.io.Reader
import java.util.concurrent.TimeUnit

interface CloudFunctionAPI {

    @POST("registerUser")
    suspend fun registerUser(
        @Body registerRequest: RegisterRequestModel
    ): Response<GenericResponse<RegisterResponseModel>>

    // i cant add it to cloud function :)
    @POST("registerWithSocialMedia")
    suspend fun registerWithSocialMedia(
          idToken :String,
          provider:String
    ): Response<GenericResponse<RegisterResponseModel>>

    companion object {
        private const val BASE_URL = "https://us-central1-android-e-commerce-training.cloudfunctions.net/"
        private const val TIME_OUT = 60L
        fun create(): CloudFunctionAPI {
            val okHttpClient =
                OkHttpClient
                    .Builder()
                    .readTimeout(TIME_OUT, TimeUnit.SECONDS)
                    .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
                    .addInterceptor { HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BODY }.intercept(it)
                    }
                    .addInterceptor { ErrorLoggingInterceptor().intercept(it) }
                    .build()

            val retrofit: CloudFunctionAPI by lazy {
                Retrofit.Builder().addConverterFactory(
                    GsonConverterFactory.create(
                        GsonBuilder().setLenient().create()
                    )
                ).baseUrl(BASE_URL).client(okHttpClient).build()
                    .create(CloudFunctionAPI::class.java)
            }

            return retrofit
        }
    }
}

fun handleErrorResponse(response: Reader): String {
    val errorResponse = try {
        Gson().fromJson(
            response, GenericResponse::class.java
        ).message
    } catch (e: Exception) {
        e.message
    }
    return errorResponse ?: ""
}