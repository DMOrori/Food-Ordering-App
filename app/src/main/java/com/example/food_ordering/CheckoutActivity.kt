package com.example.food_ordering

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class CheckoutActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        dbHelper = DatabaseHelper(this)

        val summaryRecyclerView = findViewById<RecyclerView>(R.id.summaryRecyclerView)
        summaryRecyclerView.layoutManager = LinearLayoutManager(this)

        val summaryAdapter = SummaryAdapter(Cart.items)
        summaryRecyclerView.adapter = summaryAdapter

        val totalPriceTv = findViewById<TextView>(R.id.totalPrice)
        val total = Cart.getCartTotal()
        totalPriceTv.text = "KSh $total"

        val paymentGroup = findViewById<RadioGroup>(R.id.paymentGroup)
        val phoneInputLayout = findViewById<TextInputLayout>(R.id.phoneInputLayout)
        val cardInputLayout = findViewById<TextInputLayout>(R.id.cardInputLayout)
        val phoneInput = findViewById<TextInputEditText>(R.id.phoneInput)
        val cardInput = findViewById<TextInputEditText>(R.id.cardInput)
        val payButton = findViewById<MaterialButton>(R.id.payButton)

        // Pre-fill phone if logged in
        SessionManager.userEmail?.let {
            phoneInput.setText(dbHelper.getPhoneByEmail(it))
        }

        paymentGroup.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == R.id.radioMpesa) {
                phoneInputLayout.visibility = View.VISIBLE
                cardInputLayout.visibility = View.GONE
            } else {
                phoneInputLayout.visibility = View.GONE
                cardInputLayout.visibility = View.VISIBLE
            }
        }

        payButton.setOnClickListener {
            val selectedId = paymentGroup.checkedRadioButtonId
            val method = if (selectedId == R.id.radioMpesa) "M-PESA" else "Card"
            
            if (selectedId == R.id.radioMpesa) {
                val phone = phoneInput.text.toString()
                if (phone.length >= 10) {
                    showMpesaDialog(phone, total)
                } else {
                    Toast.makeText(this, "Enter valid M-PESA number", Toast.LENGTH_SHORT).show()
                }
            } else {
                val card = cardInput.text.toString()
                if (card.length >= 16) {
                    processOrder("Card", total)
                } else {
                    Toast.makeText(this, "Enter valid Card number", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun showMpesaDialog(phone: String, amount: Double) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("M-PESA STK Push")
        builder.setMessage("A push request of KSh $amount has been sent to $phone. Please enter your M-PESA PIN on your phone to complete the transaction.")
        builder.setPositiveButton("I have Paid") { _, _ ->
            processOrder("M-PESA", amount)
        }
        builder.setNegativeButton("Cancel", null)
        builder.show()
    }

    private fun processOrder(method: String, total: Double) {
        val email = SessionManager.userEmail ?: "Guest"
        val details = Cart.items.joinToString { "${it.name} (KSh ${it.price})" }
        
        val success = dbHelper.addOrder(email, details, total, method)
        
        if (success) {
            Toast.makeText(this, "Payment Successful! Order placed.", Toast.LENGTH_LONG).show()
            Cart.items.clear()
            val intent = Intent(this, MenuActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        } else {
            Toast.makeText(this, "Failed to save order", Toast.LENGTH_SHORT).show()
        }
    }
}
