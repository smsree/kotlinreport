package com.axisbank.reportmanagement.controller

import com.axisbank.reportmanagement.model.ERole
import com.axisbank.reportmanagement.model.Role
import com.axisbank.reportmanagement.model.User
import com.axisbank.reportmanagement.repository.RoleRepository
import com.axisbank.reportmanagement.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.util.function.Consumer

@CrossOrigin("http://localhost:3000")
@RestController
@RequestMapping("/api/test")
class TestController {
    @Autowired
    private val userRepository: UserRepository? = null

    @Autowired
    private val roleRepository: RoleRepository? = null
    @GetMapping("/all")
    fun allAccess(): String {
        return "Public Content."
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER') or hasRole('AUDIT') or hasRole('ADMIN') or hasRole('OPERATIONAL')")
    fun userAccess(): String {
        return "User Content."
    }

    @GetMapping("/operational")
    @PreAuthorize("hasRole('OPERATIONAL')")
    fun moderatorAccess(): String {
        return "OPERATIONAL Board."
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    fun adminAccess(): String {
        return "Admin Board."
    }

    @GetMapping("/audit")
    @PreAuthorize("hasRole('AUDIT')")
    fun auditAccess(): String {
        return "Audit board"
    }

    @PutMapping("/updateOtherUserRole")
    @PreAuthorize("hasRole('ADMIN')")
    fun updateOtherUserRole(@RequestBody request: SignupRequest): ResponseEntity<*> {
        println("hit>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>")
        if (userRepository!!.existsByUsername(request.username)!!) {
            val updatedUser: User = userRepository.findByUsername(request.username)!!.get()
            val strRoles = request.roles
            val roles: HashSet<Role> = HashSet()
            strRoles!!.forEach(Consumer { role: String? ->
                when (role) {
                    "admin" -> {
                        val adminRole = roleRepository!!.findByName(ERole.ROLE_ADMIN)!!.get()
                            ?: throw RuntimeException("Error: Role is not found.")
                        roles.add(adminRole)
                    }
                    "audit" -> {
                        val auditRole = roleRepository!!.findByName(ERole.ROLE_AUDIT)!!.get()
                            ?: throw RuntimeException("Error: Role is not found")
                        roles.add(auditRole)
                    }
                    "operational" -> {
                        val operationRole = roleRepository!!.findByName(ERole.ROLE_OPERATIONAL)!!.get()
                            ?: throw RuntimeException("Error : Role not found")
                        roles.add(operationRole)
                        val userRole = roleRepository.findByName(ERole.ROLE_USER)!!.get()
                            ?: throw RuntimeException("Error:  role not found")
                        roles.add(userRole)
                    }
                    else -> {
                        val userRole = roleRepository!!.findByName(ERole.ROLE_USER)!!.get()
                            ?: throw RuntimeException("Error:  role not found")
                        roles.add(userRole)
                    }
                }
            })
            updatedUser.setRoles(roles)
            userRepository.save<User>(updatedUser)
        } else {
            return ResponseEntity
                .badRequest()
                .body(MessageResponse("Error: Username not found for updation!"))
        }
        return ResponseEntity.ok(MessageResponse("User updation successfully!"))
    }

    @GetMapping("/showAllUser")
    @PreAuthorize("hasRole('ADMIN')")
    fun showAllUser(): List<User?> {
        return userRepository!!.findAll()
    }
}