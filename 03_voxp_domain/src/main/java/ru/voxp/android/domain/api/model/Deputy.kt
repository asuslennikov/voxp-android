package ru.voxp.android.domain.api.model

import com.fasterxml.jackson.annotation.JsonProperty

class Deputy {
    var id: Long? = null
    var name: String? = null
    var position: String? = null
    @JsonProperty("isCurrent")
    var isCurrent: String? = null
}
