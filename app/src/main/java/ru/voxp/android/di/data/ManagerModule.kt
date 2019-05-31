package ru.voxp.android.di.data

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.voxp.android.data.HOST_PLACEHOLDER
import ru.voxp.android.data.HostInterceptor
import ru.voxp.android.data.VoxpApi

@Module
internal object ManagerModule {

    @Provides
    @ManagerScope
    fun sharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
    }

    @Provides
    @ManagerScope
    fun okHttpClient(): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(HostInterceptor())
            .build()

    @Provides
    @ManagerScope
    fun retrofit(httpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .client(httpClient)
            .baseUrl(HOST_PLACEHOLDER)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @ManagerScope
    fun voxpApi(retrofit: Retrofit): VoxpApi =
        retrofit.create(VoxpApi::class.java)
}
