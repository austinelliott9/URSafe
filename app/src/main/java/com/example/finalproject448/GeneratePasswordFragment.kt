package com.example.finalproject448

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController

/**
 * A simple [Fragment] subclass.
 * Use the [GeneratePasswordFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GeneratePasswordFragment : Fragment() {
    private lateinit var upperCheckBox: CheckBox
    private lateinit var lowerCheckBox: CheckBox
    private lateinit var numberCheckBox: CheckBox
    private lateinit var specialCheckBox: CheckBox
    private lateinit var lengthEditText: EditText
    private lateinit var generateButton: Button
    private lateinit var generatedPasswordTextView: TextView
    private lateinit var confirmButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_generate_password, container, false)
        val maxLength = 64

        upperCheckBox = view.findViewById(R.id.upperCheckBox)
        lowerCheckBox = view.findViewById(R.id.lowerCheckBox)
        numberCheckBox = view.findViewById(R.id.numberCheckBox)
        specialCheckBox = view.findViewById(R.id.specialCheckBox)
        lengthEditText = view.findViewById(R.id.lengthEditText)
        generateButton = view.findViewById(R.id.generateButton)
        generatedPasswordTextView = view.findViewById(R.id.generatedPasswordTextView)
        confirmButton = view.findViewById(R.id.confirmButton)

        generateButton.setOnClickListener {
            val length = lengthEditText.text.toString().toIntOrNull()
            if (length != null && length in 1..maxLength) {
                val upper = upperCheckBox.isChecked
                val lower = lowerCheckBox.isChecked
                val number = numberCheckBox.isChecked
                val special = specialCheckBox.isChecked

                if (!upper && !lower && !number && !special) {
                    Toast.makeText(requireContext(), "Please select at least one character type.", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                val password  = PasswordGenerator().generatePassword(length, upper, lower, number, special)
                generatedPasswordTextView.text = password

                confirmButton.visibility = View.VISIBLE
            } else {
                Toast.makeText(requireContext(), "Please enter a length between 1 and $maxLength.", Toast.LENGTH_SHORT).show()
            }
        }
        confirmButton.setOnClickListener {
            val password = generatedPasswordTextView.text.toString()
            if (password.isNotEmpty()) {
                val result = Bundle().apply{
                    putString("generatedPassword", password)
                }
                parentFragmentManager.setFragmentResult("passwordRequestKey", result)
                findNavController().popBackStack()
            }
        }
        return view
    }
}