package ru.voxp.android.data.api

import retrofit2.Call
import retrofit2.http.GET
import ru.voxp.android.data.api.model.ResponseContainer

// TODO interface MUST be located in domain module and has no implementation details (Call return type)
interface VoxpManager {
    @GET("/api/v1/laws?sort=date")
    fun getLastLaws(): Call<ResponseContainer>
}