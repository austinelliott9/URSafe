package com.example.finalproject448

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class AddCredentialsFragment : Fragment() {

    private lateinit var serviceEditText: EditText
    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var saveButton: Button
    private val gson = Gson()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_credentials, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initializes UI components, add these to fragment credentials add
        serviceEditText = view.findViewById(R.id.serviceEditText)
        usernameEditText = view.findViewById(R.id.usernameEditText)
        passwordEditText = view.findViewById(R.id.passwordEditText)
        saveButton = view.findViewById(R.id.saveButton)

        // Set click listener for the save button
        saveButton.setOnClickListener {
            saveCredentials()
        }
    }

    private fun saveCredentials() {
        val service = serviceEditText.text.toString().trim()
        val username = usernameEditText.text.toString().trim()
        val password = passwordEditText.text.toString().trim()

        if (TextUtils.isEmpty(service) || TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            // Shows a message or toast if fields are empty
            return
        }

        // Creates a new Credentials object
        val newCredential = Credentials(service, username, password)

        // Saves the new credentials to EncryptedSharedPreferences
        val masterKey = MasterKey.Builder(requireContext())
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        val sharedPrefs = EncryptedSharedPreferences.create(
            requireContext(),
            "secure_prefs",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

        // Loads existing credentials from list
        val credentialsList = loadCredentials(requireContext())

        // Adds the new credential to the list
        credentialsList.add(newCredential)

        // Saves the updated list back to SharedPreferences
        val json = gson.toJson(credentialsList)
        sharedPrefs.edit().putString("stored_credentials", json).apply()

        // Optionally, navigate back to the list of credentials
        // Navigation code (if using NavController) could go here
    }

    // Loads credentials from EncryptedSharedPreferences
    private fun loadCredentials(context: Context): MutableList<Credentials> {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        val sharedPrefs = EncryptedSharedPreferences.create(
            context,
            "secure_prefs",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

        val json = sharedPrefs.getString("stored_credentials", null) ?: return mutableListOf()
        val type = object : TypeToken<MutableList<Credentials>>() {}.type
        return gson.fromJson(json, type)
    }
}
