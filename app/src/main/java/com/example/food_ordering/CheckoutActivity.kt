package com.example.food_ordering

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CheckoutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        val summaryRecyclerView = findViewById<RecyclerView>(R.id.summaryRecyclerView)
        summaryRecyclerView.layoutManager = LinearLayoutManager(this)

        val summaryAdapter = SummaryAdapter(Cart.items)
        summaryRecyclerView.adapter = summaryAdapter

        val totalPrice = findViewById<TextView>(R.id.totalPrice)
        totalPrice.text = "Total: $${Cart.getCartTotal()}"

        val creditCardInput = findViewById<EditText>(R.id.creditCardInput)
        val payButton = findViewById<Button>(R.id.payButton)

        payButton.setOnClickListener {
            if (creditCardInput.text.isNotEmpty()) {
                Toast.makeText(this, "Payment Successful!", Toast.LENGTH_SHORT).show()
                Cart.items.clear()
                // Navigate back to the menu or a confirmation screen
                finish()
            } else {
                Toast.makeText(this, "Please enter your credit card number", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
