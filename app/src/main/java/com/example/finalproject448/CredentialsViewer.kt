package com.example.finalproject448
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class CredentialsViewer(
    context: Context,
    private val credentials: List<Credentials>
) : ArrayAdapter<Credentials>(context, 0, credentials) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(
            android.R.layout.simple_list_item_2, parent, false
        )

        val item = credentials[position]

        val text1 = view.findViewById<TextView>(android.R.id.text1)
        val text2 = view.findViewById<TextView>(android.R.id.text2)

        text1.text = "${item.service} — ${item.username}"
        text2.text = "•••••••••• (tap to reveal)"

        view.setOnClickListener {
            if (text2.text.startsWith("••")) {
                text2.text = item.password
            } else {
                text2.text = "•••••••••• (tap to reveal)"
            }
        }

        return view
    }
}
