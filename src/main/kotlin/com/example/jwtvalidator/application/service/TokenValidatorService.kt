package com.example.jwtvalidator.application.service

import com.example.jwtvalidator.application.decoder.JwtDecoder
import com.example.jwtvalidator.domain.TokenClaims
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import kotlin.math.sqrt

@Service
class TokenValidatorService(
    private val jwtDecoder: JwtDecoder
) {
    fun validateJwtToken(jwt: String): Boolean {
        logger.info("$LOG_PREFIX: Validating token claims.")
        val claimsData = runCatching {
            jwtDecoder.decode(jwt)
        }.getOrElse {
            logger.warn("$LOG_PREFIX: Error when trying to decode JWT. Message: ${it.message}")
            return false
        }

        logger.info("$LOG_PREFIX: Decoded jwt succesfully with claims count: ${claimsData.claimsCount}")

        if (claimsData.claimsCount != REQUIRED_CLAIMS_COUNT) {
            logger.info("$LOG_PREFIX: Claims count is not $REQUIRED_CLAIMS_COUNT. Invalid JWT Token.")
            return false
        }

        if(!hasAllRequiredClaims(claimsData)) {
            logger.info("$LOG_PREFIX: Don't have all required claims. Invalid JWT Token.")
            return false
        }

        if (!isValidName(claimsData.name!!)) {
            logger.info("$LOG_PREFIX: Name field ${claimsData.name} is invalid. Invalid JWT Token.")
            return false
        }

        if (!isPrime(claimsData.seed!!)) {
            logger.info("$LOG_PREFIX: Seed number is not a prime number. Invalid JWT Token.")
            return false
        }

        logger.info("$LOG_PREFIX: Token succesfully validated")
        return true
    }

    private fun isPrime(number: Long): Boolean {
        if (number <= 1) return false
        if (number == EVEN_PRIME_NUMBER) return true
        if (number % DIVISOR_STEP == 0L) return false

        val limit = sqrt(number.toDouble()).toInt()

        for (i in FIRST_ODD_DIVISOR..limit step DIVISOR_STEP) {
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

        const val EVEN_PRIME_NUMBER = 2L
        const val FIRST_ODD_DIVISOR = 3
        const val DIVISOR_STEP = 2

        private val logger = LoggerFactory.getLogger(TokenValidatorService::class.java)
        val LOG_PREFIX = TokenValidatorService::class.simpleName
    }
}
