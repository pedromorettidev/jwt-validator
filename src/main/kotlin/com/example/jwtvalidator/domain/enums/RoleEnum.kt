package com.example.jwtvalidator.domain.enums

enum class RoleEnum {
    Admin,
    Member,
    External;

    companion object {
        fun from(role: String): RoleEnum? {
            return entries.firstOrNull {
                it.name == role
            }
        }
    }
}
