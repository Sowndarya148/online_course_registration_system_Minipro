package course_registration;


import java.util.ArrayList;
import java.util.List;

public class Student {
    private String studentId;
    private List<String> registeredCourses;

    public Student(String studentId) {
        this.studentId = studentId;
        this.registeredCourses = new ArrayList<String>();
    }

    public String getStudentId() {
        return studentId;
    }

    public List<String> getRegisteredCourses() {
        return registeredCourses;
    }

    public void registerCourse(String courseCode) {
        if (!registeredCourses.contains(courseCode)) {
            registeredCourses.add(courseCode);
        }
    }

    @Override
    public String toString() {
        return "Student ID: " + studentId + ", Registered Courses: " + registeredCourses;
    }
}

