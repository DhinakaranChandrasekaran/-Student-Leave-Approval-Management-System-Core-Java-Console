package service;

import model.LeaveRequest;
import model.Student;
import util.DatabaseHandler;
import java.util.ArrayList;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LeaveService {
    private ArrayList<LeaveRequest> leaveList;
    private StudentService studentService;
    private static Scanner scanner = new Scanner(System.in);

    public LeaveService(ArrayList<LeaveRequest> leaveList, StudentService studentService) {
        this.leaveList = leaveList;
        this.studentService = studentService;
    }

    // Input helper methods
    private String getString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

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

    public void applyLeave() {
        System.out.println("\n========================================");
        System.out.println("          APPLY LEAVE");
        System.out.println("========================================");

        String regNo = getString("Enter Register Number: ");

        Student student = studentService.getStudentByRegNo(regNo);
        if (student == null) {
            System.out.println("\nError: Student not found!");
            pause();
            return;
        }

        String leaveId = generateLeaveId();
        String fromDate = getString("Enter From Date (DD-MM-YYYY): ");
        String toDate = getString("Enter To Date (DD-MM-YYYY): ");
        String reason = getString("Enter Reason: ");

        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String appliedDate = today.format(formatter);

        LeaveRequest leave = new LeaveRequest(leaveId, regNo, student.getName(),
                fromDate, toDate, reason, appliedDate, "Pending");
        leaveList.add(leave);
        DatabaseHandler.saveLeave(leave);

        System.out.println("\nLeave applied successfully!");
        System.out.println("Leave ID: " + leaveId);
        pause();
    }

    public void viewLeaveRequests() {
        System.out.println("\n========================================");
        System.out.println("          VIEW LEAVE REQUESTS");
        System.out.println("========================================");

        if (leaveList.isEmpty()) {
            System.out.println("\nNo leave requests found.");
            pause();
            return;
        }

        System.out.println("\n" + String.format("%-10s %-15s %-20s %-12s %-12s %-15s %-12s %-10s",
                "LEAVE ID", "REG NO", "NAME", "FROM DATE", "TO DATE", "REASON", "APPLIED", "STATUS"));
        System.out.println(
                "--------------------------------------------------------------------------------------------------------------------------");

        for (LeaveRequest leave : leaveList) {
            System.out.println(leave);
        }

        System.out.println("\nTotal Leave Requests: " + leaveList.size());
        pause();
    }

    public void approveLeave() {
        System.out.println("\n========================================");
        System.out.println("          APPROVE LEAVE");
        System.out.println("========================================");

        String leaveId = getString("Enter Leave ID: ");

        LeaveRequest leave = findLeaveById(leaveId);
        if (leave == null) {
            System.out.println("\nError: Leave request not found!");
            pause();
            return;
        }

        if (!leave.getStatus().equals("Pending")) {
            System.out.println("\nError: Leave is already " + leave.getStatus() + "!");
            pause();
            return;
        }

        System.out.println("\nLeave Details:");
        System.out.println("Leave ID: " + leave.getLeaveId());
        System.out.println("Student: " + leave.getStudentName() + " (" + leave.getRegNo() + ")");
        System.out.println("From: " + leave.getFromDate() + " To: " + leave.getToDate());
        System.out.println("Reason: " + leave.getReason());

        String confirm = getString("\nApprove this leave? (Y/N): ");
        if (confirm.equalsIgnoreCase("Y")) {
            leave.setStatus("Approved");
            DatabaseHandler.saveLeave(leave);
            System.out.println("\nLeave approved successfully!");
        } else {
            System.out.println("\nApproval cancelled.");
        }
        pause();
    }

    public void rejectLeave() {
        System.out.println("\n========================================");
        System.out.println("          REJECT LEAVE");
        System.out.println("========================================");

        String leaveId = getString("Enter Leave ID: ");

        LeaveRequest leave = findLeaveById(leaveId);
        if (leave == null) {
            System.out.println("\nError: Leave request not found!");
            pause();
            return;
        }

        if (!leave.getStatus().equals("Pending")) {
            System.out.println("\nError: Leave is already " + leave.getStatus() + "!");
            pause();
            return;
        }

        System.out.println("\nLeave Details:");
        System.out.println("Leave ID: " + leave.getLeaveId());
        System.out.println("Student: " + leave.getStudentName() + " (" + leave.getRegNo() + ")");
        System.out.println("From: " + leave.getFromDate() + " To: " + leave.getToDate());
        System.out.println("Reason: " + leave.getReason());

        String confirm = getString("\nReject this leave? (Y/N): ");
        if (confirm.equalsIgnoreCase("Y")) {
            leave.setStatus("Rejected");
            DatabaseHandler.saveLeave(leave);
            System.out.println("\nLeave rejected successfully!");
        } else {
            System.out.println("\nRejection cancelled.");
        }
        pause();
    }

    public void deleteLeave() {
        System.out.println("\n========================================");
        System.out.println("          DELETE LEAVE");
        System.out.println("========================================");

        String leaveId = getString("Enter Leave ID: ");

        LeaveRequest leave = findLeaveById(leaveId);
        if (leave == null) {
            System.out.println("\nError: Leave request not found!");
            pause();
            return;
        }

        System.out.println("\nLeave Details:");
        System.out.println("Leave ID: " + leave.getLeaveId());
        System.out.println("Student: " + leave.getStudentName() + " (" + leave.getRegNo() + ")");
        System.out.println("Status: " + leave.getStatus());

        String confirm = getString("\nAre you sure you want to delete? (Y/N): ");
        if (confirm.equalsIgnoreCase("Y")) {
            leaveList.remove(leave);
            DatabaseHandler.deleteLeave(leave.getLeaveId());
            System.out.println("\nLeave deleted successfully!");
        } else {
            System.out.println("\nDeletion cancelled.");
        }
        pause();
    }

    public void viewStatistics() {
        System.out.println("\n========================================");
        System.out.println("       LEAVE STATISTICS");
        System.out.println("========================================");

        int total = leaveList.size();
        int approved = 0;
        int pending = 0;
        int rejected = 0;

        for (LeaveRequest leave : leaveList) {
            switch (leave.getStatus()) {
                case "Approved":
                    approved++;
                    break;
                case "Pending":
                    pending++;
                    break;
                case "Rejected":
                    rejected++;
                    break;
            }
        }

        System.out.println("\nTotal Leave Requests: " + total);
        System.out.println("Approved: " + approved);
        System.out.println("Pending: " + pending);
        System.out.println("Rejected: " + rejected);
        pause();
    }

    public void searchByRegNo() {
        System.out.println("\n========================================");
        System.out.println("    SEARCH BY REGISTER NUMBER");
        System.out.println("========================================");

        String regNo = getString("Enter Register Number: ");

        System.out.println("\n" + String.format("%-10s %-15s %-20s %-12s %-12s %-15s %-12s %-10s",
                "LEAVE ID", "REG NO", "NAME", "FROM DATE", "TO DATE", "REASON", "APPLIED", "STATUS"));
        System.out.println(
                "--------------------------------------------------------------------------------------------------------------------------");

        int count = 0;
        for (LeaveRequest leave : leaveList) {
            if (leave.getRegNo().equals(regNo)) {
                System.out.println(leave);
                count++;
            }
        }

        if (count == 0) {
            System.out.println("No leave requests found for this register number.");
        } else {
            System.out.println("\nTotal: " + count);
        }
        pause();
    }

    public void filterByReason() {
        System.out.println("\n========================================");
        System.out.println("       FILTER BY REASON");
        System.out.println("========================================");

        String reason = getString("Enter Reason: ");

        System.out.println("\n" + String.format("%-10s %-15s %-20s %-12s %-12s %-15s %-12s %-10s",
                "LEAVE ID", "REG NO", "NAME", "FROM DATE", "TO DATE", "REASON", "APPLIED", "STATUS"));
        System.out.println(
                "--------------------------------------------------------------------------------------------------------------------------");

        int count = 0;
        for (LeaveRequest leave : leaveList) {
            if (leave.getReason().toLowerCase().contains(reason.toLowerCase())) {
                System.out.println(leave);
                count++;
            }
        }

        if (count == 0) {
            System.out.println("No leave requests found with this reason.");
        } else {
            System.out.println("\nTotal: " + count);
        }
        pause();
    }

    public void filterByStatus() {
        System.out.println("\n========================================");
        System.out.println("       FILTER BY STATUS");
        System.out.println("========================================");

        System.out.println("1. Pending");
        System.out.println("2. Approved");
        System.out.println("3. Rejected");
        int choice = getInt("Enter choice: ");

        String status = "";
        switch (choice) {
            case 1:
                status = "Pending";
                break;
            case 2:
                status = "Approved";
                break;
            case 3:
                status = "Rejected";
                break;
            default:
                System.out.println("Invalid choice!");
                pause();
                return;
        }

        System.out.println("\n" + String.format("%-10s %-15s %-20s %-12s %-12s %-15s %-12s %-10s",
                "LEAVE ID", "REG NO", "NAME", "FROM DATE", "TO DATE", "REASON", "APPLIED", "STATUS"));
        System.out.println(
                "--------------------------------------------------------------------------------------------------------------------------");

        int count = 0;
        for (LeaveRequest leave : leaveList) {
            if (leave.getStatus().equals(status)) {
                System.out.println(leave);
                count++;
            }
        }

        if (count == 0) {
            System.out.println("No leave requests found with status: " + status);
        } else {
            System.out.println("\nTotal: " + count);
        }
        pause();
    }

    private LeaveRequest findLeaveById(String leaveId) {
        for (LeaveRequest leave : leaveList) {
            if (leave.getLeaveId().equals(leaveId)) {
                return leave;
            }
        }
        return null;
    }

    private String generateLeaveId() {
        if (leaveList.isEmpty()) {
            return "L001";
        }

        int maxId = 0;
        for (LeaveRequest leave : leaveList) {
            String id = leave.getLeaveId().substring(1);
            int num = Integer.parseInt(id);
            if (num > maxId) {
                maxId = num;
            }
        }

        return String.format("L%03d", maxId + 1);
    }
}
