package com.axisbank.reportmanagement.model

import javax.persistence.*

@Entity
@Table(name = "role")
class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private var id = 0

    @Enumerated(EnumType.STRING)
    private var name: ERole? = null

    fun getId():Int=id
    fun setId(i:Int){
        this.id=i
    }
    fun getName():ERole=name!!
    fun setName(nam:ERole){
        this.name=nam
    }

    override fun toString(): String {
        return "Role(id=$id, name=$name)"
    }

    constructor(){}


}