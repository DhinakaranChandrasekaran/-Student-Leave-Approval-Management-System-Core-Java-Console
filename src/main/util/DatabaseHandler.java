package util;

import model.Student;
import model.LeaveRequest;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * DatabaseHandler - Handles all MySQL database operations
 */
public class DatabaseHandler {

    /**
     * Get a connection to MySQL database
     */
    public static Connection getConnection() throws SQLException {
        try {
            Class.forName(DatabaseConfig.getJdbcDriver());
            return DriverManager.getConnection(
                    DatabaseConfig.getJdbcUrl(),
                    DatabaseConfig.getDbUser(),
                    DatabaseConfig.getDbPassword());
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL Driver not found! Make sure mysql-connector jar is in lib folder.");
        }
    }

    /**
     * Test if database connection works
     */
    public static boolean testConnection() {
        try {
            Connection conn = getConnection();
            if (conn != null) {
                conn.close();
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Connection failed: " + e.getMessage());
        }
        return false;
    }

    /**
     * Create all required tables if they don't exist
     */
    public static void initializeDatabase() {
        try {
            Connection conn = getConnection();
            Statement stmt = conn.createStatement();

            // Create students table
            stmt.execute(
                    "CREATE TABLE IF NOT EXISTS students (" +
                            "id INT AUTO_INCREMENT PRIMARY KEY," +
                            "reg_no VARCHAR(50) UNIQUE NOT NULL," +
                            "name VARCHAR(100) NOT NULL," +
                            "department VARCHAR(100) NOT NULL," +
                            "year VARCHAR(10) NOT NULL," +
                            "batch VARCHAR(20) NOT NULL)");

            // Create leave_requests table
            stmt.execute(
                    "CREATE TABLE IF NOT EXISTS leave_requests (" +
                            "id INT AUTO_INCREMENT PRIMARY KEY," +
                            "leave_id VARCHAR(20) UNIQUE NOT NULL," +
                            "reg_no VARCHAR(50) NOT NULL," +
                            "student_name VARCHAR(100) NOT NULL," +
                            "from_date VARCHAR(20) NOT NULL," +
                            "to_date VARCHAR(20) NOT NULL," +
                            "reason TEXT," +
                            "applied_date VARCHAR(20) NOT NULL," +
                            "status VARCHAR(20) NOT NULL," +
                            "FOREIGN KEY (reg_no) REFERENCES students(reg_no) ON DELETE CASCADE)");

            stmt.close();
            conn.close();
            System.out.println("Database tables ready.");

        } catch (SQLException e) {
            System.out.println("Error creating tables: " + e.getMessage());
        }
    }

    // ==================== STUDENT OPERATIONS ====================

    /**
     * Load all students from database
     */
    public static void loadStudents(ArrayList<Student> studentList, HashMap<String, Student> studentMap) {
        try {
            Connection conn = getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM students");

            while (rs.next()) {
                Student student = new Student(
                        rs.getString("name"),
                        rs.getString("reg_no"),
                        rs.getString("department"),
                        rs.getString("year"),
                        rs.getString("batch"));
                studentList.add(student);
                studentMap.put(student.getRegNo(), student);
            }
            rs.close();
            stmt.close();
            conn.close();
            System.out.println("Students loaded successfully.");
        } catch (SQLException e) {
            System.out.println("Error loading students: " + e.getMessage());
        }
    }

    /**
     * Save a single student (insert or update)
     */
    public static void saveStudent(Student student) {
        try {
            Connection conn = getConnection();
            PreparedStatement checkStmt = conn.prepareStatement(
                    "SELECT COUNT(*) FROM students WHERE reg_no = ?");
            checkStmt.setString(1, student.getRegNo());
            ResultSet rs = checkStmt.executeQuery();
            rs.next();
            boolean exists = rs.getInt(1) > 0;
            rs.close();
            checkStmt.close();

            if (exists) {
                PreparedStatement updateStmt = conn.prepareStatement(
                        "UPDATE students SET name=?, department=?, year=?, batch=? WHERE reg_no=?");
                updateStmt.setString(1, student.getName());
                updateStmt.setString(2, student.getDepartment());
                updateStmt.setString(3, student.getYear());
                updateStmt.setString(4, student.getBatch());
                updateStmt.setString(5, student.getRegNo());
                updateStmt.executeUpdate();
                updateStmt.close();
            } else {
                PreparedStatement insertStmt = conn.prepareStatement(
                        "INSERT INTO students (reg_no, name, department, year, batch) VALUES (?,?,?,?,?)");
                insertStmt.setString(1, student.getRegNo());
                insertStmt.setString(2, student.getName());
                insertStmt.setString(3, student.getDepartment());
                insertStmt.setString(4, student.getYear());
                insertStmt.setString(5, student.getBatch());
                insertStmt.executeUpdate();
                insertStmt.close();
            }
            conn.close();
        } catch (SQLException e) {
            System.out.println("Error saving student: " + e.getMessage());
        }
    }

    /**
     * Save multiple students
     */
    public static void saveStudents(ArrayList<Student> students) {
        for (Student student : students) {
            saveStudent(student);
        }
    }

    /**
     * Delete a student by register number
     */
    public static void deleteStudent(String regNo) {
        try {
            Connection conn = getConnection();
            PreparedStatement pstmt = conn.prepareStatement(
                    "DELETE FROM students WHERE reg_no = ?");
            pstmt.setString(1, regNo);
            pstmt.executeUpdate();
            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println("Error deleting student: " + e.getMessage());
        }
    }

    // ==================== LEAVE REQUEST OPERATIONS ====================

    /**
     * Load all leave requests from database
     */
    public static void loadLeaves(ArrayList<LeaveRequest> leaveList) {
        try {
            Connection conn = getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM leave_requests");

            while (rs.next()) {
                LeaveRequest leave = new LeaveRequest(
                        rs.getString("leave_id"),
                        rs.getString("reg_no"),
                        rs.getString("student_name"),
                        rs.getString("from_date"),
                        rs.getString("to_date"),
                        rs.getString("reason"),
                        rs.getString("applied_date"),
                        rs.getString("status"));
                leaveList.add(leave);
            }
            rs.close();
            stmt.close();
            conn.close();
            System.out.println("Leave requests loaded successfully.");
        } catch (SQLException e) {
            System.out.println("Error loading leave requests: " + e.getMessage());
        }
    }

    /**
     * Save a single leave request (insert or update)
     */
    public static void saveLeave(LeaveRequest leave) {
        try {
            Connection conn = getConnection();
            PreparedStatement checkStmt = conn.prepareStatement(
                    "SELECT COUNT(*) FROM leave_requests WHERE leave_id = ?");
            checkStmt.setString(1, leave.getLeaveId());
            ResultSet rs = checkStmt.executeQuery();
            rs.next();
            boolean exists = rs.getInt(1) > 0;
            rs.close();
            checkStmt.close();

            if (exists) {
                PreparedStatement updateStmt = conn.prepareStatement(
                        "UPDATE leave_requests SET reg_no=?, student_name=?, from_date=?, to_date=?, " +
                                "reason=?, applied_date=?, status=? WHERE leave_id=?");
                updateStmt.setString(1, leave.getRegNo());
                updateStmt.setString(2, leave.getStudentName());
                updateStmt.setString(3, leave.getFromDate());
                updateStmt.setString(4, leave.getToDate());
                updateStmt.setString(5, leave.getReason());
                updateStmt.setString(6, leave.getAppliedDate());
                updateStmt.setString(7, leave.getStatus());
                updateStmt.setString(8, leave.getLeaveId());
                updateStmt.executeUpdate();
                updateStmt.close();
            } else {
                PreparedStatement insertStmt = conn.prepareStatement(
                        "INSERT INTO leave_requests (leave_id, reg_no, student_name, from_date, to_date, " +
                                "reason, applied_date, status) VALUES (?,?,?,?,?,?,?,?)");
                insertStmt.setString(1, leave.getLeaveId());
                insertStmt.setString(2, leave.getRegNo());
                insertStmt.setString(3, leave.getStudentName());
                insertStmt.setString(4, leave.getFromDate());
                insertStmt.setString(5, leave.getToDate());
                insertStmt.setString(6, leave.getReason());
                insertStmt.setString(7, leave.getAppliedDate());
                insertStmt.setString(8, leave.getStatus());
                insertStmt.executeUpdate();
                insertStmt.close();
            }
            conn.close();
        } catch (SQLException e) {
            System.out.println("Error saving leave request: " + e.getMessage());
        }
    }

    /**
     * Save multiple leave requests
     */
    public static void saveLeaves(ArrayList<LeaveRequest> leaves) {
        for (LeaveRequest leave : leaves) {
            saveLeave(leave);
        }
    }

    /**
     * Delete a leave request by leave ID
     */
    public static void deleteLeave(String leaveId) {
        try {
            Connection conn = getConnection();
            PreparedStatement pstmt = conn.prepareStatement(
                    "DELETE FROM leave_requests WHERE leave_id = ?");
            pstmt.setString(1, leaveId);
            pstmt.executeUpdate();
            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println("Error deleting leave request: " + e.getMessage());
        }
    }
}