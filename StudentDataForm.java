package project;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.Font;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.*;

class StudentDataForm {
    private JFrame dataFrame;
    private JTable studentTable;
    private JTextField searchField;
    private JButton searchButton, downloadButton;
    private JLabel totalRefundLabel;

    private static final String DB_URL = "jdbc:mysql://localhost:3306/messmanagment";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "Lalgopal@2004";

    public StudentDataForm() {
        dataFrame = new JFrame("View Student Data");
        dataFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        dataFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel topPanel = new JPanel(new BorderLayout());

        JLabel titleLabel = new JLabel("Student Data", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 25));
        topPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel searchPanel = new JPanel(new FlowLayout());

        JLabel searchLabel = new JLabel("Search by Name: ");
        searchField = new JTextField(20);
        searchButton = new JButton("Search");
        searchButton.addActionListener(e -> searchStudentData());

        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        totalRefundLabel = new JLabel("Total Refunded Amount: ₹0");
        totalRefundLabel.setFont(new Font("Arial", Font.BOLD, 18));
        searchPanel.add(totalRefundLabel);

        // Adding Download Button
        downloadButton = new JButton("Download PDF");
        downloadButton.addActionListener(e -> downloadTableAsPDF());
        searchPanel.add(downloadButton);

        topPanel.add(searchPanel, BorderLayout.SOUTH);

        studentTable = new JTable();
        initializeTable(); // Initialize table with no data initially

        JScrollPane scrollPane = new JScrollPane(studentTable);

        dataFrame.setLayout(new BorderLayout());
        dataFrame.add(topPanel, BorderLayout.NORTH);
        dataFrame.add(scrollPane, BorderLayout.CENTER);

        dataFrame.setVisible(true);
    }

    private void initializeTable() {
        String[] columnNames = {"Name", "Date of Skip", "Hostel Name", "Room Number", "Breakfast", "Lunch", "Snacks", "Dinner", "Refunded Amount"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        studentTable.setModel(tableModel);
    }

    private void loadStudentData(String searchName) {
        String[] columnNames = {"Name", "Date of Skip", "Hostel Name", "Room Number", "Breakfast", "Lunch", "Snacks", "Dinner", "Refunded Amount"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

        if (searchName == null || searchName.trim().isEmpty()) {
            JOptionPane.showMessageDialog(dataFrame, "Please enter a name to search.", "Warning", JOptionPane.WARNING_MESSAGE);
            studentTable.setModel(tableModel); // Clear the table if no name is provided
            totalRefundLabel.setText("Total Refunded Amount: ₹0");
            return;
        }

        String sql = "SELECT name, skip_date, hostel_name, room_number, breakfast, lunch, snacks, dinner, refunded_amount FROM meal_skip WHERE name = ?";

        BigDecimal totalRefunded = BigDecimal.ZERO;

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, searchName.trim()); // Exact match for the name

            ResultSet resultSet = statement.executeQuery();
            boolean dataFound = false;

            while (resultSet.next()) {
                dataFound = true;
                String name = resultSet.getString("name");
                String skipDate = resultSet.getString("skip_date");
                String hostelName = resultSet.getString("hostel_name");
                String roomNumber = resultSet.getString("room_number");
                boolean breakfast = resultSet.getBoolean("breakfast");
                boolean lunch = resultSet.getBoolean("lunch");
                boolean snacks = resultSet.getBoolean("snacks");
                boolean dinner = resultSet.getBoolean("dinner");
                BigDecimal refundedAmount = resultSet.getBigDecimal("refunded_amount");

                totalRefunded = totalRefunded.add(refundedAmount); // Sum the refund amounts

                Object[] row = {
                        name, skipDate, hostelName, roomNumber,
                        breakfast ? "Yes" : "No",
                        lunch ? "Yes" : "No",
                        snacks ? "Yes" : "No",
                        dinner ? "Yes" : "No",
                        "₹" + refundedAmount
                };
                tableModel.addRow(row);
            }

            if (!dataFound) {
                JOptionPane.showMessageDialog(dataFrame, "No data found for the given name.", "Information", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(dataFrame, "Error fetching data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        studentTable.setModel(tableModel);
        totalRefundLabel.setText("Total Refunded Amount: ₹" + totalRefunded); // Display the total refund amount
    }

    private void searchStudentData() {
        String searchName = searchField.getText();
        loadStudentData(searchName);
    }

    private void downloadTableAsPDF() {
        // Create a new document
        Document document = new Document();
        try {
            // Open a file chooser dialog to select the save location
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Save PDF");
            fileChooser.setSelectedFile(new java.io.File("student_data.pdf")); // Default filename

            int userSelection = fileChooser.showSaveDialog(dataFrame);
            if (userSelection != JFileChooser.APPROVE_OPTION) {
                return; // If the user cancels, return
            }

            // Get the selected file path
            String filePath = fileChooser.getSelectedFile().getAbsolutePath();

            // Ensure the file has a .pdf extension
            if (!filePath.endsWith(".pdf")) {
                filePath += ".pdf";
            }

            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            // Add the title
            document.add(new Paragraph("Student Data", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18)));
            document.add(Chunk.NEWLINE);

            // Create the table with the same number of columns as the JTable
            PdfPTable pdfTable = new PdfPTable(studentTable.getColumnCount());
            for (int i = 0; i < studentTable.getColumnCount(); i++) {
                pdfTable.addCell(studentTable.getColumnName(i)); // Add column headers
            }

            // Add rows to the table
            DefaultTableModel model = (DefaultTableModel) studentTable.getModel();
            for (int i = 0; i < model.getRowCount(); i++) {
                for (int j = 0; j < model.getColumnCount(); j++) {
                    pdfTable.addCell(model.getValueAt(i, j).toString());
                }
            }

            // Add the table to the document
            document.add(pdfTable);

            // Add the total refunded amount
            document.add(Chunk.NEWLINE);
            document.add(new Paragraph("Total Refunded Amount: ₹" + totalRefundLabel.getText().substring(23)));

            JOptionPane.showMessageDialog(dataFrame, "PDF downloaded successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (DocumentException | IOException e) {
            JOptionPane.showMessageDialog(dataFrame, "Error generating PDF: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            document.close();
        }
    }
}
