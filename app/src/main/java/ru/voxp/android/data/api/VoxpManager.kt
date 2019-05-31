package ru.voxp.android.data.api

import retrofit2.Call
import retrofit2.http.GET
import ru.voxp.android.data.api.model.ResponseContainer

interface VoxpManager {
    @GET("/api/v1/laws?sort=date")
    fun getLastLaws(): Call<ResponseContainer>
}