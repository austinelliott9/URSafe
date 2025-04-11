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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_generate_password, container, false)
        upperCheckBox = view.findViewById(R.id.upperCheckBox)
        lowerCheckBox = view.findViewById(R.id.lowerCheckBox)
        numberCheckBox = view.findViewById(R.id.numberCheckBox)
        specialCheckBox = view.findViewById(R.id.specialCheckBox)
        lengthEditText = view.findViewById(R.id.lengthEditText)
        generateButton = view.findViewById(R.id.generateButton)
        generatedPasswordTextView = view.findViewById(R.id.generatedPasswordTextView)

        generateButton.setOnClickListener {
            val length = lengthEditText.text.toString().toIntOrNull()
            if (length != null && length > 0) {
                val upper = upperCheckBox.isChecked
                val lower = lowerCheckBox.isChecked
                val number = numberCheckBox.isChecked
                val special = specialCheckBox.isChecked

                if (!upper && !lower && !number && !special) {
                    generatedPasswordTextView.text = "Please select at least one character type."
                    return@setOnClickListener
                }

                val password  = PasswordGenerator().generatePassword(length, upper, lower, number, special)
                generatedPasswordTextView.text = password
            } else {
                generatedPasswordTextView.text = "Please enter a length greater than 0."
            }
        }
        return view
    }
}