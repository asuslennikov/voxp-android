package ru.voxp.android.data.remote

import io.reactivex.Observable
import ru.voxp.android.di.data.ManagerScope
import javax.inject.Inject

@ManagerScope
class RemoteRepositoryImpl @Inject constructor(
    private val retrofitRepository: RetrofitRepository
) : ru.voxp.android.domain.api.remote.RemoteRepository {

    override fun getLastLaws(): Observable<ru.voxp.android.domain.api.model.ResponseContainer> {
        return retrofitRepository.getLastLaws()
    }
}