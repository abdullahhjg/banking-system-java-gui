 🏦 Java Banking System GUI

This is a simple **Banking System** with a graphical user interface (GUI) built using Java Swing. It allows users to add customers, perform transactions (deposit/withdraw), apply for loans, process loans, and detect fraud-like behaviors. The system is designed to demonstrate basic object-oriented programming, GUI handling, and data management in Java.

---

✨ Features

- ➕ **Add New Customers**
- 📋 **Display All Customers**
- 💰 **Deposit & Withdraw Money**
- 💳 **Apply for Loans**
- 🔁 **Process Loan Queue**
- 🔐 **Basic Fraud Detection**  
  - Detects large transactions  
  - Flags excessive activity in a short time
- 📑 **View Transaction History (placeholder)**
- 🖥️ **Clean and Interactive GUI**

---

🧱 Technologies Used

- Java 8+
- Java Swing (GUI toolkit)
- AWT (for layout management)
- Core Java Libraries (Collections, Map, Queue, etc.)

---

🧮 Fraud Detection Logic

The system contains a basic fraud detection mechanism:

- Flags transactions **over $10,000**
- Tracks each customer's activity, and flags if they perform **more than 5 transactions within a minute**

---


