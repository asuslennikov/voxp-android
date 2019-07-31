package ru.voxp.android.domain.api

enum class ExceptionType {
    CLIENT,
    CONNECTION,
    SERVER,
    NO_CONNECTION_AVAILABLE
}

class VoxpException(val exceptionType: ExceptionType, message: String, originalCause: Throwable?) :
    RuntimeException(message, originalCause) {

    constructor(message: String) : this(ExceptionType.CLIENT, message, null)

    constructor(exceptionType: ExceptionType, message: String) : this(exceptionType, message, null)
}