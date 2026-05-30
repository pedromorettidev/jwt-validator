package com.example.jwtvalidator.application.decoder

import com.auth0.jwt.JWT
import com.example.jwtvalidator.domain.TokenClaims
import com.example.jwtvalidator.domain.enums.RoleEnum
import org.springframework.stereotype.Component

@Component
class JwtDecoderImpl: JwtDecoder {
    override fun decode(token: String): TokenClaims {
        val decodedJwt = JWT.decode(token)
        val claims = decodedJwt.claims

        return TokenClaims(
            name = claims["Name"]?.asString(),
            role = claims["Role"]?.asString()?.let { RoleEnum.from(it) },
            seed = claims["Seed"]?.asString()?.toLongOrNull(),
            claimsCount = claims.size
        )
    }
}
