package com.axisbank.reportmanagement.amazonconfig

class ResponseMessage {
    private var message: String? = null

    constructor(message: String?) {
        this.message = message
    }

    fun getMessage(): String? {
        return message
    }

    fun setMessage(message: String?) {
        this.message = message
    }
}