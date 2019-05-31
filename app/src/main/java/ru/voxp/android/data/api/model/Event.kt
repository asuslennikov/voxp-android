package ru.voxp.android.data.api.model

import com.fasterxml.jackson.annotation.JsonFormat
import java.util.*

class Event {
    var stage: Stage? = null
    var phase: Phase? = null
    var solution: String? = null
    @JsonFormat(pattern = "yyyy-MM-dd")
    var date: Date? = null
}
