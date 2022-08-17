package com.axisbank.reportmanagement.controller

class MessageResponse {
    var message: String? = null

    constructor(message: String?) : super() {
        this.message = message
    }

    constructor() : super() {}
}