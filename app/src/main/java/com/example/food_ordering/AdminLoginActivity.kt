package com.example.food_ordering

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class AdminLoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_login)

        val adminUsernameEt = findViewById<TextInputEditText>(R.id.adminUsername)
        val adminPasswordEt = findViewById<TextInputEditText>(R.id.adminPassword)
        val adminLoginButton = findViewById<MaterialButton>(R.id.adminLoginButton)

        adminLoginButton.setOnClickListener {
            val user = adminUsernameEt.text.toString().trim()
            val pass = adminPasswordEt.text.toString().trim()

            // Using trim() to avoid issues with accidental spaces
            if (user.equals("admin", ignoreCase = true) && pass == "admin") {
                Toast.makeText(this, "Admin Login Successful", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, AdminPanelActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "Invalid Admin Credentials", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
