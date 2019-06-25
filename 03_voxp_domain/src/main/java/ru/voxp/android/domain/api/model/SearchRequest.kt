package ru.voxp.android.domain.api.model

class SearchRequest {
    val lawType: String? = null
    val status: List<Int>? = null
    val name: String? = null
    val number: String? = null
    val registrationStart: String? = null
    val registrationEnd: String? = null
    val documentNumber: String? = null

    val topic: String? = null
    val lawClass: String? = null
    val federalSubject: String? = null
    val regionalSubject: String? = null
    val deputy: String? = null
    val responsibleCommittee: String? = null
    val soExecutorCommittee: String? = null
    val profileCommittee: String? = null

    val searchMode: Int? = null
    val eventStart: String? = null
    val eventEnd: String? = null
    val instance: String? = null
    val stage: String? = null
    val phase: String? = null

    val page: Int? = null
    val sort: String? = null
    val limit: Int? = null
}
