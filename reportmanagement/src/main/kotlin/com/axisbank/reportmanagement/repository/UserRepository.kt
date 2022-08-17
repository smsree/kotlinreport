package com.axisbank.reportmanagement.repository



import com.axisbank.reportmanagement.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository : JpaRepository<User?, Int?> {
	//email as user name
	fun findByUsername(username: String?): Optional<User?>?
	fun existsByUsername(username: String?): Boolean?
	fun existsByEmail(email: String?): Boolean?
	fun deleteByUsername(username: String?)
}
