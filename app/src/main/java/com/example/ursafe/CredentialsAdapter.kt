package com.example.ursafe

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.ursafe.data.Credential

class CredentialsAdapter(
    private var fullList: MutableList<Credential>,
    private val onUpdate: () -> Unit
) : RecyclerView.Adapter<CredentialsAdapter.ViewHolder>() {

    private var displayList: MutableList<Credential> = fullList.toMutableList()

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val serviceText: TextView = view.findViewById(R.id.text_service)
        val usernameText: TextView = view.findViewById(R.id.text_username)
        val passwordText: TextView = view.findViewById(R.id.text_password)
        val toggleVisibility: ImageView = view.findViewById(R.id.toggleVisibility)
        val copyUsername: ImageView = view.findViewById(R.id.copyUsername)
        val copyPassword: ImageView = view.findViewById(R.id.copyPassword)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_credential, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val credential = displayList[position]
        val context = holder.itemView.context

        holder.serviceText.text = credential.serviceName
        holder.usernameText.text = credential.username
        holder.passwordText.text = credential.password
        holder.passwordText.transformationMethod = PasswordTransformationMethod.getInstance()

        var isPasswordVisible = false
        holder.toggleVisibility.setImageResource(R.drawable.ic_visibility_off)

        holder.toggleVisibility.setOnClickListener {
            isPasswordVisible = !isPasswordVisible
            if (isPasswordVisible) {
                holder.passwordText.transformationMethod = null
                holder.toggleVisibility.setImageResource(R.drawable.ic_visibility)
            } else {
                holder.passwordText.transformationMethod = PasswordTransformationMethod.getInstance()
                holder.toggleVisibility.setImageResource(R.drawable.ic_visibility_off)
            }
        }

        holder.copyUsername.setOnClickListener {
            copyToClipboard(context, "Username", credential.username)
        }

        holder.copyPassword.setOnClickListener {
            copyToClipboard(context, "Password", credential.password)
        }
    }

    override fun getItemCount(): Int = displayList.size

    fun updateList(newList: List<Credential>) {
        fullList = newList.toMutableList()
        displayList = fullList.toMutableList()
        notifyDataSetChanged()
        onUpdate()
    }

    fun filter(query: String) {
        displayList = if (query.isBlank()) {
            fullList.toMutableList()
        } else {
            fullList.filter {
                it.serviceName.contains(query, ignoreCase = true)
            }.toMutableList()
        }
        notifyDataSetChanged()
    }

    fun getItem(position: Int): Credential = displayList[position]

    private fun copyToClipboard(context: Context, label: String, text: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboard.setPrimaryClip(ClipData.newPlainText(label, text))
        Toast.makeText(context, "$label copied to clipboard", Toast.LENGTH_SHORT).show()
    }
}
