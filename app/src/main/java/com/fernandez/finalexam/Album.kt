package com.fernandez.finalexam

/**
 * Created by Acer on 11/02/2017.
 */

class Album {
    var name: String? = null
    var artist: String? = null
    var url: String? = null
    var image: String? = null

    constructor() {}

    constructor(name: String, artist: String, url: String, image: String) {
        this.name = name
        this.artist = artist
        this.url = url
        this.image = image
    }

    fun getImage() {}


}
