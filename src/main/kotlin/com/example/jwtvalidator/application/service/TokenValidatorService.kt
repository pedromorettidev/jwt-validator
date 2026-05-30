package com.example.jwtvalidator.application.service

import com.example.jwtvalidator.application.decoder.JwtDecoder
import com.example.jwtvalidator.domain.TokenClaims
import org.springframework.stereotype.Service
import kotlin.math.sqrt

@Service
class TokenValidatorService(
    private val jwtDecoder: JwtDecoder
) {
    fun validateJwtToken(jwt: String): Boolean {
        val claimsData = runCatching {
            jwtDecoder.decode(jwt)
        }.getOrElse {
            return false
        }

        if (claimsData.claimsCount != REQUIRED_CLAIMS_COUNT) return false

        if(!hasAllRequiredClaims(claimsData)) return false

        if (!isValidName(claimsData.name!!)) return false

        if (!isPrime(claimsData.seed!!)) return false

        return true
    }

    private fun isPrime(number: Long): Boolean {
        if (number <= 1) return false
        if (number == 2L) return true
        if (number % 2 == 0L) return false

        val limit = sqrt(number.toDouble()).toInt()
        for (i in 3..limit step 2) {
            if (number % i == 0L) return false
        }
        return true
    }

    private fun hasAllRequiredClaims(claims: TokenClaims): Boolean {
        return claims.name != null &&
                claims.role != null &&
                claims.seed != null
    }

    private fun isValidName(name: String): Boolean {
        return name.length <= MAX_NAME_LENGTH &&
                name.none { it.isDigit() }
    }

    private companion object {
        const val REQUIRED_CLAIMS_COUNT = 3
        const val MAX_NAME_LENGTH = 256
    }
}
