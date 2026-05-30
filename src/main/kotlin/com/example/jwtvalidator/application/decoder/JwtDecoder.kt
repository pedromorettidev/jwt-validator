package com.example.jwtvalidator.application.decoder

import com.example.jwtvalidator.domain.TokenClaims

interface JwtDecoder {
    fun decode(jwt: String): TokenClaims
}
