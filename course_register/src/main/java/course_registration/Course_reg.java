package course_registration;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Course_reg {
    private List<Course> availableCourses;

    public Course_reg() {
        availableCourses = new ArrayList<Course>();
        loadAvailableCoursesFromDatabase();
    }
    
    
    private void loadAvailableCoursesFromDatabase() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // Load the JDBC driver class
            try (Connection connection = DBConnection.getConnection();
                 Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery("SELECT * FROM view_available_course")) {

                while (resultSet.next()) {
                    String courseCode = resultSet.getString("course_code");
                    String courseName = resultSet.getString("course_name");
                    availableCourses.add(new Course(courseCode, courseName));
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    private void registerStudentToDatabase(String studentId, String studentName, String emailId, String password) {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "INSERT INTO registration (student_id, student_name, email_id, password) VALUES (?, ?, ?, ?)")) {

            preparedStatement.setString(1, studentId);
            preparedStatement.setString(2, studentName);
            preparedStatement.setString(3, emailId);
            preparedStatement.setString(4, password);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void registerCourseToDatabase(String studentId, String courseCode) {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection
                     .prepareStatement("INSERT INTO registered_course (studentId, course_code) VALUES (?, ?)")) {

            preparedStatement.setString(1, studentId);
            preparedStatement.setString(2, courseCode);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void removeCourseFromDatabase(String courseCode) {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection
                     .prepareStatement("DELETE FROM view_available_course WHERE course_code = ?")) {

            preparedStatement.setString(1, courseCode);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void viewAvailableCourses() {
        System.out.println("Available Courses:");
        for (Course course : availableCourses) {
        	 System.out.println(course.getCode() + ": " + course.getName());
        }
    }

    public void registerStudent(String studentId, String studentName, String emailId, String password) {
        registerStudentToDatabase(studentId, studentName, emailId, password);
        System.out.println("Student registered successfully.");
    }

    public void registerCourse(String studentId, String courseCode) {
        // Check if the student is already registered for the course
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection
                     .prepareStatement("SELECT * FROM registered_course WHERE studentId = ? AND course_code = ?")) {

            preparedStatement.setString(1, studentId);
            preparedStatement.setString(2, courseCode);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                System.out.println("Student is already registered for this course.");
            } else {
                // Register the student for the course
                registerCourseToDatabase(studentId, courseCode);
                System.out.println("Course registered successfully.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
 
    public void viewRegisteredCourses(String studentId) {
        System.out.println("Registered Courses for Student " + studentId + ":");
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection
                     .prepareStatement("SELECT course_code FROM registered_course WHERE studentId = ?")) {

            preparedStatement.setString(1, studentId);
            ResultSet resultSet = preparedStatement.executeQuery();

            boolean hasCourses = false;
            while (resultSet.next()) {
                String courseCode = resultSet.getString("course_code");
                System.out.println(courseCode);
                hasCourses = true;
            }

            if (!hasCourses) {
                System.out.println("No courses registered for this student.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addCourse(String courseCode, String courseName) {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection
                     .prepareStatement("INSERT INTO view_available_course (course_code, course_name) VALUES (?, ?)")) {

            preparedStatement.setString(1, courseCode);
            preparedStatement.setString(2, courseName);
            preparedStatement.executeUpdate();
            availableCourses.add(new Course(courseCode, courseName));

            System.out.println("Course added successfully.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeCourse(String course_code) {
        boolean courseExists = false;
        for (Course course : availableCourses) {
            if (course.getCode().equals(course_code)) {
                courseExists = true;
                break;
            }
        }

        if (courseExists) {
            removeCourseFromDatabase(course_code);
            availableCourses.removeIf(course -> course.getCode().equals(course_code));
            System.out.println("Course removed successfully.");
        } else {
            System.out.println("Course not found.");
        }
    }

    public static void main(String[] args) {
        Course_reg system = new Course_reg();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            showMenu();
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    system.viewAvailableCourses();
                    break;
                case "2":
                    System.out.print("Enter your student ID: ");
                    String studentId = scanner.nextLine();
                    System.out.print("Enter your student Name: ");
                    String studentName = scanner.nextLine();
                    System.out.print("Enter your email ID: ");
                    String emailId = scanner.nextLine();
                    System.out.print("Enter your password: ");
                    String password = scanner.nextLine();
                    system.registerStudent(studentId, studentName, emailId, password);
                    break;

                case "3":
                    System.out.print("Enter your student ID: ");
                    String studentIdToRegister = scanner.nextLine();
                    System.out.print("Enter the course code to register: ");
                    String courseCodeToRegister = scanner.nextLine();
                    system.registerCourse(studentIdToRegister, courseCodeToRegister);
                    break;
                case "4":
                    System.out.print("Enter your student ID: ");
                    String studentIdToView = scanner.nextLine();
                    system.viewRegisteredCourses(studentIdToView);
                    break;
                case "5":
                    System.out.print("Enter the admin password: ");
                    String adminPassword = scanner.nextLine();
                    if ("admin".equals(adminPassword)) {
                        System.out.print("Enter the course code: ");
                        String newCourseCode = scanner.nextLine();
                        System.out.print("Enter the course name: ");
                        String newCourseName = scanner.nextLine();
                        system.addCourse(newCourseCode, newCourseName);
                    } else {
                        System.out.println("Incorrect password. Access denied.");
                    }
                    break;
                case "6":
                    System.out.print("Enter the admin password: ");
                    String adminPasswordToRemove = scanner.nextLine();
                    if ("admin".equals(adminPasswordToRemove)) {
                        System.out.print("Enter the course code to remove: ");
                        String courseCodeToRemove = scanner.nextLine();
                        system.removeCourse(courseCodeToRemove);
                    } else {
                        System.out.println("Incorrect password. Access denied.");
                    }
                    break;
                case "7":
                    System.out.println("Goodbye!");
                    scanner.close();
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private static void showMenu() {
        System.out.println("Welcome to Course Registration System");
        System.out.println("1. View available courses");
        System.out.println("2. Register as a new student");
        System.out.println("3. Register for a course");
        System.out.println("4. View registered courses");
        System.out.println("5. Add a new course (for administrators)");
        System.out.println("6. Remove a course (for administrators)");
        System.out.println("7. Exit");
        System.out.print("Enter your choice: ");
    }
}
