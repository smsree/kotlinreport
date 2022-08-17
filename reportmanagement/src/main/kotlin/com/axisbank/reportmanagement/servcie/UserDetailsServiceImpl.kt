package com.axisbank.reportmanagement.servcie


import com.axisbank.reportmanagement.model.User
import com.axisbank.reportmanagement.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class UserDetailsServiceImpl : UserDetailsService {
    @Autowired
    private val userRepository: UserRepository? = null

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String): UserDetails {
        val user: User? = userRepository!!.findByUsername(username)!!.orElseThrow {
            UsernameNotFoundException(
                "Username not found"
            )
        }
        return UserDetailsImpl.build(user!!)
    }
}
