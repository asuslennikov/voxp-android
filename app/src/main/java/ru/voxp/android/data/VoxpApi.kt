package ru.voxp.android.data

import retrofit2.Call
import retrofit2.http.GET

interface VoxpApi {
    @GET("/api/v1/laws")
    fun getLastLaws(): Call<String>
}