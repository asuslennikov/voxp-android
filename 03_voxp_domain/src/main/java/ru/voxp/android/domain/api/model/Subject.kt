package ru.voxp.android.domain.api.model

import ru.voxp.android.domain.api.model.Committee.Type

class Subject {
    var deputies: List<Deputy>? = null
    var departments: List<Type>? = null
}
