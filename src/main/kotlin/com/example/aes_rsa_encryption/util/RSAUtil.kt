package com.example.aes_rsa_encryption.util

import java.lang.Exception
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths
import java.security.KeyFactory
import java.security.PrivateKey
import java.security.PublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.Base64
import javax.crypto.Cipher

class RSAUtil private constructor(
    private var privateKey: PrivateKey? = null,
    private var publicKey: PublicKey? = null,
) {
    private val cipher: Cipher = Cipher.getInstance(CIPHER_PADDING)

    companion object {
        private const val CIPHER_PADDING = "RSA/ECB/PKCS1Padding"
        private const val KEY_ALGORITHM = "RSA"
        private const val BEGIN_PUBKEY = "-----BEGIN PUBLIC KEY-----"
        private const val END_PUBKEY = "-----END PUBLIC KEY-----"
        private const val BEGIN_PRIKEY = "-----BEGIN PRIVATE KEY-----"
        private const val END_PRIKEY = "-----END PRIVATE KEY-----"

        operator fun invoke(privateKeyPath: String?, publicKeyPath: String?): RSAUtil {
            val rsaUtil = RSAUtil()
            privateKeyPath?.let { rsaUtil.loadPrivateKey(it) }
            publicKeyPath?.let { rsaUtil.loadPublicKey(it) }
            return rsaUtil
        }
    }

    private fun loadPublicKey(path: String) {
        var keyString = Files.readString(Paths.get(path))
        keyString = keyString.replace(BEGIN_PUBKEY, "")
            .replace(END_PUBKEY, "")
            .replace(System.lineSeparator(), "")
        val decoded = Base64.getDecoder().decode(keyString)
        val spec = X509EncodedKeySpec(decoded)
        val factory = KeyFactory.getInstance(KEY_ALGORITHM)
        publicKey = factory.generatePublic(spec)
    }

    private fun loadPrivateKey(path: String) {
        var keyString = Files.readString(Paths.get(path))
        keyString = keyString.replace(BEGIN_PRIKEY, "")
            .replace(END_PRIKEY, "")
            .replace(System.lineSeparator(), "")
        val decoded = Base64.getDecoder().decode(keyString)

        val spec = PKCS8EncodedKeySpec(decoded)
        val factory = KeyFactory.getInstance(KEY_ALGORITHM)
        privateKey = factory.generatePrivate(spec)
    }


    fun decrypt(base64data: String): String {
        if (privateKey == null) {
            throw Exception("Private Key is null")
        }
        cipher.init(Cipher.DECRYPT_MODE, privateKey)
        val decrypted = cipher.doFinal(Base64.getDecoder().decode(base64data))
        return String(decrypted, StandardCharsets.UTF_8)
    }

    fun encrypt(data: String): String {
        if (publicKey == null) {
            throw Exception("Public Key is null")
        }
        cipher.init(Cipher.ENCRYPT_MODE, publicKey)
        val encrypted = cipher.doFinal(data.toByteArray())
        return String(Base64.getEncoder().encode(encrypted))
    }
}
