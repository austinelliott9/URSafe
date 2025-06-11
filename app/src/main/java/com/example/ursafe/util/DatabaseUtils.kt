package com.example.ursafe.util

object DatabaseUtils {
    private var passphrase: ByteArray? = null

    fun setPassphrase(pass: ByteArray) {
        passphrase = pass
    }

    fun getPassphrase(): ByteArray {
        return passphrase ?: throw IllegalStateException("Passphrase not initialized")
    }
}
