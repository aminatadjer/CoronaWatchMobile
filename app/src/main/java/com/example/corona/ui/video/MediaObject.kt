package com.example.corona.ui.video

class MediaObject {
    var title: String? = null
    var media_url: String? = null
    var thumbnail: String? = null
    var description: String? = null

    constructor(
        title: String?,
        media_url: String?,
        thumbnail: String?,
        description: String?
    ) {
        this.title = title
        this.media_url = media_url
        this.thumbnail = thumbnail
        this.description = description
    }

    constructor() {}

}
