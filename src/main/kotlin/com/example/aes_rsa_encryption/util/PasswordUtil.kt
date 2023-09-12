package com.example.aes_rsa_encryption.util

import java.lang.StringBuilder
import kotlin.random.Random

object PasswordUtil {
    fun random(length: Int = 8): String {
        val set = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
        val random = Random(System.nanoTime())
        val result = StringBuilder()
        for (i in 0 until length) {
            val index = random.nextInt(set.length)
            result.append(set[index])
        }
        return result.toString()
    }
}
