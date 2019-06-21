package ru.voxp.android.data.api

import io.reactivex.Observable
import retrofit2.http.GET
import ru.voxp.android.domain.api.model.ResponseContainer

// TODO interface MUST be located in domain module and has no implementation details (Call return type)
interface VoxpManager {
    @GET("/api/v1/laws?sort=date")
    fun getLastLaws(): Observable<ResponseContainer>
}