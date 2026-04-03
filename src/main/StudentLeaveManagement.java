// Student Leave Management System - Main Entry Point

import model.Student;
import model.LeaveRequest;
import service.StudentService;
import service.LeaveService;
import util.DatabaseHandler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class StudentLeaveManagement {
    private StudentService studentService;
    private LeaveService leaveService;
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("   STUDENT LEAVE MANAGEMENT SYSTEM");
        System.out.println("========================================");
        System.out.println("Connecting to database...\n");

        // Test database connection
        if (!DatabaseHandler.testConnection()) {
            System.out.println("\nError: Could not connect to database!");
            System.out.println("Please ensure:");
            System.out.println("  1. MySQL server is running");
            System.out.println("  2. Database credentials in .env are correct");
            System.out.println("  3. MySQL JDBC driver is in lib/ folder");
            System.exit(1);
        }
        System.out.println("Database connection successful!");

        // Initialize database tables
        DatabaseHandler.initializeDatabase();

        // Load data from database
        ArrayList<Student> studentList = new ArrayList<>();
        HashMap<String, Student> studentMap = new HashMap<>();
        ArrayList<LeaveRequest> leaveList = new ArrayList<>();

        System.out.println("\nLoading data from database...");
        DatabaseHandler.loadStudents(studentList, studentMap);
        DatabaseHandler.loadLeaves(leaveList);

        // Initialize services and start application
        StudentService studentService = new StudentService(studentList, studentMap);
        LeaveService leaveService = new LeaveService(leaveList, studentService);

        System.out.println("\nSystem initialized successfully!");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Create instance and show menu
        StudentLeaveManagement app = new StudentLeaveManagement(studentService, leaveService);
        app.showMainMenu();
    }

    public StudentLeaveManagement(StudentService studentService, LeaveService leaveService) {
        this.studentService = studentService;
        this.leaveService = leaveService;
    }

    // Input helper methods
    private int getInt(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }

    private void pause() {
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }

    public void showMainMenu() {
        while (true) {
            System.out.println("\n========================================");
            System.out.println("   STUDENT LEAVE MANAGEMENT SYSTEM");
            System.out.println("========================================");
            System.out.println("1. Student Management");
            System.out.println("2. Leave Management");
            System.out.println("3. Leave Statistics");
            System.out.println("4. Exit");
            System.out.println("========================================");

            int choice = getInt("Enter choice: ");

            switch (choice) {
                case 1:
                    showStudentMenu();
                    break;
                case 2:
                    showLeaveMenu();
                    break;
                case 3:
                    showStatisticsMenu();
                    break;
                case 4:
                    System.out.println("\nThank you for using Student Leave Management System!");
                    System.exit(0);
                default:
                    System.out.println("\nInvalid choice! Please try again.");
                    pause();
            }
        }
    }

    private void showStudentMenu() {
        while (true) {
            System.out.println("\n========================================");
            System.out.println("       STUDENT MANAGEMENT");
            System.out.println("========================================");
            System.out.println("1. Add Student");
            System.out.println("2. View Students");
            System.out.println("3. Update Student");
            System.out.println("4. Delete Student");
            System.out.println("5. Back");
            System.out.println("========================================");

            int choice = getInt("Enter choice: ");

            switch (choice) {
                case 1:
                    studentService.addStudent();
                    break;
                case 2:
                    studentService.viewStudents();
                    break;
                case 3:
                    studentService.updateStudent();
                    break;
                case 4:
                    studentService.deleteStudent();
                    break;
                case 5:
                    return;
                default:
                    System.out.println("\nInvalid choice! Please try again.");
                    pause();
            }
        }
    }

    private void showLeaveMenu() {
        while (true) {
            System.out.println("\n========================================");
            System.out.println("       LEAVE MANAGEMENT");
            System.out.println("========================================");
            System.out.println("1. Apply Leave");
            System.out.println("2. View Leave Requests");
            System.out.println("3. Approve Leave");
            System.out.println("4. Reject Leave");
            System.out.println("5. Delete Leave");
            System.out.println("6. Back");
            System.out.println("========================================");

            int choice = getInt("Enter choice: ");

            switch (choice) {
                case 1:
                    leaveService.applyLeave();
                    break;
                case 2:
                    leaveService.viewLeaveRequests();
                    break;
                case 3:
                    leaveService.approveLeave();
                    break;
                case 4:
                    leaveService.rejectLeave();
                    break;
                case 5:
                    leaveService.deleteLeave();
                    break;
                case 6:
                    return;
                default:
                    System.out.println("\nInvalid choice! Please try again.");
                    pause();
            }
        }
    }

    private void showStatisticsMenu() {
        while (true) {
            System.out.println("\n========================================");
            System.out.println("       LEAVE STATISTICS");
            System.out.println("========================================");
            System.out.println("1. View Overall Statistics");
            System.out.println("2. Search by Register Number");
            System.out.println("3. Filter by Reason");
            System.out.println("4. Filter by Status");
            System.out.println("5. Back");
            System.out.println("========================================");

            int choice = getInt("Enter choice: ");

            switch (choice) {
                case 1:
                    leaveService.viewStatistics();
                    break;
                case 2:
                    leaveService.searchByRegNo();
                    break;
                case 3:
                    leaveService.filterByReason();
                    break;
                case 4:
                    leaveService.filterByStatus();
                    break;
                case 5:
                    return;
                default:
                    System.out.println("\nInvalid choice! Please try again.");
                    pause();
            }
        }
    }
}
