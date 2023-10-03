package com.example.filechangepdf.retrofit

import com.locker.app.Config.Companion.API_URL
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit

object RetrofitClient {
    @Volatile
    private var retrofit: Retrofit? = null

    fun getInstance(): Retrofit {
        if (retrofit == null) {
            synchronized(Retrofit::class.java) {
                retrofit = Retrofit.Builder()
                    .baseUrl(API_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient())
                    .build()
            }
        }
        return retrofit!!
    }

    private fun okHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BASIC

        return OkHttpClient().newBuilder().apply {
            addNetworkInterceptor(loggingInterceptor)
            connectTimeout(1000 * 10, TimeUnit.SECONDS)
            writeTimeout(1000 * 10, TimeUnit.SECONDS)
            readTimeout(1000 * 10, TimeUnit.SECONDS)
            addInterceptor(AppInterceptor())

        }.build()

    }

    class AppInterceptor : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response = with(chain) {
            val newRequest = request().newBuilder()
                .addHeader("Authorization", "accessToken")
                .build()
            proceed(newRequest)
        }
    }


}
