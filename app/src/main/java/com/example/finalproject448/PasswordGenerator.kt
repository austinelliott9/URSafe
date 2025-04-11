package com.example.finalproject448

import android.content.Context

//import kotlin.coroutines.jvm.internal.CompletedContinuation.context

class PasswordGenerator {
    val upperChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    val lowerChars = "abcdefghijklmnopqrstuvwxyz"
    val numbers = "0123456789"
    val specialChars = " !\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~"

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

        if (characterPool.isEmpty()) {
            throw IllegalArgumentException("At least one character type must be selected.")
        }

        return (1..length)
            .map { characterPool.random() }
            .joinToString("") }

        /*fun saveGeneratedCredentials(context: Context, service: String, username: String, password: String) {
            val newCredentials = Credentials(service, username, password)
            val existingCredentials = CredentialsStorage.loadCredentials(context)
            existingCredentials.add(newCredentials)
            CredentialsStorage.saveCredentials(context, existingCredentials)*/
    }

