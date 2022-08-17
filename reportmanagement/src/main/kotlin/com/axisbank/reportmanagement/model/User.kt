package com.axisbank.reportmanagement.model

import javax.persistence.*


@Entity
@Table(name = "user")
class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private var id = 0
    private var username: String? = null
    private var email: String? = null
    private var password: String? = null

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_role",
        joinColumns = [JoinColumn(name = "user_id")],
        inverseJoinColumns = [JoinColumn(name = "role_id")]
    )
    private var roles: Set<Role> = HashSet()


    fun getId():Int=id
    fun setId(i:Int){
        this.id=i
    }
    fun getUsername():String=username!!
    fun setUsername(name:String){
        this.username=name
    }
    fun getEmail():String=email!!
    fun setEmail(mail:String){
        this.email=mail
    }
    fun getPassword():String=password!!
    fun setPassword(pass:String){
        this.password=pass
    }
    fun getRoles()=roles
    fun setRoles(r:HashSet<Role>){
        this.roles=r
    }

    override fun toString(): String {
        return "User(id=$id, username=$username, email=$email, password=$password, roles=$roles)"
    }

    constructor(username:String,email:String,password:String){
        this.username=username
        this.password=password
        this.email=email
    }


}