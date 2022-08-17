package com.axisbank.reportmanagement.controller


import com.axisbank.reportmanagement.model.ERole
import com.axisbank.reportmanagement.model.Role
import com.axisbank.reportmanagement.model.User
import com.axisbank.reportmanagement.repository.RoleRepository
import com.axisbank.reportmanagement.repository.UserRepository
import com.axisbank.reportmanagement.security.jwt.JwtUtils
import com.axisbank.reportmanagement.servcie.UserDetailsImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*
import java.util.function.Consumer
import java.util.function.Supplier
import java.util.stream.Collectors

//@CrossOrigin(origins = "*", maxAge = 3600)
@CrossOrigin("http://localhost:3000")
@RestController
@RequestMapping("/api/auth")
class AuthController {
    @Autowired
    var authenticationManager: AuthenticationManager? = null

    @Autowired
    var userRepository: UserRepository? = null

    @Autowired
    var roleRepository: RoleRepository? = null

    @Autowired
    var encoder: PasswordEncoder? = null

    @Autowired
    var jwtUtils: JwtUtils? = null
    @PostMapping("/signin")
    fun authenticateUser(@RequestBody loginRequest: LoginRequest): ResponseEntity<*> {
        val authentication = authenticationManager!!.authenticate(
            UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        )
        SecurityContextHolder.getContext().authentication = authentication
        val jwt = jwtUtils!!.generateJwtToken(authentication)
        val userDetails = authentication.principal as UserDetailsImpl
        val roles = userDetails.authorities.stream()
            .map { item: GrantedAuthority -> item.authority }
            .collect(Collectors.toList())
        return ResponseEntity.ok(JwtResponse(jwt, userDetails.id, userDetails.username, userDetails.email, roles))
    }

    @PostMapping("/signup")
    fun registerUser(@RequestBody signUpRequest: SignupRequest): ResponseEntity<*> {
        if (userRepository!!.existsByUsername(signUpRequest.takeUsername())!!) {
            return ResponseEntity
                .badRequest()
                .body(MessageResponse("Error: Username is already taken!"))
        }
        if (userRepository!!.existsByEmail(signUpRequest.takeEmail())!!) {
            return ResponseEntity
                .badRequest()
                .body(MessageResponse("Error: Email is already in use!"))
        }
        // Create new user's account
        val user =
            User(signUpRequest.takeUsername()!!, signUpRequest.takeEmail()!!, encoder!!.encode(signUpRequest.takePassword()))
        val strRoles: HashSet<String> = (signUpRequest.takeRoles() as HashSet<String>?)!!
        val roles: HashSet<Role> = HashSet()
        if (strRoles == null) {
            val userRole: Role? = roleRepository!!.findByName(ERole.ROLE_USER)
                ?.orElseThrow(Supplier { RuntimeException("Error: Role is not found.") })
            roles.add(userRole!!)
        } else {
            strRoles.forEach(Consumer { role: String? ->
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
                        val userRole = roleRepository!!.findByName(ERole.ROLE_USER)!!.get()
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
        }
        user.setRoles(roles)
        userRepository!!.save<User>(user)
        return ResponseEntity.ok(MessageResponse("User registered successfully!"))
    }
}