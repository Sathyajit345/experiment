import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.io.File;
import java.util.HashMap;
import javax.swing.UIManager.LookAndFeelInfo;
public class GymManagementSystem extends JFrame {
private JTextField memberIdField, memberNameField, memberAgeField, membershipFeeField;
private JButton addMemberButton, showMembersButton, deleteMemberButton, dietButton;
private JTextArea memberListArea;
public GymManagementSystem() {
try {
for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
if ("Nimbus".equals(info.getName())) {
UIManager.setLookAndFeel(info.getClassName());
break;
}
}
} catch (Exception e) {
e.printStackTrace();
}
setTitle("Gym Management System");
setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
setSize(600, 400);
getContentPane().setBackground(new Color(216, 216, 216));
String dataDirectory = "gymdata";
createDataDirectory(dataDirectory);
JPanel panel = new JPanel(new GridLayout(6, 2));
JLabel memberIdLabel = new JLabel("Member ID:");
JLabel memberNameLabel = new JLabel("Member Name:");
JLabel memberAgeLabel = new JLabel("Member Age:");
JLabel membershipFeeLabel = new JLabel("Membership Fee:");
memberIdField = new JTextField(10);
memberNameField = new JTextField(10);
memberAgeField = new JTextField(10);
membershipFeeField = new JTextField(10);
addMemberButton = new JButton("Add Member");
showMembersButton = new JButton("Show Members");
deleteMemberButton = new JButton("Delete Member");
dietButton = new JButton("Diet Plan");
memberListArea = new JTextArea();
memberListArea.setEditable(false);
JScrollPane scrollPane = new JScrollPane(memberListArea);
addMemberButton.setBackground(new Color(255, 165, 0));
showMembersButton.setBackground(new Color(108, 117, 125));
deleteMemberButton.setBackground(new Color(0x28A745));
dietButton.setBackground(new Color(0, 123, 255));
addMemberButton.setForeground(Color.white);
showMembersButton.setForeground(Color.white);
deleteMemberButton.setForeground(Color.white);
dietButton.setForeground(Color.white);
addMemberButton.addActionListener(new ActionListener() {
public void actionPerformed(ActionEvent e) {
addMember();
}
});
showMembersButton.addActionListener(new ActionListener() {
public void actionPerformed(ActionEvent e) {
showMembers();
}
});
deleteMemberButton.addActionListener(new ActionListener() {
public void actionPerformed(ActionEvent e) {
deleteMember();
}
});
dietButton.addActionListener(new ActionListener() {
public void actionPerformed(ActionEvent e) {
openDietaryPlanSuggestion();
}
});
panel.add(memberIdLabel);
panel.add(memberIdField);
panel.add(memberNameLabel);
panel.add(memberNameField);
panel.add(memberAgeLabel);
panel.add(memberAgeField);
panel.add(membershipFeeLabel);
panel.add(membershipFeeField);
panel.add(addMemberButton);
panel.add(showMembersButton);
panel.add(deleteMemberButton);
panel.add(dietButton);
add(panel, BorderLayout.NORTH);
add(scrollPane, BorderLayout.CENTER);
createMembersTable();
}
private void createMembersTable() {
try {
String jdbcURL = "jdbc:mysql://localhost:3306/gym";
String username = "root";
String password = "nks2005nks";
Connection connection = DriverManager.getConnection(jdbcURL, username, password);
Statement statement = connection.createStatement();
String createTableQuery = "CREATE TABLE IF NOT EXISTS members ("
+ "id VARCHAR(20) PRIMARY KEY,"
+ "name VARCHAR(255) NOT NULL,"
+ "age INT NOT NULL,"
+ "membership_fee DECIMAL(10, 2) NOT NULL"
+ ")";
statement.executeUpdate(createTableQuery);
statement.close();
connection.close();
} catch (SQLException ex) {
ex.printStackTrace();
JOptionPane.showMessageDialog(this, "Error creating 'members' table: " + ex.getMessage());
}
}
private void createDataDirectory(String directoryName) {
File directory = new File(directoryName);
if (!directory.exists()) {
if (directory.mkdir()) {
System.out.println("Data directory created successfully.");
} else {
System.err.println("Failed to create data directory.");
}
} else {
System.out.println("Data directory already exists.");
}
}
public void addMember() {
String memberId = memberIdField.getText();
String memberName = memberNameField.getText();
String memberAge = memberAgeField.getText();
String membershipFee = membershipFeeField.getText();
if (memberId.isEmpty() || memberName.isEmpty() || memberAge.isEmpty() ||
membershipFee.isEmpty()) {
JOptionPane.showMessageDialog(this, "Please fill in all fields.");
return;
}
try {
String jdbcURL = "jdbc:mysql://localhost:3306/gym";
String username = "root";
String password = "nks2005nks";
Connection connection = DriverManager.getConnection(jdbcURL, username, password);
connection.setAutoCommit(false);
String insertQuery = "INSERT INTO members (id, name, age, membership_fee) VALUES (?, ?, ?,
?)";
PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
preparedStatement.setString(1, memberId);
preparedStatement.setString(2, memberName);
preparedStatement.setString(3, memberAge);
preparedStatement.setString(4, membershipFee);
preparedStatement.executeUpdate();
connection.commit();
preparedStatement.close();
connection.close();
JOptionPane.showMessageDialog(this, "Member added successfully.");
} catch (SQLException ex) {
ex.printStackTrace();
JOptionPane.showMessageDialog(this, "Error adding member: " + ex.getMessage());
}
}
public void showMembers() {
memberListArea.setText("");
try {
String jdbcURL = "jdbc:mysql://localhost:3306/gym";
String username = "root";
String password = "nks2005nks";
Connection connection = DriverManager.getConnection(jdbcURL, username, password);
Statement statement = connection.createStatement();
ResultSet resultSet = statement.executeQuery("SELECT * FROM members");
while (resultSet.next()) {
String memberId = resultSet.getString("id");
String memberName = resultSet.getString("name");
String memberAge = resultSet.getString("age");
String membershipFee = resultSet.getString("membership_fee");
memberListArea.append("ID: " + memberId + ", Name: " + memberName + ", Age: " +
memberAge +
", Fee: $" + membershipFee + " per month\n");
}
resultSet.close();
statement.close();
connection.close();
} catch (SQLException ex) {
ex.printStackTrace();
JOptionPane.showMessageDialog(this, "Error retrieving members: " + ex.getMessage());
}
}
public void deleteMember() {
String memberId = memberIdField.getText();
if (memberId.isEmpty()) {
JOptionPane.showMessageDialog(this, "Please enter a Member ID to delete.");
return;
}
try {
String jdbcURL = "jdbc:mysql://localhost:3306/gym";
String username = "root";
String password = "nks2005nks";
Connection connection = DriverManager.getConnection(jdbcURL, username, password);
connection.setAutoCommit(false);
String deleteQuery = "DELETE FROM members WHERE id=?";
PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery);
preparedStatement.setString(1, memberId);
int rowsAffected = preparedStatement.executeUpdate();
if (rowsAffected == 1) {
connection.commit();
JOptionPane.showMessageDialog(this, "Member with ID " + memberId + " deleted
successfully.");
} else {
connection.rollback();
JOptionPane.showMessageDialog(this, "Member with ID " + memberId + " not found.");
}
preparedStatement.close();
connection.close();
} catch (SQLException ex) {
ex.printStackTrace();
JOptionPane.showMessageDialog(this, "Error deleting member: " + ex.getMessage());
}
}
private void openDietaryPlanSuggestion() {
EventQueue.invokeLater(() -> {
DietaryPlanSuggestionCombined dietGUI = new DietaryPlanSuggestionCombined();
dietGUI.setVisible(true);
});
}
public static void main(String[] args) {
EventQueue.invokeLater(() -> {
GymManagementSystem app = new GymManagementSystem();
app.setVisible(true);
});
}
}
class DietaryPlanSuggestionCombined extends JFrame {
private JTextField ageTextField;
private JComboBox<String> goalComboBox;
private JTextArea resultTextArea;
private static final String DB_URL = "jdbc:mysql://localhost:3306/gym";
private static final String DB_USER = "root";
private static final String DB_PASSWORD = "nks2005nks";
public DietaryPlanSuggestionCombined() {
super("Dietary Plan Suggestion");
try {
for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
if ("Nimbus".equals(info.getName())) {
UIManager.setLookAndFeel(info.getClassName());
break;
}
}
} catch (Exception e) {
e.printStackTrace();
}
setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
setSize(400, 300);
JPanel mainPanel = new JPanel();
mainPanel.setLayout(null);
JLabel ageLabel = new JLabel("Enter your age:");
ageLabel.setBounds(20, 20, 120, 20);
mainPanel.add(ageLabel);
ageTextField = new JTextField();
ageTextField.setBounds(160, 20, 100, 20);
mainPanel.add(ageTextField);
JLabel goalLabel = new JLabel("Select your goal:");
goalLabel.setBounds(20, 60, 120, 20);
mainPanel.add(goalLabel);
String[] goals = {"weight_loss", "muscle_gain"};
goalComboBox = new JComboBox<>(goals);
goalComboBox.setBounds(160, 60, 100, 20);
mainPanel.add(goalComboBox);
JButton generateButton = new JButton("Generate Plan");
generateButton.setBounds(20, 100, 150, 30);
mainPanel.add(generateButton);
resultTextArea = new JTextArea();
resultTextArea.setBounds(20, 150, 350, 100);
resultTextArea.setEditable(false);
mainPanel.add(resultTextArea);
generateButton.addActionListener(new ActionListener() {
@Override
public void actionPerformed(ActionEvent e) {
generateDietaryPlan();
}
});
getContentPane().add(mainPanel);
}
private void generateDietaryPlan() {
String ageText = ageTextField.getText();
String goal = goalComboBox.getSelectedItem().toString();
try {
int age = Integer.parseInt(ageText);
HashMap<String, String> dietaryPlan = retrieveDietaryPlan(age, goal);
String resultText = buildResultText(age, goal, dietaryPlan);
resultTextArea.setText(resultText);
} catch (NumberFormatException e) {
resultTextArea.setText("Please enter a valid age.");
}
}
private HashMap<String, String> retrieveDietaryPlan(int age, String goal) {
HashMap<String, String> plan = new HashMap<>();
try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER,
DB_PASSWORD)) {
String query = "SELECT * FROM dietary_plans WHERE age_group = ? AND goal = ?";
PreparedStatement preparedStatement = connection.prepareStatement(query);
preparedStatement.setInt(1, calculateAgeGroup(age));
preparedStatement.setString(2, goal);
ResultSet resultSet = preparedStatement.executeQuery();
if (resultSet.next()) {
plan.put("Breakfast", resultSet.getString("breakfast"));
plan.put("Lunch", resultSet.getString("lunch"));
plan.put("Dinner", resultSet.getString("dinner"));
plan.put("Snacks", resultSet.getString("snacks"));
}
} catch (SQLException e) {
e.printStackTrace();
}
return plan;
}
private int calculateAgeGroup(int age) {
int ageGroup = 1;
if (age >= 18 && age <= 30) {
ageGroup = 1;
} else if (age >= 31 && age <= 45) {
ageGroup = 2;
} else if (age >= 46 && age <= 60) {
ageGroup = 3;
} else if (age > 60) {
ageGroup = 4;
} else {
ageGroup = 0;
}
return ageGroup;
}
private String buildResultText(int age, String goal, HashMap<String, String> dietaryPlan) {
StringBuilder resultText = new StringBuilder();
resultText.append("Dietary Plan for Age ").append(age).append(" Goal: ").append(goal).append("\n");
resultText.append("Breakfast: ").append(dietaryPlan.get("Breakfast")).append("\n");
resultText.append("Lunch: ").append(dietaryPlan.get("Lunch")).append("\n");
resultText.append("Dinner: ").append(dietaryPlan.get("Dinner")).append("\n");
resultText.append("Snacks: ").append(dietaryPlan.get("Snacks")).append("\n");
return resultText.toString();
}
public static void main(String[] args) {
SwingUtilities.invokeLater(() -> {
DietaryPlanSuggestionCombined gui = new DietaryPlanSuggestionCombined();
gui.setVisible(true);
});
}
}
