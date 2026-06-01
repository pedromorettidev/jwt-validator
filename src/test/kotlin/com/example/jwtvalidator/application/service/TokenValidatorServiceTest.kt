package com.example.jwtvalidator.application.service

import com.example.jwtvalidator.application.decoder.JwtDecoder
import com.example.jwtvalidator.utils.TokenClaimsBuilder
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertTrue

class TokenValidatorServiceTest {
    private lateinit var jwtDecoder: JwtDecoder
    private lateinit var tokenValidatorService: TokenValidatorService

    @BeforeEach
    fun beforeEach() {
        jwtDecoder = mockk()
        tokenValidatorService = TokenValidatorService(jwtDecoder)
    }

    @Test
    fun `should return true when token is valid`() {
        every { jwtDecoder.decode(any()) } returns TokenClaimsBuilder.getTokenClaims()

        val result = tokenValidatorService.validateJwtToken("any")

        assertTrue(result)
    }

    @Test
    fun `should return false when name contains digits`() {
        every { jwtDecoder.decode(any()) } returns
                TokenClaimsBuilder.getTokenClaims(name = "1name")

        val result = tokenValidatorService.validateJwtToken("any")

        assertFalse(result)
    }

    @Test
    fun `should return false when name exceeds max length`() {
        every { jwtDecoder.decode(any()) } returns
                TokenClaimsBuilder.getTokenClaims(
                    name = "a".repeat(257)
                )

        val result = tokenValidatorService.validateJwtToken("any")

        assertFalse(result)
    }

    @Test
    fun `should return true when name has exactly max length`() {
        every { jwtDecoder.decode(any()) } returns
                TokenClaimsBuilder.getTokenClaims(
                    name = "a".repeat(256)
                )

        val result = tokenValidatorService.validateJwtToken("any")

        assertTrue(result)
    }

    @Test
    fun `should return false when seed is not prime`() {
        every { jwtDecoder.decode(any()) } returns
                TokenClaimsBuilder.getTokenClaims(
                    seed = 4L
                )

        val result = tokenValidatorService.validateJwtToken("any")

        assertFalse(result)
    }

    @Test
    fun `should return false when claims count is less than required`() {
        every { jwtDecoder.decode(any()) } returns
                TokenClaimsBuilder.getTokenClaims(
                    claimsCount = 2
                )

        val result = tokenValidatorService.validateJwtToken("any")

        assertFalse(result)
    }

    @Test
    fun `should return false when claims count is greater than required`() {
        every { jwtDecoder.decode(any()) } returns
                TokenClaimsBuilder.getTokenClaims(
                    claimsCount = 4
                )

        val result = tokenValidatorService.validateJwtToken("any")

        assertFalse(result)
    }

    @Test
    fun `should return false when required claim is null`() {
        every { jwtDecoder.decode(any()) } returns
                TokenClaimsBuilder.getTokenClaims(
                    seed = null
                )

        val result = tokenValidatorService.validateJwtToken("any")

        assertFalse(result)
    }

    @Test
    fun `should return false when name is null`() {
        every { jwtDecoder.decode(any()) } returns
                TokenClaimsBuilder.getTokenClaims(
                    name = null
                )

        val result = tokenValidatorService.validateJwtToken("any")

        assertFalse(result)
    }

    @Test
    fun `should return false when role is null`() {
        every { jwtDecoder.decode(any()) } returns
                TokenClaimsBuilder.getTokenClaims(
                    role = null
                )

        val result = tokenValidatorService.validateJwtToken("any")

        assertFalse(result)
    }

    @Test
    fun `should return false when decoder throws exception`() {
        every {
            jwtDecoder.decode(any())
        } throws RuntimeException()

        val result = tokenValidatorService.validateJwtToken("any")

        assertFalse(result)
    }
}
