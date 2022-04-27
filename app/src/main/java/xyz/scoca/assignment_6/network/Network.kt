package xyz.scoca.assignment_6.network

import androidx.viewbinding.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import xyz.scoca.assignment_6.view.common.Base
import java.util.concurrent.TimeUnit

class Network {

    var apiService : ApiService? = null

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(Base.BASE_URL)
            .client(getClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        apiService = retrofit.create(ApiService::class.java)
    }

    private fun getClient(): OkHttpClient {
        val httpClient = OkHttpClient.Builder()
        httpClient.readTimeout(60, TimeUnit.SECONDS)
        httpClient.writeTimeout(60, TimeUnit.SECONDS)
        httpClient.connectTimeout(60, TimeUnit.SECONDS)
        httpClient.addInterceptor(createHttpLoggingInterceptor(BuildConfig.DEBUG))
        return httpClient.build()
    }

    private fun createHttpLoggingInterceptor(debugMode: Boolean): HttpLoggingInterceptor {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        if (debugMode) httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        else httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.NONE
        return httpLoggingInterceptor
    }
}