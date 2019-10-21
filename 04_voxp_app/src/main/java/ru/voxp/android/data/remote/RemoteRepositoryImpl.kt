package ru.voxp.android.data.remote

import android.text.TextUtils
import io.reactivex.Observable
import retrofit2.HttpException
import ru.voxp.android.data.di.ManagerScope
import ru.voxp.android.domain.api.ExceptionType.*
import ru.voxp.android.domain.api.VoxpException
import ru.voxp.android.domain.api.model.ResponseContainer
import ru.voxp.android.domain.api.model.SearchRequest
import ru.voxp.android.domain.api.remote.RemoteRepository
import java.io.IOException
import javax.inject.Inject

@ManagerScope
class RemoteRepositoryImpl @Inject constructor(
    private val retrofitRepository: RetrofitRepository
) : RemoteRepository {
    override fun getLastLaws(searchRequest: SearchRequest, page: Int?, limit: Int?): Observable<ResponseContainer> {
        val searchQueries = HashMap<String, String?>()
        if (!TextUtils.isEmpty(searchRequest.name)) {
            searchQueries["name"] = searchRequest.name
        }
        return retrofitRepository.getLastLaws(searchQueries, page ?: 1, limit ?: 20)
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