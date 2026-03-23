package com.example.food_ordering

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton
import com.google.android.material.tabs.TabLayout

class AdminPanelActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var ordersListView: ListView
    private lateinit var menuRecyclerView: RecyclerView
    private lateinit var ordersContainer: View
    private lateinit var menuContainer: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_panel)

        dbHelper = DatabaseHelper(this)

        ordersListView = findViewById(R.id.ordersListView)
        menuRecyclerView = findViewById(R.id.adminMenuRecyclerView)
        ordersContainer = findViewById(R.id.ordersContainer)
        menuContainer = findViewById(R.id.menuContainer)
        
        val tabLayout = findViewById<TabLayout>(R.id.tabLayout)
        val addItemButton = findViewById<MaterialButton>(R.id.addItemButton)
        val clearHistoryButton = findViewById<MaterialButton>(R.id.clearHistoryButton)
        val logoutButton = findViewById<MaterialButton>(R.id.logoutButton)

        logoutButton.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        addItemButton.setOnClickListener {
            startActivity(Intent(this, AddMenuItemActivity::class.java))
        }

        clearHistoryButton.setOnClickListener {
            showClearHistoryDialog()
        }

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab?.position == 0) {
                    ordersContainer.visibility = View.VISIBLE
                    menuContainer.visibility = View.GONE
                    loadOrders()
                } else {
                    ordersContainer.visibility = View.GONE
                    menuContainer.visibility = View.VISIBLE
                    loadMenu()
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        menuRecyclerView.layoutManager = LinearLayoutManager(this)
        loadOrders()
    }

    override fun onResume() {
        super.onResume()
        loadOrders()
        loadMenu()
    }

    private fun loadOrders() {
        val totalOrdersCountTv = findViewById<TextView>(R.id.totalOrdersCount)
        val totalRevenueTv = findViewById<TextView>(R.id.totalRevenueText)
        
        val cursor = dbHelper.getAllOrders()
        val orders = mutableListOf<OrderInfo>()
        var totalRevenue = 0.0

        cursor?.use { c: Cursor ->
            if (c.moveToFirst()) {
                do {
                    val email = c.getString(c.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_EMAIL))
                    val details = c.getString(c.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ORDER_DETAILS))
                    val total = c.getDouble(c.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ORDER_TOTAL))
                    val status = c.getString(c.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ORDER_STATUS))
                    orders.add(OrderInfo(email, details, total, status))
                    totalRevenue += total
                } while (c.moveToNext())
            }
        }

        ordersListView.adapter = OrderAdapter(this, orders)
        totalOrdersCountTv.text = orders.size.toString()
        totalRevenueTv.text = "KSh ${String.format("%.2f", totalRevenue)}"
    }

    private fun loadMenu() {
        val menuItems = dbHelper.getAllMenuItems()
        menuRecyclerView.adapter = AdminMenuAdapter(menuItems) { item, action ->
            if (action == "edit") {
                val intent = Intent(this, AddMenuItemActivity::class.java)
                intent.putExtra("EDIT_ITEM", item)
                startActivity(intent)
            } else if (action == "delete") {
                showDeleteConfirmDialog(item)
            }
        }
    }

    private fun showDeleteConfirmDialog(item: MenuItem) {
        AlertDialog.Builder(this)
            .setTitle("Delete Item")
            .setMessage("Are you sure you want to delete '${item.name}'?")
            .setPositiveButton("Delete") { _, _ ->
                if (dbHelper.deleteMenuItem(item.id)) {
                    loadMenu()
                    Toast.makeText(this, "Item deleted", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showClearHistoryDialog() {
        AlertDialog.Builder(this)
            .setTitle("Clear Order History")
            .setMessage("Are you sure you want to delete all order history?")
            .setPositiveButton("Clear All") { _, _ ->
                dbHelper.clearOrderHistory()
                loadOrders()
                Toast.makeText(this, "Order history cleared", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    data class OrderInfo(val email: String, val details: String, val total: Double, val status: String)

    class OrderAdapter(context: Context, private val orders: List<OrderInfo>) : 
        ArrayAdapter<OrderInfo>(context, 0, orders) {
        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.order_item, parent, false)
            val order = getItem(position)!!
            view.findViewById<TextView>(R.id.orderUserEmail).text = order.email
            view.findViewById<TextView>(R.id.orderDetails).text = "Items: ${order.details}"
            view.findViewById<TextView>(R.id.orderTotal).text = "Total: KSh ${String.format("%.2f", order.total)}"
            view.findViewById<TextView>(R.id.orderStatus).text = order.status
            return view
        }
    }

    class AdminMenuAdapter(
        private val items: List<MenuItem>, 
        private val onAction: (MenuItem, String) -> Unit
    ) : RecyclerView.Adapter<AdminMenuAdapter.ViewHolder>() {
        
        class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
            val img: ImageView = v.findViewById(R.id.adminItemImage)
            val name: TextView = v.findViewById(R.id.adminItemName)
            val price: TextView = v.findViewById(R.id.adminItemPrice)
            val editBtn: MaterialButton = v.findViewById(R.id.editItemButton)
            val deleteBtn: MaterialButton = v.findViewById(R.id.deleteItemButton)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.admin_menu_item, parent, false)
            return ViewHolder(v)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = items[position]
            holder.name.text = item.name
            holder.price.text = "KSh ${String.format("%.2f", item.price)}"
            Glide.with(holder.itemView.context).load(item.imageUrl).into(holder.img)
            holder.editBtn.setOnClickListener { onAction(item, "edit") }
            holder.deleteBtn.setOnClickListener { onAction(item, "delete") }
        }

        override fun getItemCount() = items.size
    }
}
