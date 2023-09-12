package com.example.aes_rsa_encryption.dto

data class EncryptedDto(
    val password: String,
    val salt: String,
    val iv: String,
    val encrypted: String,
)
