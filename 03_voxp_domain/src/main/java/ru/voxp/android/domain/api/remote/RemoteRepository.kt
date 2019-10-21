package ru.voxp.android.domain.api.remote

import io.reactivex.Observable
import ru.voxp.android.domain.api.model.ResponseContainer
import ru.voxp.android.domain.api.model.SearchRequest

interface RemoteRepository {
    fun getLastLaws(searchRequest: SearchRequest, page: Int?, limit: Int?): Observable<ResponseContainer>
}
