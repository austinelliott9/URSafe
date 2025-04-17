package com.example.finalproject448

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

class LoginFragment : Fragment() {
    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val editPassword = view.findViewById<EditText>(R.id.editTextText)
        val myButton = view.findViewById<Button>(R.id.goToCredentials)

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

        // Set the hint based on whether a password is saved
        if (savedPassword == null) {
            editPassword.hint = "Set your password"
            myButton.text = "Save Password"
        } else {
            editPassword.hint = "Enter your password"
            myButton.text = "Login"
        }

        myButton.setOnClickListener {
            val enteredPassword = editPassword.text.toString()

            if (enteredPassword.isBlank()){
                Toast.makeText(requireContext(), "Please enter a password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (savedPassword == null) {
                // Save the password if it doesn't exist
                sharedPrefs.edit { putString("user_password", enteredPassword) }
                Toast.makeText(requireContext(), "Password saved!", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_loginFragment_to_credentialsListFragment)
            } else {
                // Check entered password against saved password
                if (enteredPassword == savedPassword) {
                    Toast.makeText(requireContext(), "Success!", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_loginFragment_to_credentialsListFragment)
                } else {
                    Toast.makeText(requireContext(), "Incorrect password", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }
}