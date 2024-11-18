package project;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;

public class ComplaintForm {
    JFrame complaintFrame;
    JTextArea complaintArea;
    JTextField nameField, rollNumberField, searchField;
    JTable suggestionTable;
    DefaultTableModel tableModel;

    // Database connection details
    private static final String DB_URL = "jdbc:mysql://localhost:3306/messmanagment";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "Lalgopal@2004";

    // Sample list of student names
    private static final List<String> studentNames = List.of(
            "Ansh Sharma 23AI012", "Bansode Vikrant Vinod 23AI018", "Rahul Panwar 23AI041", "Anurag Jaiswal 23AI013", "Anurag Tiwari 23AI014", "Yeetraditya Dhadhiya 23AI057", "Abhimanyu Srivastava 23AI001", "Harsh Vardhan 23AI025", "Sanskar Agarwal 23AI047", "Himanshu 23AI026", "Kabeer Kumar 23AI029", "Sambhav 23AI045", "Bhagirth Mahesh Sherkhane 23CE010", "Bharat 23CE011", "Rajeshkumar 23CE020", "Ankit Gupta 23AI010", "Asmit Sharma 23AI017", "Chakshu Vashisth 23AI019", "Dev Yadav 23AI021", "Harphool Singh Bajdoliya 23AI023", "Pradeep Kumar 23AI038", "Gopal Patidar 23ME014", "Ojas Singh 23CE017", "Parth Sidhu 23EC040", "Aryan Kumar 23ME007", "Aryan Thakur 23ME008", "Gaddam Venkata Sai Nishant 23ME013", "Ojasva Jadon 23AI035", "Shivam Kumar 23AI050", "Vishvendra 23AI055", "Ishan 23AI027", "Rishav Pal 23AI044", "Sayantan Mandal 23AI048", "Ashish Kumar 23AI016", "Harsh Raj 23AI024", "Pratyush Rai 23AI039", "Ayush Kumar Singh 23EC019", "Karthik Gurubaran 23EE025", "Srijan Aditya Singh 23ME034", "Ankit Anand 23CE006", "Pranav Choudhary 23EC042", "Vaibhav Sharma 23ME037", "Aditya Anand 23EE007", "Alok Kumar Banjare 23CE003", "Money Singh 23ME020", "Alamuru Lakshmidhar Reddy 23ME003", "Hitesh Kumar 23ME018", "Akshat Singh 23EC006", "Aditya Acharya 23AI005", "Hemant Saini 23EC031", "Vibhanshu Choudhary 23EE048", "Aayush Agarwal 23EE001", "Aryan Vaish 23EC015", "Paras Agarwal 23EE034", "Adarsh Raj 23ME001", "Himanshu Singh 23ME017", "Himanshu Xalxo 23EC032", "Arjun Kumar Yadav 23ME006", "Dibyajyoti Sahoo 23ME012", "Pratik Ajay Shelke 23EC043", "Abhishek Kumar 23AI003", "Kanishk Kushwaha 23AI030", "Karmveer Kumar 23AI031", "Piyush Sharma 23AI037", "Jaykishan Bhadu 23AI028", "Aditya Prakash 23CE002", "L K Mukesh 23CE014", "Vasamsetti Chakradhar 23EE046", "Chaman Kumar 23AI020", "Vikash Kumar 23AI054", "Vivek Rajak 23AI056", "Abhishek Roop Singh 23AI004", "Arup Das 23AI015", "Brahm Dev Tripathi 23EC021", "Karri Komalrao 23EE024", "Nimmakayala Shanmukha Kumar 23AI034", "Sanapala Hema Raj Varabhushan 23AI046", "Arpit Rauthan 23CE007", "Devprakash Chandrakar 23ME011", "Shivansh Gupta 23AI051", "Pushpak Kumar Behera 2340026", "Rishab Singh 2340027", "Harshit Daksh 2340015", "Shivam Pathak 2340030", "Suvendu Patra 2340035", "Adith Rakesh 2340003", "Aditya Kumar 2340004", "Aniruddhsinh Dodia 2340007", "Aditya Kumar 23EE008", "Mohmmad Athar Parwez 23EC035", "Naman Narayan 23EC038", "Akshat Singh 23ME002", "Ashish Meena 23EC016", "Suwarn 23EC054", "Durgesh Ratna 23EC024", "Prashant Dilip Ingole 23EC033", "Sumit Kumar 23EC052", "Hardik Singh 23EC028", "Raghvendra Dutt 23CE019", "Roshan Gupta 23EC046", "Ashutosh Kumar 2280010", "Nitesh Kumar Singh 2280034", "Devesh Tripathi 2220006", "Rishabh Asuthkar 2220016", "Abhay Shrinag 2220001", "Ujjwal Parashar 2220021", "Abhivyakt Trivedi 2220002", "Harshit Lohani 2220009", "Shivamkumar Arvindkumar Singh 2220018", "Deependra Yadav 2220022", "Dheeraj Kushwaha 2220007", "Shivanand 2220019", "Samvas Mukherjee 2160030", "Sumit Mondal 2160038", "Diwakar Yadav 2160015", "Shivraj Choudhary 2160036", "Aditya Upadhyay 2160004", "Rudraksha Verma 2160027", "Prateek Yadav 2170028", "Saurabh Kumar Singh 2180049", "Pushkar Aryan 2180041", "Tirtha Pattanayak 2180057", "Aaditya Raj 2180001", "Shivam Yadav 2180052", "Abhinav Raj 2180004", "Saurav Kumar 2180050", "Ekansh Malav 2180021", "Himanshu Yadav 2180024", "Prakash Kumar 2170026", "Prateek Prasoon 2170027", "Aharnish Anand 2160005", "Darshan Agrawal 2160013", "Pulkit Tembhre 2160024", "Vaibhav Chandrakar 2160040", "Aryan Raju Awasthi 2160010", "Mohit Prakash Srivastava 2160018", "Pratik Jena 2160022", "Sayan Saurabh Tripathi 2160034", "Harpreet Singh 2180022", "Sumit Yadav 2170038", "Adit Chawla 2180006", "Dheeraj Kumar 2180018", "Deepak Kumar 2170011", "Rohan Kumar 2170033", "Aashish Kumar 2170001", "Sourabh Saini 2160037", "Animesh Kumar Singh 2180015", "Ankit Kumar 2170003", "Abhinav Singhal 2180005", "Krishna Kumar Swarnkar 2180030", "Satya Kamal Sahu 2160032", "Sudhanshu Mani Tiwari 2180054", "Granth Kumar 2170015", "Harish Sonwani 2170016", "Aditya Pratap Singh 2180009", "Ajeet Kumar 2180011", "Aditya Aman 2180007", "Satyam Singh 2180048", "Aditya Raj 2180010", "Akshat Sthapak 2180012", "Anand Anil Kumar Singh 2160007", "Parshant 2160019", "Aditya Kumar Singh 2160002", "Ankit Raj 2160008", "Utkarsh Sinha 2170039", "Vedant Gupta 2170040", "Vrushabh Sheetal Chougule 2170042", "Kartikey Tiwari 2170019", "Chetan Ajaypal Singh 2170010", "Kshitij Jain 2160017", "Aman Chrungu 2160006", "Harsh Chauhan 2180023", "Agnibha Karmakar 2170002", "Vidyabhushan Kumar 2160041", "Aditya Ranjan 2160003", "Atul Krishan 2160009", "Abhishek Verma 2160001", "Prashant Kumar 2160021", "Pulkit Kumar 2160023", "Tanishq Srivastava 2160039", "Anmol Vinod Mehrotra 2170004", "Ronit Vyas 2170034", "Aman Kumar Mishra 2180013", "Balveer Jat 2180017", "Rohan Maheshwari 2180045", "Vishal Pandey 2180059", "Mokal Yash Balu 2180037", "Piyush Soni 2170014", "Shubham Agarwal 2170012", "Aryan Negi 2170008", "Aaditya Raghav 2170036", "Ankit Kumar Gupta 2170007", "Ravi Kumar 2170018", "Pankaj Yadav 2160025", "Gaurav Thakur 2160031", "Shubham Yadav 2180036", "Nishant Soni 2180038", "Shubham Singh 2180051", "Mayank Sharma 2170029", "Tanuj Soni 2180049", "Tanay Aggarwal 2180020", "Sandeep Kumar 2180014", "Vishal Kumar 2180035", "Shashank Kumar 2170035", "Satyam Gupta 2170030", "Yash Pratap Singh 2170005", "Utkarsh Yadav 2160035", "Harsh Yadav 2180040", "Krishnan R 2170021", "Amit Kumar 2170022", "Tushar Malik 2160045", "Krishna Patel 2160029", "Sandeep Kumar 2160043", "Sourabh Kumar Yadav 2160033", "Ashish Kumar 2170031", "Mayur Arvind Deshmukh 2170041", "Sushant Patel 2170043", "Shivansh Dubey 2180044"
    );

    public ComplaintForm() {
        complaintFrame = new JFrame("Complaint Form");
        complaintFrame.setSize(800, 700);
        complaintFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Search Field
        gbc.gridy = 0;
        gbc.gridx = 0;
        panel.add(new JLabel("Search Student:"), gbc);

        searchField = new JTextField(20);
        gbc.gridx = 1;
        panel.add(searchField, gbc);

        tableModel = new DefaultTableModel();
        tableModel.addColumn("Names");
        suggestionTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(suggestionTable);
        scrollPane.setPreferredSize(new Dimension(250, 150));
        gbc.gridy = 1;
        panel.add(scrollPane, gbc);

        gbc.gridy = 2;
        gbc.gridx = 0;
        panel.add(new JLabel("Selected Name:"), gbc);

        nameField = new JTextField(20);
        nameField.setEditable(false);
        gbc.gridx = 1;
        panel.add(nameField, gbc);

        gbc.gridy = 3;
        gbc.gridx = 0;
        panel.add(new JLabel("Roll Number:"), gbc);

        rollNumberField = new JTextField(20);
        gbc.gridx = 1;
        panel.add(rollNumberField, gbc);

        gbc.gridy = 4;
        gbc.gridx = 0;
        panel.add(new JLabel("Complaint:"), gbc);

        complaintArea = new JTextArea(5, 20);
        gbc.gridx = 1;
        panel.add(new JScrollPane(complaintArea), gbc);

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> submitComplaint());

        JButton viewButton = new JButton("View Complaints");
        viewButton.addActionListener(e -> showLoginPage());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(submitButton);
        buttonPanel.add(viewButton);

        complaintFrame.add(panel, BorderLayout.CENTER);
        complaintFrame.add(buttonPanel, BorderLayout.SOUTH);
        complaintFrame.setVisible(true);

        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { updateSuggestions(); }
            public void removeUpdate(DocumentEvent e) { updateSuggestions(); }
            public void changedUpdate(DocumentEvent e) { updateSuggestions(); }
        });

        suggestionTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                nameField.setText((String) tableModel.getValueAt(suggestionTable.getSelectedRow(), 0));
            }
        });
    }

    private void updateSuggestions() {
        tableModel.setRowCount(0);
        String text = searchField.getText().toLowerCase();
        studentNames.stream()
                .filter(name -> name.toLowerCase().startsWith(text))
                .forEach(name -> tableModel.addRow(new Object[]{name}));
    }

    private void submitComplaint() {
        String name = nameField.getText();
        String rollNumber = rollNumberField.getText();
        String complaint = complaintArea.getText();

        if (name.isEmpty() || rollNumber.isEmpty() || complaint.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please fill all fields");
            return;
        }

        try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "INSERT INTO complaints_suggestions (name, roll_number, complaint_or_suggestion) VALUES (?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, name);
            ps.setString(2, rollNumber);
            ps.setString(3, complaint);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "Submitted Successfully");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Database error: " + e.getMessage());
        }
    }

    private void showLoginPage() {
        JDialog dialog = new JDialog(complaintFrame, "Admin Login", true);
        dialog.setSize(600, 600);
        dialog.setLocationRelativeTo(null); // Center the dialog on the screen

        // Use GridBagLayout for alignment
        JPanel loginPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Add spacing between components

        JLabel userLabel = new JLabel("Username:");
        JTextField userField = new JTextField(15);

        JLabel passLabel = new JLabel("Password:");
        JPasswordField passField = new JPasswordField(15);

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(e -> {
            if ("himanshu@2004".equals(userField.getText()) &&
                    "admin@123".equals(new String(passField.getPassword()))) {
                dialog.dispose();
                showComplaints();
            } else {
                JOptionPane.showMessageDialog(dialog, "Invalid Credentials");
            }
        });

        // Arrange components in two columns
        gbc.anchor = GridBagConstraints.LINE_END; // Align labels to the right
        gbc.gridx = 0;
        gbc.gridy = 0;
        loginPanel.add(userLabel, gbc);

        gbc.gridy = 1;
        loginPanel.add(passLabel, gbc);

        gbc.anchor = GridBagConstraints.LINE_START; // Align text fields to the left
        gbc.gridx = 1;
        gbc.gridy = 0;
        loginPanel.add(userField, gbc);

        gbc.gridy = 1;
        loginPanel.add(passField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2; // Span the button across both columns
        gbc.anchor = GridBagConstraints.CENTER; // Center the button
        loginPanel.add(loginButton, gbc);

        dialog.add(loginPanel);
        dialog.setVisible(true);
    }

    private void showComplaints() {
        JFrame frame = new JFrame("Complaints");
        frame.setSize(500, 400);
        DefaultTableModel model = new DefaultTableModel();
        JTable table = new JTable(model);
        model.addColumn("ID");
        model.addColumn("Name");
        model.addColumn("Roll Number");
        model.addColumn("Complaint");

        try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM complaints_suggestions");
            while (rs.next()) {
                model.addRow(new Object[]{rs.getInt("id"), rs.getString("name"), rs.getString("roll_number"), rs.getString("complaint_or_suggestion")});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error fetching data");
        }

        frame.add(new JScrollPane(table));
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new ComplaintForm();
    }
}