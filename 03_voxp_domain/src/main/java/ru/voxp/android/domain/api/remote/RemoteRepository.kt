package ru.voxp.android.domain.api.remote

import io.reactivex.Observable
import ru.voxp.android.domain.api.model.ResponseContainer

interface RemoteRepository {
    fun getLastLaws(page: Int?, limit: Int?): Observable<ResponseContainer>
}
