package ru.voxp.android.data.remote

import io.reactivex.Observable
import retrofit2.http.GET

interface RetrofitRepository {
    @GET("/api/v1/laws?sort=date")
    fun getLastLaws(): Observable<ru.voxp.android.domain.api.model.ResponseContainer>
}