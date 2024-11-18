package project;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class CSVToJTable {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Create JFrame
            JFrame frame = new JFrame("CSV Data Viewer");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
}