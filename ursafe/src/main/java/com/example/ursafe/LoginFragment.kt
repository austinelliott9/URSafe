package com.example.ursafe

import android.annotation.SuppressLint
import android.os.Bundle
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
import androidx.core.content.edit
import com.example.ursafe.data.URSafeDatabase
import com.example.ursafe.util.SecurityUtils.hashToBytes
import com.google.android.material.textfield.TextInputLayout
import java.security.MessageDigest

class LoginFragment : Fragment() {
    @SuppressLint("SetTextI18n", "ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val editPassword = view.findViewById<EditText>(R.id.editTextText)
        val loginButton = view.findViewById<Button>(R.id.goToCredentials)

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

        val savedPassword = sharedPrefs.getString("user_password", null)
        val passwordLayout = view.findViewById<TextInputLayout>(R.id.passwordInputLayout)

        // Set the hint based on whether a password is saved
        if (savedPassword == null) {
            passwordLayout.hint = "Set your password"
            loginButton.text = "Save Password"
        } else {
            passwordLayout.hint = "Enter your password"
            loginButton.text = "Login"
        }

        loginButton.setOnClickListener {
            val enteredPassword = editPassword.text.toString()

            if (enteredPassword.isBlank()){
                Toast.makeText(requireContext(), "Please enter a password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (savedPassword == null) {
                // Save the password if it doesn't exist
                sharedPrefs.edit { putString("user_password", enteredPassword) }
                val passphrase = hashToBytes(enteredPassword)
                val db = URSafeDatabase.getInstance(requireContext(), passphrase)
                Toast.makeText(requireContext(), "Password saved!", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_loginFragment_to_credentialsListFragment)
            } else {
                // Check entered password against saved password
                if (enteredPassword == savedPassword) {
                    val passphrase = hashToBytes(enteredPassword)
                    val db = URSafeDatabase.getInstance(requireContext(), passphrase)
                    Toast.makeText(requireContext(), "Success!", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_loginFragment_to_credentialsListFragment)
                } else {
                    Toast.makeText(requireContext(), "Incorrect password", Toast.LENGTH_SHORT).show()
                }
            }
            editPassword.text?.clear()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }
}