import java.sql.*;
import java.util.Scanner;

public class StudentManager {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n1. Add Student");
            System.out.println("2. View Students");
            System.out.println("3. Update Student");
            System.out.println("4. Delete Student");
            System.out.println("5. Exit");
            System.out.print("Enter choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // clear the newline

            switch (choice) {
                case 1 -> addStudent();
                case 2 -> viewStudents();
                case 3 -> updateStudent();
                case 4 -> deleteStudent();
                case 5 -> {
                    System.out.println("Goodbye!");
                    System.exit(0);
                }
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private static void addStudent() {
        try (Connection conn = DBConnection.getConnection()) {
            System.out.print("Enter name: ");
            String name = scanner.nextLine();
            System.out.print("Enter age: ");
            int age = scanner.nextInt();
            scanner.nextLine(); // clear newline
            System.out.print("Enter major: ");
            String major = scanner.nextLine();

            String sql = "INSERT INTO students (name, age, major) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setInt(2, age);
            stmt.setString(3, major);

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Student added successfully!");
            } else {
                System.out.println("Failed to add student.");
            }
        } catch (SQLException e) {
            System.out.println("Error adding student:");
            e.printStackTrace();
        }
    }

    private static void viewStudents() {
        try (Connection conn = DBConnection.getConnection()) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM students");

            System.out.println("\n--- Students ---");
            while (rs.next()) {
                System.out.printf("ID: %d | Name: %s | Age: %d | Major: %s%n",
                        rs.getInt("id"), rs.getString("name"),
                        rs.getInt("age"), rs.getString("major"));
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving students:");
            e.printStackTrace();
        }
    }

    private static void updateStudent() {
        try (Connection conn = DBConnection.getConnection()) {
            System.out.print("Enter student ID to update: ");
            int id = scanner.nextInt();
            scanner.nextLine(); // clear newline

            System.out.print("New name: ");
            String name = scanner.nextLine();
            System.out.print("New age: ");
            int age = scanner.nextInt();
            scanner.nextLine(); // clear newline
            System.out.print("New major: ");
            String major = scanner.nextLine();

            String sql = "UPDATE students SET name = ?, age = ?, major = ? WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setInt(2, age);
            stmt.setString(3, major);
            stmt.setInt(4, id);

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Student updated successfully!");
            } else {
                System.out.println("Student ID not found.");
            }
        } catch (SQLException e) {
            System.out.println("Error updating student:");
            e.printStackTrace();
        }
    }

    private static void deleteStudent() {
        try (Connection conn = DBConnection.getConnection()) {
            System.out.print("Enter student ID to delete: ");
            int id = scanner.nextInt();
            scanner.nextLine(); // clear newline

            String sql = "DELETE FROM students WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Student deleted successfully!");
            } else {
                System.out.println("Student ID not found.");
            }
        } catch (SQLException e) {
            System.out.println("Error deleting student:");
            e.printStackTrace();
        }
    }
}
