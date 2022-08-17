package com.axisbank.reportmanagement.repository


import com.axisbank.reportmanagement.model.ERole
import com.axisbank.reportmanagement.model.Role
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface RoleRepository : JpaRepository<Role?, Int?> {
    fun findByName(name: ERole?): Optional<Role?>?
}