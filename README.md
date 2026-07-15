# Banking Management System

## Overview
A console-based Banking Management System developed using Core Java 
with JDBC and MySQL database integration. This is an upgraded version 
of the banking system where all account data, transactions, and loan 
details are persistently stored in a MySQL database. The system loads 
all existing account data from the database on startup, ensuring no 
data is lost between sessions.

## Features
- Dual account types — Savings and Current with different business rules
- Secure 4-digit PIN-based authentication for all transactions
- Deposit with real-time database update
- Withdrawal with minimum balance validation
  (Savings: ₹500 minimum, Current: ₹1000 minimum)
- Fund transfer between accounts with real-time DB sync for both accounts
- Loan request with salary and balance eligibility check
- Savings account loan capped at ₹5,00,000
- Loan repayment with multiple loan tracking
- Complete transaction history stored and retrieved from database
- Account data loaded from MySQL on every startup — no data loss
- Account creation and deletion with validation

## Technologies Used
- Core Java
- JDBC — Java Database Connectivity
- MySQL Database
- OOP Concepts — Abstract class, Inheritance, Method Overriding
- Java Collections — HashMap, ArrayList

## OOP Concepts Used
- Abstract class (Bank) with abstract methods
- Inheritance — saveBank and currentBank extend Bank
- Method Overriding — withdrawal and loan logic differs per account type
- Encapsulation — balance and pin managed via access modifiers

## Database Tables
- saving — stores savings account pin, name, balance
- current — stores current account pin, name, balance
- loan — stores pin and loan amount for all active loans
- transaction_details — logs every transaction for all accounts

## How to Run
1. Clone the repository
2. Create MySQL database named 'bank'
3. Create saving, current, loan, and transaction_details tables
4. Add MySQL connector JAR to lib folder
5. Update DB password in code if needed
6. Compile and run Main.javame