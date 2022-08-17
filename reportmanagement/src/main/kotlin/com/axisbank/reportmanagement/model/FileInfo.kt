package com.axisbank.reportmanagement.model

class FileInfo {
    private var name: String? = null
    private var url: String? = null
    fun getName(): String? {
        return name
    }

    fun setName(name: String?) {
        this.name = name
    }

    fun getUrl(): String? {
        return url
    }

    fun setUrl(url: String?) {
        this.url = url
    }

    constructor(name: String?, url: String?) {

        this.name = name
        this.url = url
    }
    constructor() {

    }
}