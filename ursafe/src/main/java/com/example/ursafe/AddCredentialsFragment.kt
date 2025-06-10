package com.example.ursafe

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import androidx.core.content.edit
import com.example.ursafe.data.Credential

class AddCredentialsFragment : Fragment() {

    private lateinit var serviceEditText: EditText
    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var toGeneratorButton: Button
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

        serviceEditText = view.findViewById(R.id.serviceEditText)
        usernameEditText = view.findViewById(R.id.usernameEditText)
        passwordEditText = view.findViewById(R.id.passwordEditText)
        saveButton = view.findViewById(R.id.saveButton)
        toGeneratorButton = view.findViewById(R.id.toGeneratorButton)

        // Set click listener for the buttons
        saveButton.setOnClickListener {
            saveCredentials()
        }
        toGeneratorButton.setOnClickListener{
            findNavController().navigate(R.id.action_addCredentialsFragment_to_generatePasswordFragment)
        }

        // Listen for result from password generator
        parentFragmentManager.setFragmentResultListener("passwordRequestKey", viewLifecycleOwner){_, bundle ->
            val generatedPassword = bundle.getString("generatedPassword")
            passwordEditText.setText(generatedPassword)
        }
    }

    private fun saveCredentials() {
        val service = serviceEditText.text.toString().trim()
        val username = usernameEditText.text.toString().trim()
        val password = passwordEditText.text.toString().trim()

        if (TextUtils.isEmpty(service) || TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        // Creates a new Credentials object
        val newCredential = Credential(
            id = 0,
            serviceName = service,
            username = username,
            password = password)

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
        sharedPrefs.edit { putString("stored_credentials", json) }

        // Optionally, navigate back to the list of credentials
        findNavController().popBackStack()
    }

    // Loads credentials from EncryptedSharedPreferences
    private fun loadCredentials(context: Context): MutableList<Credential> {
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
        val type = object : TypeToken<MutableList<Credential>>() {}.type
        return gson.fromJson(json, type)
    }
}
