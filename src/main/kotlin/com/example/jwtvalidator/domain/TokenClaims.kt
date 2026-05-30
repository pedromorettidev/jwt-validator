package com.example.jwtvalidator.domain

import com.example.jwtvalidator.domain.enums.RoleEnum

data class TokenClaims(
    val name: String?,
    val role: RoleEnum?,
    val seed: Long?,
    val claimsCount: Int
)
