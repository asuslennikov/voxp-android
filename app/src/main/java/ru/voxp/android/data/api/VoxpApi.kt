package ru.voxp.android.data.api

import retrofit2.Call
import retrofit2.http.GET
import ru.voxp.android.data.api.model.ResponseContainer

interface VoxpApi {
    @GET("/api/v1/laws")
    fun getLastLaws(): Call<ResponseContainer>
}