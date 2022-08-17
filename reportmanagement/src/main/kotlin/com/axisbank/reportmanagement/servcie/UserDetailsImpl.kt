package com.axisbank.reportmanagement.servcie


import com.axisbank.reportmanagement.model.User
import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.stream.Collectors


class UserDetailsImpl(
    var id: Int, private val username: String, @field:JsonIgnore private val password: String, var email: String,
    private val authorities: Collection<GrantedAuthority>
) : UserDetails {

    override fun getAuthorities(): Collection<GrantedAuthority> {
        // TODO Auto-generated method stub
        return authorities
    }

    override fun getPassword(): String {
        // TODO Auto-generated method stub
        return password
    }

    override fun getUsername(): String {
        // TODO Auto-generated method stub
        return username
    }

    override fun isAccountNonExpired(): Boolean {
        // TODO Auto-generated method stub
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        // TODO Auto-generated method stub
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        // TODO Auto-generated method stub
        return true
    }

    override fun isEnabled(): Boolean {
        // TODO Auto-generated method stub
        return true
    }

    override fun equals(obj: Any?): Boolean {
        if (this === obj) {
            return true
        }
        if (obj == null || javaClass != obj.javaClass) {
            return false
        }
        val user = obj as UserDetailsImpl
        return id == user.id
    }

    companion object {
        private const val serialVersionUID = 1L
        fun build(user: User): UserDetailsImpl {
            val authorities: List<GrantedAuthority> = user.getRoles().stream().map { role ->
                SimpleGrantedAuthority(
                    role.getName().name
                )
            }.collect(Collectors.toList())
            return UserDetailsImpl(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.getEmail(),
                authorities
            )
        }
    }
}