package com.example.ursafe

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.example.ursafe.data.URSafeDatabase
import com.example.ursafe.util.DatabaseUtils
import com.example.ursafe.util.SecurityUtils.hashToBytes
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout

class LoginFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    @SuppressLint("SetTextI18n", "ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val editPassword = view.findViewById<EditText>(R.id.editTextText)
        val loginButton = view.findViewById<Button>(R.id.goToCredentials)
        val passwordLayout = view.findViewById<TextInputLayout>(R.id.passwordInputLayout)

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

        // Set the hint and button text depending on whether this is first-time setup
        if (savedPassword == null) {
            passwordLayout.hint = "Set your password"
            loginButton.text = "Save Password"
        } else {
            passwordLayout.hint = "Enter your password"
            loginButton.text = "Login"
        }

        loginButton.setOnClickListener {
            val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)

            val enteredPassword = editPassword.text.toString()

            if (enteredPassword.isBlank()) {
                Snackbar.make(requireView(), "Please enter a password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (savedPassword == null) {
                // First time use: Save password
                val hashed = Base64.encodeToString(hashToBytes(enteredPassword), Base64.DEFAULT)
                sharedPrefs.edit { putString("user_password", hashed) }
                val passphrase = hashToBytes(enteredPassword)
                DatabaseUtils.setPassphrase(passphrase)
                URSafeDatabase.getInstance(requireContext(), passphrase)
                Snackbar.make(requireView(), "Password saved!", Snackbar.LENGTH_SHORT).show()
            } else {
                val hashedInput = Base64.encodeToString(hashToBytes(enteredPassword), Base64.DEFAULT)
                if (hashedInput == savedPassword) {
                    val passphrase = hashToBytes(enteredPassword)
                    DatabaseUtils.setPassphrase(passphrase)
                    URSafeDatabase.getInstance(requireContext(), passphrase)
                    Snackbar.make(requireView(), "Success!", Snackbar.LENGTH_SHORT).show()
                } else {
                    Snackbar.make(requireView(), "Incorrect password", Snackbar.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            }

            editPassword.text?.clear()
            findNavController().navigate(R.id.action_loginFragment_to_credentialsListFragment)
        }
    }
}
