package com.example.finalproject448

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class CreatePasswordFragment : Fragment() {

    private var isChangeMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isChangeMode = arguments?.getBoolean("isChangeMode", false) ?: false
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_initial_create_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val currentPasswordField = view.findViewById<EditText>(R.id.currentPasswordEditText)
        val passwordField = view.findViewById<EditText>(R.id.passwordEditText)
        val confirmPasswordField = view.findViewById<EditText>(R.id.confirmPasswordEditText)
        val setPasswordButton = view.findViewById<Button>(R.id.setPasswordButton)
        val title = view.findViewById<TextView>(R.id.createPasswordTitle)

        // Only show current password field if changing password
        currentPasswordField.visibility = if (isChangeMode) View.VISIBLE else View.GONE
        title.text = if (isChangeMode) "Change Password" else "Create Password"

        setPasswordButton.setOnClickListener {
            val prefs = CredentialsStorage.getPrefs(requireContext())
            val savedPassword = prefs.getString("user_password", null)

            val current = currentPasswordField.text.toString()
            val newPassword = passwordField.text.toString()
            val confirm = confirmPasswordField.text.toString()

            // Check if current password matches (only for change mode)
            if (isChangeMode && current != savedPassword) {
                Toast.makeText(requireContext(), "Current password incorrect", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Check for empty password fields
            if (newPassword.trim().isEmpty() || confirm.trim().isEmpty()) {
                Toast.makeText(requireContext(), "Password fields cannot be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Check if new passwords match
            if (newPassword != confirm) {
                Toast.makeText(requireContext(), "Passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Save the new password using CredentialsStorage utility
            if (!isChangeMode) {
                CredentialsStorage.saveUserPassword(requireContext(), newPassword)
            }

            Toast.makeText(requireContext(), "Password saved!", Toast.LENGTH_SHORT).show()

            // Navigate based on the mode
            if (isChangeMode) {
                findNavController().navigateUp() // go back to previous screen
            } else {
                findNavController().navigate(R.id.action_createPasswordFragment_to_loginFragment)
            }
        }
    }
}
