package project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GUIDesign {
    private JFrame formFrame;
    private JTextField nameField, roomNumberField;
    private JComboBox<String> hostelComboBox;
    private JCheckBox breakfastCheckBox, lunchCheckBox, snacksCheckBox, dinnerCheckBox;
    private JSpinner dateSpinner;

    private static final String DB_URL = "jdbc:mysql://localhost:3306/messmanagment";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "Lalgopal@2004";

    public GUIDesign() {
        formFrame = new JFrame("Mess Skip Form");
        formFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        formFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        formFrame.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel titleLabel = new JLabel("Mess Skip Form");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        formFrame.add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridy++;
        gbc.gridx = 0;
        formFrame.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        nameField = new JTextField(20);
        formFrame.add(nameField, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        formFrame.add(new JLabel("Hostel Name:"), gbc);
        gbc.gridx = 1;
        hostelComboBox = new JComboBox<>(new String[]{"NHRSCL", "Pahune", "Mehman", "Stanza"});
        formFrame.add(hostelComboBox, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        formFrame.add(new JLabel("Room Number:"), gbc);
        gbc.gridx = 1;
        roomNumberField = new JTextField(20);
        formFrame.add(roomNumberField, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        formFrame.add(new JLabel("Date of Skipping Meal:"), gbc);
        gbc.gridx = 1;

        dateSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor editor = new JSpinner.DateEditor(dateSpinner, "dd/MM/yyyy");
        dateSpinner.setEditor(editor);
        dateSpinner.setValue(new Date());
        formFrame.add(dateSpinner, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        formFrame.add(new JLabel("Select Meals to Skip (Cost):"), gbc);

        JPanel mealPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        breakfastCheckBox = new JCheckBox("Breakfast (₹25)");
        lunchCheckBox = new JCheckBox("Lunch (₹40)");
        snacksCheckBox = new JCheckBox("Snacks (₹20)");
        dinnerCheckBox = new JCheckBox("Dinner (₹40)");
        mealPanel.add(breakfastCheckBox);
        mealPanel.add(lunchCheckBox);
        mealPanel.add(snacksCheckBox);
        mealPanel.add(dinnerCheckBox);

        gbc.gridx = 1;
        formFrame.add(mealPanel, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        JButton submitButton = new JButton("Submit");
        submitButton.setPreferredSize(new Dimension(150, 50));
        submitButton.setFont(new Font("Arial", Font.BOLD, 18));
        submitButton.addActionListener(e -> submitForm());

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setPreferredSize(new Dimension(150, 50));
        cancelButton.setFont(new Font("Arial", Font.BOLD, 18));
        cancelButton.addActionListener(e -> formFrame.dispose());

        buttonPanel.add(submitButton);
        buttonPanel.add(cancelButton);

        formFrame.add(buttonPanel, gbc);

        formFrame.setVisible(true);
    }

    private void submitForm() {
        String name = nameField.getText();
        String roomNumber = roomNumberField.getText();
        String hostel = (String) hostelComboBox.getSelectedItem();
        Date selectedDate = (Date) dateSpinner.getValue();
        String date = new SimpleDateFormat("yyyy-MM-dd").format(selectedDate);

        boolean breakfast = breakfastCheckBox.isSelected();
        boolean lunch = lunchCheckBox.isSelected();
        boolean snacks = snacksCheckBox.isSelected();
        boolean dinner = dinnerCheckBox.isSelected();

        int refund = 0;
        if (breakfast) refund += 25;
        if (lunch) refund += 40;
        if (snacks) refund += 20;
        if (dinner) refund += 40;

        if (name.isEmpty() || roomNumber.isEmpty()) {
            JOptionPane.showMessageDialog(formFrame, "Name and Room Number are mandatory.", "Missing Fields", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!selectedDate.after(new Date())) {
            JOptionPane.showMessageDialog(formFrame, "Please select a future date.", "Invalid Date", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!(breakfast || lunch || snacks || dinner)) {
            JOptionPane.showMessageDialog(formFrame, "At least one meal must be selected.", "Missing Meals", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "INSERT INTO meal_skip (name, skip_date, hostel_name, room_number, breakfast, lunch, snacks, dinner, refunded_amount) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, date);
            preparedStatement.setString(3, hostel);
            preparedStatement.setString(4, roomNumber);
            preparedStatement.setBoolean(5, breakfast);
            preparedStatement.setBoolean(6, lunch);
            preparedStatement.setBoolean(7, snacks);
            preparedStatement.setBoolean(8, dinner);
            preparedStatement.setBigDecimal(9, BigDecimal.valueOf(refund));

            int rows = preparedStatement.executeUpdate();

            if (rows > 0) {
                JOptionPane.showMessageDialog(formFrame, "Form Submitted Successfully!\nRefunded Amount: ₹" + refund);
            } else {
                JOptionPane.showMessageDialog(formFrame, "Submission Failed. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(formFrame, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new GUIDesign();
    }
}
