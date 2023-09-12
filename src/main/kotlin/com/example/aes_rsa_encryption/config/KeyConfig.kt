package com.example.aes_rsa_encryption.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties
data class KeyConfig(
    var privateKeyPath: String = "",
    var publicKeyPath: String = "",
)
