package ru.voxp.android.di.data

import android.content.Context
import android.content.SharedPreferences
import dagger.Binds
import dagger.Module
import dagger.Provides
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level.BODY
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.jackson.JacksonConverterFactory
import ru.voxp.android.BuildConfig
import ru.voxp.android.data.network.NetworkManagerImpl
import ru.voxp.android.data.remote.RemoteRepositoryImpl
import ru.voxp.android.data.remote.RetrofitRepository
import ru.voxp.android.domain.api.network.NetworkManager
import ru.voxp.android.domain.api.remote.RemoteRepository

@Module
internal abstract class ManagerModule {

    @Binds
    @ManagerScope
    abstract fun bindsNetworkManager(networkManager: NetworkManagerImpl): NetworkManager

    @Binds
    @ManagerScope
    abstract fun bindsRemoteRepository(remoteRepository: RemoteRepositoryImpl): RemoteRepository

    @Module
    companion object {
        @Provides
        @JvmStatic
        @ManagerScope
        fun sharedPreferences(context: Context): SharedPreferences {
            return context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
        }

        @Provides
        @JvmStatic
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
        @JvmStatic
        @ManagerScope
        fun retrofit(httpClient: OkHttpClient): Retrofit =
            Retrofit.Builder()
                .client(httpClient)
                .baseUrl("http://voxp.ru")
                .addConverterFactory(JacksonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .build()

        @Provides
        @JvmStatic
        @ManagerScope
        fun retrofitRepository(retrofit: Retrofit): RetrofitRepository =
            retrofit.create(RetrofitRepository::class.java)
    }
}
