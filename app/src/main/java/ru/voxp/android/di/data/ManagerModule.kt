package ru.voxp.android.di.data

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level.BODY
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import ru.voxp.android.BuildConfig
import ru.voxp.android.data.api.VoxpManager

@Module
internal object ManagerModule {

    @Provides
    @ManagerScope
    fun sharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
    }

    @Provides
    @ManagerScope
    fun okHttpClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()
        if (BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = BODY
            builder.addInterceptor(loggingInterceptor)
        }
        return builder.build()
    }

    @Provides
    @ManagerScope
    fun retrofit(httpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .client(httpClient)
            .baseUrl("http://voxp.ru")
            .addConverterFactory(JacksonConverterFactory.create())
            .build()

    @Provides
    @ManagerScope
    fun voxpApi(retrofit: Retrofit): VoxpManager =
        retrofit.create(VoxpManager::class.java)
}
