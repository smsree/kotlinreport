package com.axisbank.reportmanagement.security.jwt


import com.axisbank.reportmanagement.servcie.UserDetailsImpl
import io.jsonwebtoken.*
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import java.util.*


@Component
class JwtUtils {
    @Value("\${jwt.secret}")
    private val jwtSecret: String? = null

    @Value("\${jwt.expirationMs}")
    private val jwtExpirationMs = 0
    fun generateJwtToken(authentication: Authentication): String {
        val userPrincipal: UserDetailsImpl = authentication.principal as UserDetailsImpl
        return Jwts.builder().setSubject(userPrincipal.getUsername()).setIssuedAt(Date())
            .setExpiration(Date(Date().time + jwtExpirationMs)).signWith(
                SignatureAlgorithm.HS512, jwtSecret
            ).compact()
    }

    fun getUserNameFromJwtToken(authToken: String?): String {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken).body.subject
    }

    fun validateJwtToken(authtoken: String?): Boolean {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authtoken)
            return true
        } catch (e: SignatureException) {
            logger.error("Invalid JWT signature: {}", e.message)
        } catch (e: MalformedJwtException) {
            logger.error("Invalid JWT token: {}", e.message)
        } catch (e: ExpiredJwtException) {
            logger.error("JWT token is expired: {}", e.message)
        } catch (e: UnsupportedJwtException) {
            logger.error("JWT token is unsupported: {}", e.message)
        } catch (e: IllegalArgumentException) {
            logger.error("JWT claims string is empty: {}", e.message)
        }
        return false
    }

    companion object {
        private val logger = LoggerFactory.getLogger(JwtUtils::class.java)
    }
}