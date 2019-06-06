package ru.voxp.android.presentation

import ru.jewelline.mvvm.interfaces.presentation.State

class LawListState(
    val title: String = "",
    val subtitle: String = "",
    val date: String = ""
) : State {
    val titleVisible: Boolean = title.isNotBlank()
    val subtitleVisible: Boolean = subtitle.isNotBlank()
    val dateVisible: Boolean = date.isNotBlank()
}