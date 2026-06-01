package com.example.jwtvalidator.application.decoder

import com.auth0.jwt.exceptions.JWTDecodeException
import com.example.jwtvalidator.domain.enums.RoleEnum
import com.example.jwtvalidator.utils.TokenClaimsBuilder
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

class JwtDecoderImplTest {

    private lateinit var jwtDecoder: JwtDecoder

    @BeforeEach
    fun beforeEach() {
        jwtDecoder = JwtDecoderImpl()
    }

    @Test
    fun `should decode jwt token succesfully and build tokenClaims`() {
        val token = "eyJhbGciOiJIUzI1NiJ9.eyJSb2xlIjoiQWRtaW4iLCJTZWVkIjoiNzg0MSIsIk5hbWUiOiJUb25pbmhvIEFyYXVqbyJ9.QY05sIjtrcJnP533kQNk8QXcaleJ1Q01jWY_ZzIZuAg"
        val result = jwtDecoder.decode(token)
        val expectedResult = TokenClaimsBuilder.getTokenClaims(role = RoleEnum.Admin, name = "Toninho Araujo", seed = 7841L)
        assertEquals(expectedResult, result)
    }

    @Test
    fun `should try to decode jwt token and throw exception when jwt token is invalid`() {
        val token = "eyJhbGciOiJzI1NiJ9.dfsdfsfryJSr2xrIjoiQWRtaW4iLCJTZrkIjoiNzg0MSIsIk5hbrUiOiJUb25pbmhvIEFyYXVqbyJ9.QY05fsdfsIjtrcJnP533kQNk8QXcaleJ1Q01jWY_ZzIZuAg"
        assertThrows<JWTDecodeException> { jwtDecoder.decode(token) }
    }

    @Test
    fun `should decode jwt token with more claims that expected and build tokenClaims`() {
        val token = "eyJhbGciOiJIUzI1NiJ9.eyJSb2xlIjoiTWVtYmVyIiwiT3JnIjoiQlIiLCJTZWVkIjoiMTQ2MjciLCJOYW1lIjoiVmFsZGlyIEFyYW5oYSJ9.cmrXV_Flm5mfdpfNUVopY_I2zeJUy4EZ4i3Fea98zvY"
        val result = jwtDecoder.decode(token)
        assertEquals(
            TokenClaimsBuilder.getTokenClaims(name = "Valdir Aranha", role = RoleEnum.Member, seed = 14627L, claimsCount = 4),
            result
        )
    }

    @Test
    fun `should decode jwt token and build tokenClaims when decoded role is invalid to domain`() {
        val token = "eyJhbGciOiJIUzI1NiJ9.eyJSb2xlIjoiU3VwZXJBZG1pbiIsIlNlZWQiOiI3ODQxIiwiTmFtZSI6IlRvbmluaG8gQXJhdWpvIn0.invalid-signature"
        val result = jwtDecoder.decode(token)
        assertEquals(
            TokenClaimsBuilder.getTokenClaims(name = "Toninho Araujo", seed = 7841L, role = null),
            result
        )
    }

    @Test
    fun `should decode jwt token and build tokenClaims when decoded jwt does not has name claim`() {
        val token = "eyJhbGciOiJIUzI1NiJ9.eyJSb2xlIjoiQWRtaW4iLCJTZWVkIjoiNzg0MSJ9.invalid-signature"
        val result = jwtDecoder.decode(token)
        assertEquals(
            TokenClaimsBuilder.getTokenClaims(name = null, seed = 7841L, role = RoleEnum.Admin, claimsCount = 2),
            result
        )
    }

    @Test
    fun `should decode jwt token and build tokenClaims when decoded jwt does not has role claim`() {
        val token = "eyJhbGciOiJIUzI1NiJ9.eyJTZWVkIjoiNzg0MSIsIk5hbWUiOiJUb25pbmhvIEFyYXVqbyJ9.invalid-signature"
        val result = jwtDecoder.decode(token)
        assertEquals(
            TokenClaimsBuilder.getTokenClaims(name = "Toninho Araujo", seed = 7841L, role = null, claimsCount = 2),
            result
        )
    }

    @Test
    fun `should decode jwt token and build tokenClaims when decoded jwt does not has seed claim`() {
        val token = "eyJhbGciOiJIUzI1NiJ9.eyJSb2xlIjoiQWRtaW4iLCJOYW1lIjoiVG9uaW5obyBBcmF1am8ifQ.invalid-signature"
        val result = jwtDecoder.decode(token)
        assertEquals(
            TokenClaimsBuilder.getTokenClaims(name = "Toninho Araujo", seed = null, role = RoleEnum.Admin, claimsCount = 2),
            result
        )
    }
}
