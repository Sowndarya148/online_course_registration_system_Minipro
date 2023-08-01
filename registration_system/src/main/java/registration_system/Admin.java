package registration_system;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Admin extends AbstractCourse {
    public Admin(String code, String name) {
        super(code, name);
    }

    
    @Override
    public void saveToDatabase() {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "INSERT INTO view_available_course (course_id, course_name) VALUES (?, ?)")) {

            preparedStatement.setString(1, getCode());
            preparedStatement.setString(2, getName());
            preparedStatement.executeUpdate();

            System.out.println("Course added successfully.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    
    @Override
    public void removeFromDatabase() {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "DELETE FROM view_available_course WHERE course_id = ?")) {

            preparedStatement.setString(1, getCode());
            preparedStatement.executeUpdate();

            System.out.println("Course removed successfully.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
