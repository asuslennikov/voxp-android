package ru.voxp.android.presentation.law.card

import com.github.asuslennikov.mvvm.api.presentation.State
import ru.voxp.android.presentation.HasKey

open class LawCardState(
    val id: String,
    val title: String = "",
    val subtitle: String = "",
    val date: String = "",
    val url: String? = null
) : State, HasKey<String> {
    val titleVisible: Boolean = title.isNotBlank()
    val subtitleVisible: Boolean = subtitle.isNotBlank()
    val dateVisible: Boolean = date.isNotBlank()

    override fun getKey(): String = id

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (javaClass != other?.javaClass) {
            return false
        }

        if (id != (other as LawCardState).id) {
            return false
        }

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}