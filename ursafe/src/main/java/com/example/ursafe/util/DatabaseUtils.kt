package com.example.ursafe.util

object DatabaseUtils {
    private var passphrase: ByteArray? = null

    fun setPassphrase(value: ByteArray) {
        passphrase = value
    }

    fun getPassphrase(): ByteArray {
        return passphrase ?: throw IllegalStateException("Passphrase not set")
    }

    fun isPassphraseSet(): Boolean = passphrase != null
}
