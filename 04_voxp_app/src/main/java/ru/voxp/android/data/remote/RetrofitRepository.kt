package ru.voxp.android.data.remote

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query
import ru.voxp.android.domain.api.model.ResponseContainer

interface RetrofitRepository {
    @GET("/api/v1/laws?sort=date")
    fun getLastLaws(@Query("page") page: Int, @Query("limit") limit: Int): Observable<ResponseContainer>
}