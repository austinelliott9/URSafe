package com.example.ursafe.util

import android.content.Context
import android.util.Base64
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import java.security.MessageDigest
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

object SecurityUtils {

    fun hashToBytes(input: String): ByteArray {
        return MessageDigest.getInstance("SHA-256").digest(input.toByteArray(Charsets.UTF_8))
    }

    private const val KEY_ALIAS = "encryption_key"
    private const val PREF_NAME = "ursafe_secure_prefs"
    private const val ENCRYPTION_KEY_PREF = "aes_key"

    private fun getOrCreateKey(context: Context): SecretKey {
        val sharedPrefs = EncryptedSharedPreferences.create(
            context,
            PREF_NAME,
            MasterKey.Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build(),
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

        val existingKey = sharedPrefs.getString(ENCRYPTION_KEY_PREF, null)
        return if (existingKey != null) {
            val decoded = Base64.decode(existingKey, Base64.DEFAULT)
            SecretKeySpec(decoded, "AES")
        } else {
            val newKey = ByteArray(32).also { java.security.SecureRandom().nextBytes(it) }
            sharedPrefs.edit().putString(ENCRYPTION_KEY_PREF, Base64.encodeToString(newKey, Base64.DEFAULT)).apply()
            SecretKeySpec(newKey, "AES")
        }
    }

    fun encrypt(plainText: String, context: Context): String {
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        val key = getOrCreateKey(context)
        val iv = ByteArray(16).also { java.security.SecureRandom().nextBytes(it) }
        cipher.init(Cipher.ENCRYPT_MODE, key, IvParameterSpec(iv))
        val encrypted = cipher.doFinal(plainText.toByteArray(Charsets.UTF_8))
        return Base64.encodeToString(iv + encrypted, Base64.DEFAULT)
    }

    fun decrypt(cipherText: String, context: Context): String {
        val raw = Base64.decode(cipherText, Base64.DEFAULT)
        val iv = raw.sliceArray(0 until 16)
        val encrypted = raw.sliceArray(16 until raw.size)
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        cipher.init(Cipher.DECRYPT_MODE, getOrCreateKey(context), IvParameterSpec(iv))
        return String(cipher.doFinal(encrypted), Charsets.UTF_8)
    }
}