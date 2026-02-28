package com.example.food_ordering

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AdminLoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_login)

        val adminUsername = findViewById<EditText>(R.id.adminUsername)
        val adminPassword = findViewById<EditText>(R.id.adminPassword)
        val adminLoginButton = findViewById<Button>(R.id.adminLoginButton)

        adminLoginButton.setOnClickListener {
            // Mock admin login logic
            if (adminUsername.text.toString() == "admin" && adminPassword.text.toString() == "admin") {
                Toast.makeText(this, "Admin Login Successful", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, AdminPanelActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Invalid Admin Credentials", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
