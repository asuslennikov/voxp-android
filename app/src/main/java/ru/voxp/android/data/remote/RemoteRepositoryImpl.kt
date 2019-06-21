package ru.voxp.android.data.remote

import io.reactivex.Observable
import ru.voxp.android.di.data.ManagerScope
import ru.voxp.android.domain.api.model.ResponseContainer
import ru.voxp.android.domain.api.remote.RemoteRepository
import javax.inject.Inject

@ManagerScope
class RemoteRepositoryImpl @Inject constructor(
    private val retrofitRepository: RetrofitRepository
) : RemoteRepository {

    override fun getLastLaws(): Observable<ResponseContainer> {
        return retrofitRepository.getLastLaws()
    }
}