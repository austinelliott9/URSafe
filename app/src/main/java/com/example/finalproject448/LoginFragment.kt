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


class LoginFragment : Fragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val prefs = CredentialsStorage.getPrefs(requireContext())
        val savedPassword = prefs.getString("user_password", null)

        if (savedPassword == null) {
            // No password yet, go to password creation screen
            findNavController().navigate(R.id.action_createPasswordFragment_to_loginFragment)
            return
        }

        val editPassword = view.findViewById<EditText>(R.id.editTextText)
        val myButton = view.findViewById<Button>(R.id.goToCredentials)

        myButton.setOnClickListener {
            val enteredPassword = editPassword.text.toString()
            if (enteredPassword == savedPassword) {
                Toast.makeText(requireContext(), "Login Successful!", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_loginFragment_to_credentialsListFragment)
            } else {
                Toast.makeText(requireContext(), "Incorrect login, try again!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }
}


