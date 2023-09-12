package com.example.aes_rsa_encryption.util

import java.nio.charset.StandardCharsets
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

class AESUtil private constructor(
    private var password: String,
    private var salt: ByteArray,
    private var iv: ByteArray,
) {
    companion object {
        private const val ITERATION_COUNT = 100
        private const val KEY_LENGTH = 256
        private const val KEY_ALGORITHM = "PBKDF2WithHmacSHA1"
        private const val CIPHER_PADDING = "AES/CBC/PKCS5Padding"
        private const val SECRET_KEY_ALGORITHM = "AES"

        operator fun invoke(password: String, salt: String, iv: String): AESUtil {
            return AESUtil(
                password,
                salt.toByteArray(),
                iv.toByteArray(),
            )
        }
    }

    fun encrypt(data: String): String {
        val secretKeySpec = getSecretKeySpec()
        val cipher = Cipher.getInstance(CIPHER_PADDING)
        val ivSpec = IvParameterSpec(iv)
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivSpec)
        val encrypted = cipher.doFinal(data.toByteArray(StandardCharsets.UTF_8))
        return Base64.getEncoder().encodeToString(encrypted)
    }

    fun decrypt(data: String): String {
        val secretKeySpec = getSecretKeySpec()
        val cipher = Cipher.getInstance(CIPHER_PADDING)
        val ivSpec = IvParameterSpec(iv)
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivSpec)
        val decrypted = cipher.doFinal(Base64.getDecoder().decode(data))
        return String(decrypted, StandardCharsets.UTF_8)
    }

    private fun getSecretKeySpec(): SecretKeySpec {
        val spec = PBEKeySpec(password.toCharArray(), salt, ITERATION_COUNT, KEY_LENGTH)
        val factory = SecretKeyFactory.getInstance(KEY_ALGORITHM)
        val secret = factory.generateSecret(spec)
        return SecretKeySpec(secret.encoded, SECRET_KEY_ALGORITHM)
    }
}
