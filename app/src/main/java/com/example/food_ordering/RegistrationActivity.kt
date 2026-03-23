package com.example.food_ordering

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class RegistrationActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        dbHelper = DatabaseHelper(this)

        val nameEt = findViewById<TextInputEditText>(R.id.name)
        val emailEt = findViewById<TextInputEditText>(R.id.email)
        val phoneEt = findViewById<TextInputEditText>(R.id.phone)
        val passwordEt = findViewById<TextInputEditText>(R.id.password)
        val registerButton = findViewById<MaterialButton>(R.id.registerButton)
        val loginRedirectText = findViewById<TextView>(R.id.loginRedirectText)

        registerButton.setOnClickListener {
            val name = nameEt.text.toString().trim()
            val email = emailEt.text.toString().trim()
            val phone = phoneEt.text.toString().trim()
            val password = passwordEt.text.toString().trim()

            if (validateInput(name, email, phone, password)) {
                val user = User(name = name, email = email, phone = phone, password = password)
                val success = dbHelper.addUser(user)
                if (success) {
                    Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this, "Registration Failed. Email might already exist.", Toast.LENGTH_SHORT).show()
                }
            }
        }

        loginRedirectText.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun validateInput(name: String, email: String, phone: String, pass: String): Boolean {
        if (name.isEmpty()) {
            Toast.makeText(this, "Enter name", Toast.LENGTH_SHORT).show()
            return false
        }
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Enter a valid email", Toast.LENGTH_SHORT).show()
            return false
        }
        if (phone.isEmpty() || phone.length < 10) {
            Toast.makeText(this, "Enter a valid phone number", Toast.LENGTH_SHORT).show()
            return false
        }
        if (pass.isEmpty() || pass.length < 6) {
            Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }
}
