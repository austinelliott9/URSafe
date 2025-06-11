package com.example.ursafe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar

class GeneratePasswordFragment : Fragment() {

    private lateinit var upperCheckBox: CheckBox
    private lateinit var lowerCheckBox: CheckBox
    private lateinit var numberCheckBox: CheckBox
    private lateinit var specialCheckBox: CheckBox
    private lateinit var lengthInput: EditText
    private lateinit var passwordDisplay: TextView
    private lateinit var generateButton: Button
    private lateinit var applyButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_generate_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        upperCheckBox = view.findViewById(R.id.uppercaseCheckbox)
        lowerCheckBox = view.findViewById(R.id.lowercaseCheckbox)
        numberCheckBox = view.findViewById(R.id.numbersCheckbox)
        specialCheckBox = view.findViewById(R.id.specialCheckbox)
        lengthInput = view.findViewById(R.id.lengthEditText)
        passwordDisplay = view.findViewById(R.id.passwordDisplayTextView)
        generateButton = view.findViewById(R.id.generatePasswordButton)
        applyButton = view.findViewById(R.id.applyPasswordButton)

        generateButton.setOnClickListener {
            val length = lengthInput.text.toString().toIntOrNull()
            if (length == null || length <= 0) {
                Snackbar.make(requireView(), "Enter a valid password length", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val password = PasswordGenerator.generate(
                length,
                upperCheckBox.isChecked,
                lowerCheckBox.isChecked,
                numberCheckBox.isChecked,
                specialCheckBox.isChecked
            )

            if (password.isEmpty()) {
                Snackbar.make(requireView(), "Select at least one character type", Toast.LENGTH_SHORT).show()
            } else {
                passwordDisplay.text = password
                applyButton.visibility = View.VISIBLE
            }
        }

        applyButton.setOnClickListener {
            val password = passwordDisplay.text.toString()
            if (password.isBlank()) {
                Snackbar.make(requireView(), "Generate a password first", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            parentFragmentManager.setFragmentResult(
                "passwordRequestKey",
                Bundle().apply { putString("generatedPassword", password) }
            )
            findNavController().popBackStack()
        }
    }
}
