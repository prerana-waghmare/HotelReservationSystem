# Grand Stay - Advanced Hotel Management System

## Project Overview

The Grand Stay - Advanced Hotel Management System is a Java-based desktop graphical user interface (GUI) application built using Java Swing. 

It helps users manage hotel room inventories, simulate secure guest room reservations with various payment options, monitor live room vacancies, and maintain guest booking registries with persistent file storage.

## Features

- Room Directory & Showcase: View room categories (Standard Room, Deluxe Room, Executive Suite) with detailed pricing, amenities, and dedicated image cards loaded directly from the desktop (standard.jpg, deluxe.jpg, suite.jpg).
- Live Available Rooms Monitor: Dedicated navigation tab showing real-time capacity, currently occupied rooms, available vacancies, and occupancy status rates.
- Book & Pay Simulator: Secure guest reservation form featuring guest name, 10-digit phone number validation, number of nights, automated price calculation, and a multi-option payment gateway simulation (Credit/Debit Card, UPI/NetBanking, Cash at Counter).
- Registered Guests & Bookings Management: View all active bookings in a comprehensive table, track unique booking IDs (GS-XXXXX), and cancel reservations to automatically free up room inventory.
- Persistent Data Storage: Automatically saves and loads all active booking records to a local text file (hotel_bookings_data.txt).

## Technologies Used

- Java
- Java Swing & AWT (JFrame, JTabbedPane, JTable, JComboBox, JTextField, Custom Graphics)
- Object-Oriented Programming (OOP)
- Java File I/O (BufferedReader, PrintWriter)
- Collections Framework (ArrayList)

## How to Run the Project

Step 1: Prepare Room Images

Ensure you have three images named standard.jpg, deluxe.jpg, and suite.jpg saved on your Desktop so the application can load them automatically into the room cards.

Step 2: Compile the program

Open the terminal in the project folder and execute:

javac HotelReservationSystem.java

Step 3: Run the program

java HotelReservationSystem

## Sample Test Data & Workflow

1. Explore Rooms: Navigate to the "Room Directory" tab to check out room amenities and visual cards.
2. Check Vacancy: Open the "Available Rooms" tab to see live inventory counts.
3. Make a Booking: Go to the "Book & Pay Simulator" tab and enter:
   - Guest Full Name: Aarav Sharma
   - Phone Number: 9876543210
   - Room Category: Deluxe - Rs. 2500/night
   - Number of Nights: 2
4. Authorize Payment: Click "Proceed to Payment & Confirm", select a payment option (e.g., UPI / NetBanking), and finalize to generate your unique Booking ID (e.g., GS-48291).
5. Manage Bookings: Switch to the "Registered Guests & Bookings" tab to view your active reservation record or cancel it if needed.

## Internship

This project was created as part of the Java Programming Internship.

## Author

Prerana Pradip Waghmare