package com.example.aes_rsa_encryption.service

import com.example.aes_rsa_encryption.dto.EncryptedDto
import com.example.aes_rsa_encryption.config.KeyConfig
import com.example.aes_rsa_encryption.util.AESUtil
import com.example.aes_rsa_encryption.util.PasswordUtil
import com.example.aes_rsa_encryption.util.RSAUtil
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Service

@Service
class EncryptionService(
    private val objectMapper: ObjectMapper,
    private val keyConfig: KeyConfig,
) {

    fun encrypt(data: Any): EncryptedDto {
        val password = PasswordUtil.random()
        val salt = PasswordUtil.random()
        val iv = PasswordUtil.random(16)
        val encrypted = AESUtil(password, salt, iv).encrypt(objectMapper.writeValueAsString(data))

        val rsaUtil = RSAUtil(null, keyConfig.publicKeyPath)
        return EncryptedDto(
            rsaUtil.encrypt(password),
            rsaUtil.encrypt(salt),
            rsaUtil.encrypt(iv),
            encrypted
        )
    }

    fun decrypt(dto: EncryptedDto): String {
        val rsaUtil = RSAUtil(keyConfig.privateKeyPath, null)
        val password = rsaUtil.decrypt(dto.password)
        val salt = rsaUtil.decrypt(dto.salt)
        val iv = rsaUtil.decrypt(dto.iv)
        return AESUtil(password, salt, iv).decrypt(dto.encrypted)
    }
}
