package ru.voxp.android.domain.api.model

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

class Committee {
    var responsible: Type? = null
    var profile: List<Type>? = null
    var soexecutor: List<Type>? = null

    class Type {
        var id: Long? = null
        var name: String? = null
        @JsonProperty("isCurrent")
        var isCurrent: String? = null
        @JsonFormat(pattern = "yyyy-MM-dd")
        var startDate: Date? = null
        @JsonFormat(pattern = "yyyy-MM-dd")
        var endDate: Date? = null
    }
}
