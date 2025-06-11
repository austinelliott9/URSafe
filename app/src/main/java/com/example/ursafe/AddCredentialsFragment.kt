package com.example.ursafe

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.ursafe.viewmodel.CredentialViewModel
import com.google.android.material.snackbar.Snackbar

class AddCredentialsFragment : Fragment() {

    private lateinit var serviceEditText: EditText
    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var toGeneratorButton: Button

    private lateinit var viewModel: CredentialViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_add_credentials, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        serviceEditText = view.findViewById(R.id.serviceEditText)
        usernameEditText = view.findViewById(R.id.usernameEditText)
        passwordEditText = view.findViewById(R.id.passwordEditText)
        saveButton = view.findViewById(R.id.saveButton)
        toGeneratorButton = view.findViewById(R.id.toGeneratorButton)

        viewModel = ViewModelProvider(requireActivity())[CredentialViewModel::class.java]

        saveButton.setOnClickListener { saveCredentials() }

        toGeneratorButton.setOnClickListener {
            findNavController().navigate(R.id.action_addCredentialsFragment_to_generatePasswordFragment)
        }

        // Receive generated password from GeneratePasswordFragment
        parentFragmentManager.setFragmentResultListener("passwordRequestKey", viewLifecycleOwner) { _, bundle ->
            val generatedPassword = bundle.getString("generatedPassword")
            passwordEditText.setText(generatedPassword)
        }
    }

    private fun saveCredentials() {
        val service = serviceEditText.text.toString().trim()
        val username = usernameEditText.text.toString().trim()
        val password = passwordEditText.text.toString().trim()

        if (TextUtils.isEmpty(service) || TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            Snackbar.make(requireView(), "Please fill in all fields", Snackbar.LENGTH_SHORT).show()
            return
        }

        viewModel.addCredential(service, username, password)

        Snackbar.make(requireView(), "Credential saved", Toast.LENGTH_SHORT).show()
        findNavController().popBackStack()
    }
}
