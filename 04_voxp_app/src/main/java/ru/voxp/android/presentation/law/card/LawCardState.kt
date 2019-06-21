package ru.voxp.android.presentation.law.card

import ru.jewelline.mvvm.interfaces.HasKey
import ru.jewelline.mvvm.interfaces.presentation.State

class LawCardState(
    val id: Long,
    val title: String = "",
    val subtitle: String = "",
    val date: String = ""
) : State, HasKey<Long> {
    val titleVisible: Boolean = title.isNotBlank()
    val subtitleVisible: Boolean = subtitle.isNotBlank()
    val dateVisible: Boolean = date.isNotBlank()

    override fun getKey(): Long = id
}