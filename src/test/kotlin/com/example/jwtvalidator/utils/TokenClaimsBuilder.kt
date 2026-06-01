package com.example.jwtvalidator.utils

import com.example.jwtvalidator.domain.TokenClaims
import com.example.jwtvalidator.domain.enums.RoleEnum

object TokenClaimsBuilder {
    fun getTokenClaims(
        name: String? = "name",
        role: RoleEnum? = RoleEnum.Member,
        seed: Long? = 11L,
        claimsCount: Int = 3
    ) =
        TokenClaims(
            name = name,
            role = role,
            seed = seed,
            claimsCount = claimsCount
        )
}
