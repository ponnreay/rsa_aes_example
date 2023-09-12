package com.example.aes_rsa_encryption.controller

import com.example.aes_rsa_encryption.dto.EncryptedDto
import com.example.aes_rsa_encryption.service.EncryptionService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class HomeController(
    private val encryptionService: EncryptionService,
) {

    @PostMapping("/encrypt")
    fun encryptData(@RequestBody data: Any): EncryptedDto {
        return encryptionService.encrypt(data)
    }

    @PostMapping("/decrypt")
    fun decryptData(@RequestBody data: EncryptedDto): Any {
        return encryptionService.decrypt(data)
    }
}
