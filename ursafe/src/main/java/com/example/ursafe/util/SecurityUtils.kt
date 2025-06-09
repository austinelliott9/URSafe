package com.example.ursafe.util

import java.security.MessageDigest

object SecurityUtils {
    fun hashToBytes(input: String): ByteArray {
        val digest = MessageDigest.getInstance("SHA-256")
        return digest.digest(input.toByteArray(Charsets.UTF_8))
    }
}