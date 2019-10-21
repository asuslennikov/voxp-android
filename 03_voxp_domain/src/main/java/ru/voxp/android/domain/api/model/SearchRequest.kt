package ru.voxp.android.domain.api.model

class SearchRequest {
    var lawType: String? = null
    var status: List<Int>? = null
    var name: String? = null
    var number: String? = null
    var registrationStart: String? = null
    var registrationEnd: String? = null
    var documentNumber: String? = null

    var topic: String? = null
    var lawClass: String? = null
    var federalSubject: String? = null
    var regionalSubject: String? = null
    var deputy: String? = null
    var responsibleCommittee: String? = null
    var soExecutorCommittee: String? = null
    var profileCommittee: String? = null

    var searchMode: Int? = null
    var eventStart: String? = null
    var eventEnd: String? = null
    var instance: String? = null
    var stage: String? = null
    var phase: String? = null

    var page: Int? = null
    var sort: String? = null
    var limit: Int? = null
}
