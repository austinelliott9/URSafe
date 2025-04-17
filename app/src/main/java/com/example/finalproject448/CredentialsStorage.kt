package com.example.finalproject448
import android.content.Context
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object CredentialsStorage {

    private const val PREF_NAME = "secure_prefs"
    private const val CREDENTIALS_KEY = "stored_credentials"
    private val gson = Gson()

    // initialize shared preferences for better performance and reuse
    fun getPrefs(context: Context) = EncryptedSharedPreferences.create(
        context,
        PREF_NAME,
        MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build(),
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )
    //save credentials to json file
    fun saveCredentials(context: Context, credentials: List<Credentials>) {
        val json = gson.toJson(credentials)
        getPrefs(context).edit { putString(CREDENTIALS_KEY, json) }
    }
    //loading credentials from json file
    fun loadCredentials(context: Context): MutableList<Credentials> {
        val json = getPrefs(context).getString(CREDENTIALS_KEY, null) ?: return mutableListOf()
        val type = object : TypeToken<MutableList<Credentials>>() {}.type
        return gson.fromJson(json, type)
    }
    fun saveUserPassword(context: Context, password: String) {
        getPrefs(context).edit { putString("user_password", password) }
    }
    fun validatePassword(context: Context, enteredPassword: String): Boolean {
        val storedPassword = getPrefs(context).getString("user_password", null)
        return storedPassword == enteredPassword
    }
}
