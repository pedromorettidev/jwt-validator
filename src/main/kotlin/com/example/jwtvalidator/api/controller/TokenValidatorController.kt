package com.example.jwtvalidator.api.controller

import com.example.jwtvalidator.application.service.TokenValidatorService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/jwt")
class TokenValidatorController(
    private val tokenValidatorService: TokenValidatorService
) {
    @PostMapping("/validate")
    fun validateJwt(
        @RequestParam jwt: String
    ): ResponseEntity<Boolean> {
        return ResponseEntity.status(HttpStatus.OK).body(tokenValidatorService.validateJwtToken(jwt))
    }
}
