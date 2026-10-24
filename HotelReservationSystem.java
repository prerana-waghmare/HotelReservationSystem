import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;

public class HotelReservationSystem extends JFrame {

    private static final String DATA_FILE = "hotel_bookings_data.txt";

    // Global Collections & Inventory Counters
    private ArrayList<Booking> bookingList;
    
    // Fixed Inventory Limits per Category
    private int standardTotal = 10, deluxeTotal = 8, suiteTotal = 5;

    // UI Tables & Models
    private DefaultTableModel bookingTableModel;
    private DefaultTableModel dedicatedInventoryModel;
    
    // Form Components
    private JTextField nameField, phoneField, daysField;
    private JComboBox<String> roomCategoryBox;
    private JLabel inventoryStatusLabel;

    public HotelReservationSystem() {
        setTitle("Grand Stay - Advanced Hotel Management System");
        setSize(1100, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Load data from text file
        bookingList = loadBookingsFromFile();

        // Main Navigation Tabs with Professional Styling & Icons
        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabbedPane.setBackground(new Color(230, 235, 245));
        tabbedPane.setForeground(new Color(30, 50, 80));

        // Navigation Bar Tabs
        tabbedPane.addTab(" Room Directory", createHomeIcon(), createInventoryPanel());
        tabbedPane.addTab(" Available Rooms", createAvailabilityIcon(), createDedicatedAvailabilityPanel());
        tabbedPane.addTab(" Book & Pay Simulator", createBookingIcon(), createBookingPanel());
        tabbedPane.addTab(" Registered Guests & Bookings", createManagementIcon(), createManagementPanel());

        add(tabbedPane);
        setVisible(true);
    }

    // ================= 1. ROOM DIRECTORY PANEL =================
    private JPanel createInventoryPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setBackground(new Color(245, 247, 250));

        JLabel title = new JLabel("Grand Stay - Room Showcase Directory", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(new Color(30, 55, 90));
        panel.add(title, BorderLayout.NORTH);

        JPanel cardsGrid = new JPanel(new GridLayout(1, 3, 15, 0));
        cardsGrid.setOpaque(false);

        // Automatically point to Desktop path for images
        String desktopPath = System.getProperty("user.home") + File.separator + "Desktop" + File.separator;

        cardsGrid.add(createRoomCard("Standard Room", "Rs. 1,500 / night", "Queen bed, Free Wi-Fi, AC, Attached Bathroom.", "Ideal for budget travelers.", desktopPath + "standard.jpg", new Color(70, 130, 180)));
        cardsGrid.add(createRoomCard("Deluxe Room", "Rs. 2,500 / night", "King-size bed, City View, Mini-bar, Smart TV.", "Perfect for couples and families.", desktopPath + "deluxe.jpg", new Color(46, 139, 87)));
        cardsGrid.add(createRoomCard("Executive Suite", "Rs. 4,500 / night", "Private Jacuzzi, Living area, 24/7 Butler, Balcony.", "Ultimate luxury experience.", desktopPath + "suite.jpg", new Color(184, 134, 11)));

        panel.add(cardsGrid, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createRoomCard(String name, String price, String amenities, String desc, String imgPath, Color themeColor) {
        JPanel card = new JPanel(null);
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(210, 220, 235), 1, true),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        // Room Header Title & Price
        JLabel nameLbl = new JLabel(name);
        nameLbl.setBounds(15, 12, 280, 25);
        nameLbl.setFont(new Font("Segoe UI", Font.BOLD, 17));
        nameLbl.setForeground(new Color(25, 65, 100));
        card.add(nameLbl);

        JLabel priceLbl = new JLabel(price);
        priceLbl.setBounds(15, 38, 280, 22);
        priceLbl.setFont(new Font("Segoe UI", Font.BOLD, 15));
        priceLbl.setForeground(themeColor);
        card.add(priceLbl);

        // Description & Amenities Text Area
        JTextArea descArea = new JTextArea(desc + "\n\nAmenities:\n" + amenities);
        descArea.setBounds(15, 68, 290, 110);
        descArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        descArea.setEditable(false);
        descArea.setBackground(Color.WHITE);
        card.add(descArea);

        // Room Image Section Placed Directly Below Room Details
        Component imageContainer;
        File file = new File(imgPath);
        if (file.exists()) {
            ImageIcon icon = new ImageIcon(new ImageIcon(imgPath).getImage().getScaledInstance(290, 185, Image.SCALE_SMOOTH));
            JLabel imgLabel = new JLabel(icon);
            imgLabel.setBounds(15, 185, 290, 185);
            imgLabel.setBorder(BorderFactory.createLineBorder(new Color(200, 210, 225), 1));
            imageContainer = imgLabel;
        } else {
            // Fallback preview panel if file isn't found on Desktop
            JPanel previewPanel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    
                    GradientPaint gp = new GradientPaint(0, 0, themeColor, getWidth(), getHeight(), themeColor.darker());
                    g2d.setPaint(gp);
                    g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);

                    g2d.setColor(Color.WHITE);
                    g2d.setFont(new Font("Segoe UI", Font.BOLD, 13));
                    FontMetrics fm = g2d.getFontMetrics();
                    String subtitle = "📷 " + name + " Image Not Found";
                    g2d.drawString(subtitle, (getWidth() - fm.stringWidth(subtitle)) / 2, getHeight() / 2 - 5);
                    
                    g2d.setFont(new Font("Segoe UI", Font.PLAIN, 10));
                    String hint = "(Ensure file is on Desktop)";
                    g2d.drawString(hint, (getWidth() - g2d.getFontMetrics().stringWidth(hint)) / 2, getHeight() / 2 + 15);
                }
            };
            previewPanel.setBounds(15, 185, 290, 185);
            previewPanel.setBorder(BorderFactory.createLineBorder(new Color(200, 210, 225), 1));
            imageContainer = previewPanel;
        }
        card.add(imageContainer);

        return card;
    }

    // ================= 2. DEDICATED "AVAILABLE ROOMS" TAB =================
    private JPanel createDedicatedAvailabilityPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBorder(new EmptyBorder(25, 25, 25, 25));
        panel.setBackground(new Color(245, 247, 250));

        JLabel title = new JLabel("Real-Time Available Rooms & Inventory Monitor", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(new Color(30, 55, 90));
        panel.add(title, BorderLayout.NORTH);

        String[] columns = {"Room Category", "Total Capacity", "Currently Occupied", "Available Vacancies", "Availability Status"};
        dedicatedInventoryModel = new DefaultTableModel(columns, 0);
        JTable dedicatedTable = new JTable(dedicatedInventoryModel);
        dedicatedTable.setRowHeight(38);
        dedicatedTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        dedicatedTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        updateDedicatedAvailabilityData();

        JScrollPane scrollPane = new JScrollPane(dedicatedTable);
        
        JPanel centerContainer = new JPanel(new BorderLayout());
        centerContainer.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(200, 210, 225)), 
            "Live Room Vacancy Directory", 0, 0, 
            new Font("Segoe UI", Font.BOLD, 13), new Color(50, 70, 100)
        ));
        centerContainer.setOpaque(false);
        centerContainer.add(scrollPane, BorderLayout.CENTER);
        
        panel.add(centerContainer, BorderLayout.CENTER);

        // Refresh Button
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        actionPanel.setOpaque(false);
        JButton refreshBtn = new JButton("🔄 Refresh Inventory Status");
        refreshBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        refreshBtn.setBackground(new Color(30, 90, 150));
        refreshBtn.setForeground(Color.WHITE);
        refreshBtn.setFocusPainted(false);
        refreshBtn.addActionListener(e -> {
            updateDedicatedAvailabilityData();
            JOptionPane.showMessageDialog(this, "Room inventory updated successfully!", "Live Status", JOptionPane.INFORMATION_MESSAGE);
        });
        actionPanel.add(refreshBtn);
        panel.add(actionPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void updateDedicatedAvailabilityData() {
        if (dedicatedInventoryModel == null) return;
        dedicatedInventoryModel.setRowCount(0);

        int stdBooked = getBookedCountForType("Standard");
        int delBooked = getBookedCountForType("Deluxe");
        int suiteBooked = getBookedCountForType("Suite");

        int stdAvail = standardTotal - stdBooked;
        int delAvail = deluxeTotal - delBooked;
        int suiteAvail = suiteTotal - suiteBooked;

        dedicatedInventoryModel.addRow(new Object[]{"Standard Room", standardTotal, stdBooked, stdAvail, (stdAvail > 0 ? "🟢 Available" : "🔴 Fully Booked")});
        dedicatedInventoryModel.addRow(new Object[]{"Deluxe Room", deluxeTotal, delBooked, delAvail, (delAvail > 0 ? "🟢 Available" : "🔴 Fully Booked")});
        dedicatedInventoryModel.addRow(new Object[]{"Executive Suite", suiteTotal, suiteBooked, suiteAvail, (suiteAvail > 0 ? "🟢 Available" : "🔴 Fully Booked")});
    }

    // ================= 3. BOOKING & PAYMENT PANEL =================
    private JPanel createBookingPanel() {
        JPanel panel = new JPanel(null);
        panel.setBackground(new Color(245, 247, 250));

        JLabel heading = new JLabel("Secure Room Reservation Form");
        heading.setBounds(300, 20, 450, 30);
        heading.setFont(new Font("Segoe UI", Font.BOLD, 22));
        heading.setForeground(new Color(30, 55, 90));
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(heading);

        addLabel(panel, "Guest Full Name:", 150, 90);
        nameField = addTextField(panel, 370, 90);

        addLabel(panel, "Phone Number (10 digits):", 150, 150);
        phoneField = addTextField(panel, 370, 150);

        addLabel(panel, "Select Room Category:", 150, 210);
        String[] rooms = {"Standard - Rs. 1500/night", "Deluxe - Rs. 2500/night", "Suite - Rs. 4500/night"};
        roomCategoryBox = new JComboBox<>(rooms);
        roomCategoryBox.setBounds(370, 210, 320, 35);
        roomCategoryBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        panel.add(roomCategoryBox);

        addLabel(panel, "Number of Nights:", 150, 270);
        daysField = addTextField(panel, 370, 270);

        inventoryStatusLabel = new JLabel("Check availability in the 'Available Rooms' tab before booking.");
        inventoryStatusLabel.setBounds(370, 320, 450, 30);
        inventoryStatusLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        inventoryStatusLabel.setForeground(new Color(100, 110, 125));
        panel.add(inventoryStatusLabel);

        JButton payBookButton = new JButton("Proceed to Payment & Confirm");
        payBookButton.setBounds(370, 380, 220, 45);
        payBookButton.setBackground(new Color(46, 139, 87));
        payBookButton.setForeground(Color.WHITE);
        payBookButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        payBookButton.setFocusPainted(false);
        panel.add(payBookButton);

        JButton clearBtn = new JButton("Clear Form");
        clearBtn.setBounds(600, 380, 90, 45);
        clearBtn.setBackground(new Color(180, 50, 50));
        clearBtn.setForeground(Color.WHITE);
        clearBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        clearBtn.setFocusPainted(false);
        panel.add(clearBtn);

        payBookButton.addActionListener(e -> initiatePaymentAndBooking());
        clearBtn.addActionListener(e -> clearForm());

        return panel;
    }

    private void initiatePaymentAndBooking() {
        String name = nameField.getText().trim();
        String phone = phoneField.getText().trim();

        if (name.isEmpty() || phone.isEmpty() || daysField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all required fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!phone.matches("[0-9]{10}")) {
            JOptionPane.showMessageDialog(this, "Please enter a valid 10-digit phone number.", "Invalid Phone", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int nights = Integer.parseInt(daysField.getText().trim());
            if (nights <= 0) {
                JOptionPane.showMessageDialog(this, "Nights must be greater than 0.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int index = roomCategoryBox.getSelectedIndex();
            String roomType = (index == 0) ? "Standard" : (index == 1) ? "Deluxe" : "Suite";
            int pricePerNight = (index == 0) ? 1500 : (index == 1) ? 2500 : 4500;

            int bookedCount = getBookedCountForType(roomType);
            int totalAllowed = (index == 0) ? standardTotal : (index == 1) ? deluxeTotal : suiteTotal;

            if (bookedCount >= totalAllowed) {
                JOptionPane.showMessageDialog(this, "Sorry! All " + roomType + " rooms are currently fully booked.", "Inventory Full", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int totalAmount = nights * pricePerNight;

            String[] paymentOptions = {"Credit / Debit Card", "UPI / NetBanking", "Cash at Counter"};
            int paymentChoice = JOptionPane.showOptionDialog(this,
                    "Guest: " + name + "\nRoom Type: " + roomType + "\nTotal Bill: Rs. " + totalAmount + "\n\nSelect Payment Gateway:",
                    "Secure Payment Simulator",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, paymentOptions, paymentOptions[0]);

            if (paymentChoice == JOptionPane.CLOSED_OPTION) return;
            String paymentMethod = paymentOptions[paymentChoice];

            int confirmPay = JOptionPane.showConfirmDialog(this,
                    "Authorizing Rs. " + totalAmount + " via " + paymentMethod + "...\nClick OK to finalize booking.",
                    "Payment Authorization", JOptionPane.OK_CANCEL_OPTION);

            if (confirmPay == JOptionPane.OK_OPTION) {
                String bookingId = "GS-" + (10000 + (int)(Math.random() * 90000));
                Booking newBooking = new Booking(bookingId, name, phone, roomType, nights, totalAmount, paymentMethod);
                
                bookingList.add(newBooking);
                saveBookingsToFile();
                refreshAllTables();

                JOptionPane.showMessageDialog(this,
                        "Reservation Confirmed Successfully! 🎉\nBooking ID: " + bookingId + "\nAssigned Room Type: " + roomType,
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                clearForm();
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid numeric value for nights.", "Format Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ================= 4. MANAGEMENT & REGISTERED GUESTS PANEL =================
    private JPanel createManagementPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));
        panel.setBackground(new Color(245, 247, 250));

        String[] columns = {"Booking ID", "Guest Full Name", "Phone Number", "Room Type", "Nights", "Total Paid (Rs.)", "Payment Mode"};
        bookingTableModel = new DefaultTableModel(columns, 0);
        JTable bookingTable = new JTable(bookingTableModel);
        bookingTable.setRowHeight(26);
        bookingTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        refreshBookingTableData();

        JScrollPane scrollPane = new JScrollPane(bookingTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel bottomBar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomBar.setOpaque(false);

        JButton cancelBtn = new JButton("Cancel Reservation & Free Room");
        cancelBtn.setBackground(new Color(180, 50, 50));
        cancelBtn.setForeground(Color.WHITE);
        cancelBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        cancelBtn.setFocusPainted(false);
        cancelBtn.addActionListener(e -> cancelSelectedBooking());

        bottomBar.add(cancelBtn);
        panel.add(bottomBar, BorderLayout.SOUTH);

        return panel;
    }

    private void cancelSelectedBooking() {
        String inputId = JOptionPane.showInputDialog(this, "Enter Booking ID to Cancel & Free Room:");
        if (inputId == null || inputId.trim().isEmpty()) return;

        boolean found = false;
        for (int i = 0; i < bookingList.size(); i++) {
            if (bookingList.get(i).id.equalsIgnoreCase(inputId.trim())) {
                bookingList.remove(i);
                found = true;
                break;
            }
        }

        if (found) {
            saveBookingsToFile();
            refreshAllTables();
            JOptionPane.showMessageDialog(this, "Reservation cancelled successfully. Inventory updated.");
        } else {
            JOptionPane.showMessageDialog(this, "Booking ID not found.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ================= CUSTOM NAVIGATION ICONS =================
    private Icon createHomeIcon() {
        BufferedImage image = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = image.createGraphics();
        g2.setColor(new Color(30, 90, 150));
        g2.fillRect(2, 6, 12, 9);
        int[] xPoints = {1, 8, 15};
        int[] yPoints = {7, 1, 7};
        g2.fillPolygon(xPoints, yPoints, 3);
        g2.dispose();
        return new ImageIcon(image);
    }

    private Icon createAvailabilityIcon() {
        BufferedImage image = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = image.createGraphics();
        g2.setColor(new Color(34, 139, 34));
        g2.fillRect(2, 2, 12, 12);
        g2.setColor(Color.WHITE);
        g2.fillRect(4, 5, 8, 2);
        g2.fillRect(4, 9, 5, 2);
        g2.dispose();
        return new ImageIcon(image);
    }

    private Icon createBookingIcon() {
        BufferedImage image = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = image.createGraphics();
        g2.setColor(new Color(40, 140, 70));
        g2.fillRoundRect(1, 2, 14, 12, 3, 3);
        g2.setColor(Color.WHITE);
        g2.fillRect(3, 5, 10, 2);
        g2.fillRect(3, 9, 6, 2);
        g2.dispose();
        return new ImageIcon(image);
    }

    private Icon createManagementIcon() {
        BufferedImage image = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = image.createGraphics();
        g2.setColor(new Color(180, 100, 30));
        g2.fillOval(4, 1, 8, 8);
        g2.fillArc(1, 10, 14, 10, 0, 180);
        g2.dispose();
        return new ImageIcon(image);
    }

    // ================= HELPERS & FILE I/O =================
    private int getBookedCountForType(String type) {
        int count = 0;
        for (Booking b : bookingList) {
            if (b.roomType.equalsIgnoreCase(type)) count++;
        }
        return count;
    }

    private void refreshAllTables() {
        refreshBookingTableData();
        updateDedicatedAvailabilityData();
    }

    private void refreshBookingTableData() {
        if (bookingTableModel == null) return;
        bookingTableModel.setRowCount(0);
        for (Booking b : bookingList) {
            bookingTableModel.addRow(new Object[]{b.id, b.name, b.phone, b.roomType, b.nights, b.totalAmount, b.paymentMethod});
        }
    }

    private void saveBookingsToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(DATA_FILE))) {
            for (Booking b : bookingList) {
                writer.println(b.id + "," + b.name + "," + b.phone + "," + b.roomType + "," + b.nights + "," + b.totalAmount + "," + b.paymentMethod);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "File I/O Error saving data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private ArrayList<Booking> loadBookingsFromFile() {
        ArrayList<Booking> list = new ArrayList<>();
        File file = new File(DATA_FILE);
        if (!file.exists()) return list;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 7) {
                    Booking b = new Booking(parts[0], parts[1], parts[2], parts[3], Integer.parseInt(parts[4]), Integer.parseInt(parts[5]), parts[6]);
                    list.add(b);
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("No prior records loaded.");
        }
        return list;
    }

    private void addLabel(JPanel panel, String text, int x, int y) {
        JLabel label = new JLabel(text);
        label.setBounds(x, y, 200, 35);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(new Color(40, 45, 55));
        panel.add(label);
    }

    private JTextField addTextField(JPanel panel, int x, int y) {
        JTextField field = new JTextField();
        field.setBounds(x, y, 320, 35);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        panel.add(field);
        return field;
    }

    private void clearForm() {
        nameField.setText("");
        phoneField.setText("");
        daysField.setText("");
        roomCategoryBox.setSelectedIndex(0);
    }

    // ================= OOP BOOKING DATA MODEL =================
    public static class Booking {
        String id, name, phone, roomType, paymentMethod;
        int nights, totalAmount;

        public Booking(String id, String name, String phone, String roomType, int nights, int totalAmount, String paymentMethod) {
            this.id = id;
            this.name = name;
            this.phone = phone;
            this.roomType = roomType;
            this.nights = nights;
            this.totalAmount = totalAmount;
            this.paymentMethod = paymentMethod;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(HotelReservationSystem::new);
    }
}