package com.axisbank.reportmanagement.controller

class LoginRequest {
    private var username: String? = null

    private var password: String? = null

    fun getUsername(): String? {
        return username
    }

    fun setUsername(username: String?) {
        this.username = username
    }

    fun getPassword(): String? {
        return password
    }

    fun setPassword(password: String?) {
        this.password = password
    }

    constructor(username: String?, password: String?) {

        this.username = username
        this.password = password
    }

    constructor() {

    }
}