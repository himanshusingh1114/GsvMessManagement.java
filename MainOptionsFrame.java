package project;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class MainOptionsFrame {
    JFrame optionsFrame;

    public MainOptionsFrame() {
        optionsFrame = new JFrame("GSV Mess");
        optionsFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        optionsFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel logoLabel = new JLabel();
        ImageIcon logoIcon = new ImageIcon("/Users/sambhavverma/Desktop/hl.png");
        Image scaledImage = logoIcon.getImage().getScaledInstance(400, 400, Image.SCALE_SMOOTH);
        logoIcon = new ImageIcon(scaledImage);
        logoLabel.setIcon(logoIcon);
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel headingLabel = new JLabel("GSV Mess Management System", SwingConstants.CENTER);
        headingLabel.setFont(new Font("Arial", Font.BOLD, 40));
        headingLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        headerPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        headerPanel.add(logoLabel);
        headerPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        headerPanel.add(headingLabel);
        headerPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        optionsFrame.add(headerPanel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.anchor = GridBagConstraints.CENTER;

        // Create buttons with padding
        JButton messSkipButton = createPaddedButton("Mess Skip", 200, 50);
        messSkipButton.addActionListener(e -> new GUIDesign());

        JButton viewStudentDataButton = createPaddedButton("View Student Data", 250, 50);
        viewStudentDataButton.addActionListener(e -> new StudentDataForm());

        JButton complainButton = createPaddedButton("Complaint/Suggestion", 250, 50);
        complainButton.addActionListener(e -> new ComplaintForm());

        JButton messMenuButton = createPaddedButton("Mess Menu", 200, 50);
        messMenuButton.addActionListener(e -> openPDF());

        JButton allStudentsButton = createPaddedButton("All Students", 200, 50);
        allStudentsButton.addActionListener(e -> displayAllStudents());

        gbc.gridy = 0;
        buttonPanel.add(messSkipButton, gbc);
        gbc.gridy++;
        buttonPanel.add(viewStudentDataButton, gbc);
        gbc.gridy++;
        buttonPanel.add(complainButton, gbc);
        gbc.gridy++;
        buttonPanel.add(messMenuButton, gbc);
        gbc.gridy++;
        buttonPanel.add(allStudentsButton, gbc);

        optionsFrame.add(buttonPanel, BorderLayout.CENTER);

        optionsFrame.getContentPane().add(Box.createRigidArea(new Dimension(10, 10)), BorderLayout.WEST);
        optionsFrame.getContentPane().add(Box.createRigidArea(new Dimension(10, 10)), BorderLayout.EAST);

        optionsFrame.setVisible(true);
    }

    // Method to create a button with increased padding
    private JButton createPaddedButton(String text, int width, int height) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(width, height));
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setMargin(new Insets(10, 20, 10, 20)); // Top, Left, Bottom, Right padding
        return button;
    }

    private void openPDF() {
        String filePath = "/Users/sambhavverma/Desktop/Mess Menu @November.pdf";
        File file = new File(filePath);
        if (file.exists()) {
            try {
                Desktop.getDesktop().open(file);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error opening the file!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "File not found!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void displayAllStudents() {
        SwingUtilities.invokeLater(() -> {
            // Create JFrame for table
            JFrame frame = new JFrame("All Students Data");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setSize(800, 600);

            // Create JTable and JScrollPane
            JTable table = new JTable();
            JScrollPane scrollPane = new JScrollPane(table);
            frame.add(scrollPane);

            // Load CSV data into JTable
            String csvFilePath = "/Users/sambhavverma/Desktop/GSV BOYS - Mess Data.csv"; // Update with your CSV file path
            try {
                DefaultTableModel model = readCSVToTableModel(csvFilePath);
                table.setModel(model);
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Error loading CSV file: " + e.getMessage());
            }

            frame.setVisible(true);
        });
    }

    private static DefaultTableModel readCSVToTableModel(String filePath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        DefaultTableModel tableModel = new DefaultTableModel();

        String line;
        boolean isFirstLine = true;

        while ((line = reader.readLine()) != null) {
            String[] rowData = line.split(","); // Split by commas
            if (isFirstLine) {
                // Add column names
                for (String columnName : rowData) {
                    tableModel.addColumn(columnName.trim());
                }
                isFirstLine = false;
            } else {
                // Add row data
                tableModel.addRow(rowData);
            }
        }

        reader.close();
        return tableModel;
    }

    public static void main(String[] args) {
        new MainOptionsFrame();
    }
}
