package com.axisbank.reportmanagement.controller

import com.axisbank.reportmanagement.model.Role
import com.axisbank.reportmanagement.repository.RoleRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController


@RestController
class RoleController {
    @Autowired
    private val roleRepository: RoleRepository? = null
    @PostMapping("/role/add")
    fun addRole(@RequestBody role: Role): String {
        roleRepository!!.save<Role>(role)
        return "role added successfully"
    }

    @GetMapping("/role/list")
    fun allRole(): List<Role?> {
        return roleRepository!!.findAll()
    }
}
