package com.example.finalproject448

class PasswordGenerator {
    private val upperChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    private val lowerChars = "abcdefghijklmnopqrstuvwxyz"
    private val numbers = "0123456789"
    private val specialChars = " !\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~"

    fun generatePassword(
        length: Int,
        useUppercase: Boolean,
        useLowercase: Boolean,
        useNumbers: Boolean,
        useSpecialChars: Boolean
    ): String {
        var characterPool = ""
        if (useUppercase) characterPool += upperChars
        if (useLowercase) characterPool += lowerChars
        if (useNumbers) characterPool += numbers
        if (useSpecialChars) characterPool += specialChars

        return (1..length)
            .map { characterPool.random() }
            .joinToString("") }
    }