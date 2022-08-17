package com.axisbank.reportmanagement.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class ReportModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private var id = 0

    private var username: String? = null
    private var email: String? = null
    private var downloadedItems: String? = null
    private var timestamp: String? = null

    fun getUsername(): String? {
        return username
    }

    fun setUsername(username: String?) {
        this.username = username
    }

    fun getEmail(): String? {
        return email
    }

    fun setEmail(email: String?) {
        this.email = email
    }

    fun getDownloadedItems(): String? {
        return downloadedItems
    }

    fun setDownloadedItems(downloadedItems: String?) {
        this.downloadedItems = downloadedItems
    }

    fun getTimestamp(): String? {
        return timestamp
    }

    fun setTimestamp(timestamp: String?) {
        this.timestamp = timestamp
    }

    constructor(username: String?, email: String?, downloadedItems: String?, timestamp: String?) {

        this.username = username
        this.email = email
        this.downloadedItems = downloadedItems
        this.timestamp = timestamp
    }

    constructor() {

    }

    fun getId(): Int {
        return id
    }

    fun setId(id: Int) {
        this.id = id
    }

    constructor(id: Int, username: String?, email: String?, downloadedItems: String?, timestamp: String?) {
        this.id = id
        this.username = username
        this.email = email
        this.downloadedItems = downloadedItems
        this.timestamp = timestamp
    }
}