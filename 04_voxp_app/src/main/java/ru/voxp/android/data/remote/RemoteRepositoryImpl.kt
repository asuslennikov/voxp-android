package ru.voxp.android.data.remote

import io.reactivex.Observable
import retrofit2.HttpException
import ru.voxp.android.data.di.ManagerScope
import ru.voxp.android.domain.api.ExceptionType.*
import ru.voxp.android.domain.api.VoxpException
import ru.voxp.android.domain.api.model.ResponseContainer
import ru.voxp.android.domain.api.remote.RemoteRepository
import java.io.IOException
import javax.inject.Inject

@ManagerScope
class RemoteRepositoryImpl @Inject constructor(
    private val retrofitRepository: RetrofitRepository
) : RemoteRepository {
    override fun getLastLaws(page: Int?, limit: Int?): Observable<ResponseContainer> {
        return retrofitRepository.getLastLaws(page ?: 1, limit ?: 20)
            .onErrorResumeNext { originCause: Throwable -> mapError(originCause) }
    }

    private fun mapError(originCause: Throwable): Observable<ResponseContainer> {
        val resultException: Throwable =
            when (originCause) {
                is HttpException -> VoxpException(SERVER, "Communication with remote repository failed", originCause)
                is IOException -> VoxpException(CONNECTION, "Communication with remote repository failed", originCause)
                else -> VoxpException(CLIENT, "Remote repository invocation failed", originCause)
            }
        return Observable.error(resultException)
    }
}