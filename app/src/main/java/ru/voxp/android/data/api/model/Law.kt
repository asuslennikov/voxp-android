package ru.voxp.android.data.api.model

class Law {
    var id: Long? = null
    var number: String? = null
    var name: String? = null
    var comments: String? = null
    var introductionDate: String? = null
    var url: String? = null
    var documents: List<Document>? = null
    var transcriptUrl: String? = null
    var lastEvent: Event? = null
    var subject: Subject? = null
}
