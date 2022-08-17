package com.axisbank.reportmanagement.controller


class SignupRequest {
    var username: String? = null
    var email: String? = null
    var password: String? = null
    var roles: Set<String>? = null

    fun takeUsername():String=username!!
    fun putUsername(nam:String){
        this.username=nam
    }
    fun takeEmail():String=email!!
    fun putEmail(mail:String){
        this.email=mail
    }
    fun takePassword():String=password!!
    fun putPassword(pass:String){
        this.password=pass
    }
    fun takeRoles()=roles
    fun putRoles(r:HashSet<String>){
        this.roles=r
    }

    constructor(username: String?, email: String?, password: String?, roles: Set<String>?) : super() {
        this.username = username
        this.email = email
        this.password = password
        this.roles = roles
    }

    constructor(username: String?, email: String?, password: String?) : super() {
        this.username = username
        this.email = email
        this.password = password
    }

    constructor() : super() {        // TODO Auto-generated constructor stub
    }
}