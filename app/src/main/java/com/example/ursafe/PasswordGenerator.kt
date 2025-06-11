package com.example.ursafe

import kotlin.random.Random

object PasswordGenerator {

    private const val UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    private const val LOWERCASE = "abcdefghijklmnopqrstuvwxyz"
    private const val DIGITS = "0123456789"
    private const val SYMBOLS = "!@#\$%^&*()-_=+[{]}|;:',<.>/?"

    fun generate(
        length: Int,
        useUppercase: Boolean,
        useLowercase: Boolean,
        useDigits: Boolean,
        useSymbols: Boolean
    ): String {
        if (length <= 0) return ""

        val charPool = buildString {
            if (useUppercase) append(UPPERCASE)
            if (useLowercase) append(LOWERCASE)
            if (useDigits) append(DIGITS)
            if (useSymbols) append(SYMBOLS)
        }

        if (charPool.isEmpty()) return ""

        return (1..length)
            .map { charPool[Random.nextInt(charPool.length)] }
            .joinToString("")
    }
}
