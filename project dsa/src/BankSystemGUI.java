import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class BankSystemGUI {
    static int customerIdCounter = 1;
    static Map<Integer, Customer> customers = new HashMap<>();
    static Queue<Loan> loanQueue = new LinkedList<>();
    static FraudCheck fraudCheck = new FraudCheck();
    static JFrame frame;
    static JTextArea displayArea;
    static class FraudCheck {
        private static final double MAX_AMOUNT = 10000.0;
        private static final int MAX_TRANSACTIONS = 5;
        private static final long TIME_LIMIT = 60000; // 1 minute
        private Map<Integer, LinkedList<Long>> customerTransactions = new HashMap<>();

        boolean checkFraud(int customerId, double amount) {
            long currentTime = System.currentTimeMillis();

            // Check for large transactions
            if (amount > MAX_AMOUNT) {
                System.out.println("Fraud Alert: Large transaction detected for Customer ID " + customerId);
                return true;
            }

            // Check for too many transactions in a short time
            LinkedList<Long> times = customerTransactions.computeIfAbsent(customerId, k -> new LinkedList<>());
            times.add(currentTime);

            // Remove old transactions
            while (!times.isEmpty() && currentTime - times.peek() > TIME_LIMIT) {
                times.poll();
            }

            if (times.size() > MAX_TRANSACTIONS) {
                System.out.println("Fraud Alert: Too many transactions for Customer ID " + customerId);
                return true;
            }

            return false;
        }
    }
    static void viewTransactions() {
        try {
            int id = Integer.parseInt(JOptionPane.showInputDialog("Enter customer ID:"));
            Customer customer = customers.get(id);
            if (customer != null) {
                displayArea.append("Transaction history for " + customer.name + " (ID: " + id + "):\n");
                // Placeholder for transaction history (Modify if transactions are being recorded)
                displayArea.append("No transactions recorded yet.\n");
            } else {
                displayArea.append("Customer not found.\n");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Invalid input.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }



    public static void main(String[] args) {
        frame = new JFrame("Banking System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 600);
        frame.setLayout(new BorderLayout());

        displayArea = new JTextArea();
        displayArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(displayArea);
        frame.add(scrollPane, BorderLayout.CENTER);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2, 5, 5));

        JButton addCustomerBtn = new JButton("Add Customer");
        JButton showCustomersBtn = new JButton("Show Customers");
        JButton depositBtn = new JButton("Deposit Money");
        JButton withdrawBtn = new JButton("Withdraw Money");
        JButton loanBtn = new JButton("Apply for Loan");
        JButton processLoanBtn = new JButton("Process Loan");
        JButton transactionsBtn = new JButton("View Transactions");
        JButton exitBtn = new JButton("Exit");

        panel.add(addCustomerBtn);
        panel.add(showCustomersBtn);
        panel.add(depositBtn);
        panel.add(withdrawBtn);
        panel.add(loanBtn);
        panel.add(processLoanBtn);
        panel.add(transactionsBtn);
        panel.add(exitBtn);

        frame.add(panel, BorderLayout.SOUTH);

        addCustomerBtn.addActionListener(e -> addCustomer());
        showCustomersBtn.addActionListener(e -> showAllCustomers());
        depositBtn.addActionListener(e -> depositMoney());
        withdrawBtn.addActionListener(e -> withdrawMoney());
        loanBtn.addActionListener(e -> applyLoan());
        processLoanBtn.addActionListener(e -> processLoan());
        transactionsBtn.addActionListener(e -> viewTransactions());
        exitBtn.addActionListener(e -> System.exit(0));

        frame.setVisible(true);
    }

    static void addCustomer() {
        String name = JOptionPane.showInputDialog("Enter customer name:");
        if (name == null || name.trim().isEmpty()) return;
        String balanceStr = JOptionPane.showInputDialog("Enter starting balance:");
        try {
            double balance = Double.parseDouble(balanceStr);
            Customer customer = new Customer(customerIdCounter++, name, balance);
            customers.put(customer.id, customer);
            displayArea.append("Customer added: " + customer + "\n");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Invalid balance amount.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    static void showAllCustomers() {
        displayArea.setText("");
        if (customers.isEmpty()) {
            displayArea.append("No customers yet.\n");
        } else {
            for (Customer customer : customers.values()) {
                displayArea.append(customer + "\n");
            }
        }
    }

    static void depositMoney() {
        try {
            int id = Integer.parseInt(JOptionPane.showInputDialog("Enter customer ID:"));
            double amount = Double.parseDouble(JOptionPane.showInputDialog("Enter deposit amount:"));
            Customer customer = customers.get(id);
            if (customer != null) {
                customer.deposit(amount);
                displayArea.append("Deposited $" + amount + " to Customer ID " + id + "\n");
            } else {
                displayArea.append("Customer not found.\n");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Invalid input.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    static void withdrawMoney() {
        try {
            int id = Integer.parseInt(JOptionPane.showInputDialog("Enter customer ID:"));
            double amount = Double.parseDouble(JOptionPane.showInputDialog("Enter withdrawal amount:"));
            Customer customer = customers.get(id);
            if (customer != null && customer.withdraw(amount)) {
                displayArea.append("Withdrawn $" + amount + " from Customer ID " + id + "\n");
            } else {
                displayArea.append("Not enough balance or customer not found.\n");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Invalid input.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    static void applyLoan() {
        try {
            int id = Integer.parseInt(JOptionPane.showInputDialog("Enter customer ID:"));
            double amount = Double.parseDouble(JOptionPane.showInputDialog("Enter loan amount:"));
            Customer customer = customers.get(id);
            if (customer != null) {
                loanQueue.add(new Loan(customer, amount));
                displayArea.append("Loan applied for $" + amount + " by Customer ID " + id + "\n");
            } else {
                displayArea.append("Customer not found.\n");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Invalid input.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    static void processLoan() {
        if (!loanQueue.isEmpty()) {
            Loan loan = loanQueue.poll();
            displayArea.append("Processing loan of $" + loan.amount + " for " + loan.customer.name + "\n");
        } else {
            displayArea.append("No loans to process.\n");
        }
    }

    static class Customer {
        int id;
        String name;
        double balance;

        Customer(int id, String name, double balance) {
            this.id = id;
            this.name = name;
            this.balance = balance;
        }

        void deposit(double amount) {
            balance += amount;
        }

        boolean withdraw(double amount) {
            if (balance >= amount) {
                balance -= amount;
                return true;
            }
            return false;
        }

        public String toString() {
            return "ID: " + id + ", Name: " + name + ", Balance: $" + balance;
        }
    }

    static class Loan {
        Customer customer;
        double amount;

        Loan(Customer customer, double amount) {
            this.customer = customer;
            this.amount = amount;
        }
    }
}
