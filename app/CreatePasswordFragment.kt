package com.example.finalproject448

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class CreatePasswordFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_create_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val passwordField = view.findViewById<EditText>(R.id.passwordEditText)
        val confirmPasswordField = view.findViewById<EditText>(R.id.confirmPasswordEditText)
        val setPasswordButton = view.findViewById<Button>(R.id.setPasswordButton)

        setPasswordButton.setOnClickListener {
            val password = passwordField.text.toString()
            val confirmPassword = confirmPasswordField.text.toString()

            if (password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(requireContext(), "Fields cannot be empty!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                Toast.makeText(requireContext(), "Passwords do not match!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val prefs = getEncryptedPrefs(requireContext())
            prefs.edit().putString("user_password", password).apply()

            Toast.makeText(requireContext(), "Password set successfully!", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_createPasswordFragment_to_loginFragment)
        }
        val bundle = Bundle().apply {
            putBoolean("isChangeMode", true)
        }
        findNavController().navigate(R.id.action_to_createPasswordFragment, bundle)
    }
}
