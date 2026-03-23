package com.example.food_ordering

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import kotlin.random.Random

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private var generatedOtp: String? = null
    private var targetEmail: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        dbHelper = DatabaseHelper(this)

        val emailResetEt = findViewById<TextInputEditText>(R.id.emailReset)
        val sendOtpButton = findViewById<MaterialButton>(R.id.sendOtpButton)
        val otpSection = findViewById<LinearLayout>(R.id.otpSection)
        val otpInputEt = findViewById<TextInputEditText>(R.id.otpInput)
        val newPasswordEt = findViewById<TextInputEditText>(R.id.newPasswordInput)
        val resetPasswordButton = findViewById<MaterialButton>(R.id.resetPasswordButton)

        sendOtpButton.setOnClickListener {
            val email = emailResetEt.text.toString().trim()
            val phone = dbHelper.getPhoneByEmail(email)

            if (phone != null) {
                targetEmail = email
                generatedOtp = (1000..9999).random().toString()
                // Simulating sending SMS
                Toast.makeText(this, "OTP $generatedOtp sent to $phone", Toast.LENGTH_LONG).show()
                otpSection.visibility = View.VISIBLE
                sendOtpButton.visibility = View.GONE
            } else {
                Toast.makeText(this, "Email not found", Toast.LENGTH_SHORT).show()
            }
        }

        resetPasswordButton.setOnClickListener {
            val enteredOtp = otpInputEt.text.toString().trim()
            val newPass = newPasswordEt.text.toString().trim()

            if (enteredOtp == generatedOtp) {
                if (newPass.length >= 6) {
                    val success = dbHelper.updatePassword(targetEmail!!, newPass)
                    if (success) {
                        Toast.makeText(this, "Password updated successfully", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                } else {
                    Toast.makeText(this, "Password too short", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Invalid OTP", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
