# Banking Management System

## Overview
A console-based Banking Management System developed using Core Java. 
This project simulates real-world banking operations with two account 
types — Savings and Current — each with their own rules and restrictions. 
All account data is managed in-memory using Java Collections.

## Features
- Dual account types — Savings and Current with different business rules
- Secure 4-digit PIN-based authentication for all transactions
- Deposit and withdrawal with minimum balance validation
  (Savings: ₹500 minimum, Current: ₹1000 minimum)
- Fund transfer between accounts with balance verification
- Loan request with salary and balance eligibility check
- Loan repayment with multiple loan tracking
- Complete transaction history for every account
- Account creation and deletion with validation

## Technologies Used
- Core Java
- OOP Concepts — Abstract class, Inheritance, Method Overriding
- Java Collections — HashMap, ArrayList

## OOP Concepts Used
- Abstract class (Bank) with abstract methods
- Inheritance — saveBank and currentBank extend Bank
- Method Overriding — withdrawal and loan logic differs per account type
- Encapsulation — balance and pin protected using access modifiers

## How to Run
1. Clone the repository
2. Open in VS Code or any Java IDE
3. Compile and run Main.java
4. Follow the console menu to operate
