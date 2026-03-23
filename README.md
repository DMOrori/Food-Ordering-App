# Food Ordering App 🍔🍕

A comprehensive Android application for ordering food, featuring a user-friendly interface for customers and an administrative panel for managing menu items and orders.

## 🚀 Features

### For Users
*   **Authentication**: Secure User Registration and Login systems.
*   **Password Recovery**: Forgot password functionality to reset credentials.
*   **Menu Browsing**: View a dynamic list of available food items with descriptions, prices, and images.
*   **Shopping Cart**: Add multiple items to the cart, adjust quantities, and view the total cost.
*   **Checkout**: Seamless checkout process with order summary and payment method selection.
*   **Session Management**: Persistent login sessions for a better user experience.

### For Administrators
*   **Admin Login**: Dedicated secure login for administrators.
*   **Menu Management**: Add new food items to the menu database.
*   **Order Tracking**: View all incoming orders, customer details, and total revenue statistics.

## 🛠 Technologies Used
*   **Kotlin**: Primary programming language.
*   **SQLite**: Local database for storing users, menu items, and orders.
*   **Android Jetpack Components**:
    *   `AppCompatActivity`
    *   `RecyclerView` for efficient list rendering.
    *   `ConstraintLayout` for responsive UI design.
*   **Material Design**: Modern UI components (Buttons, TextFields, etc.).
*   **Glide**: Image loading and caching library.

## 📁 Project Structure
*   `MainActivity.kt`: Entry point for user authentication.
*   `MenuActivity.kt`: Main dashboard for browsing food items.
*   `CartActivity.kt`: Manages selected items before checkout.
*   `AdminPanelActivity.kt`: Administrative dashboard for orders and stats.
*   `DatabaseHelper.kt`: Handles all SQLite database operations.

## ⚙️ Installation & Setup
1.  Clone the repository:
    ```bash
    git clone https://github.com/yourusername/Food-Ordering-App.git
    ```
2.  Open the project in **Android Studio**.
3.  Sync the project with Gradle files.
4.  Run the application on an emulator or a physical Android device (API 21+).

## 📝 License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---
*Developed as a modern food delivery solution.*
