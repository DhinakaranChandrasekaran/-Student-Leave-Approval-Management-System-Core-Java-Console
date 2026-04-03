package service;

import model.Student;
import util.DatabaseHandler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class StudentService {
    private ArrayList<Student> studentList;
    private HashMap<String, Student> studentMap;
    private static Scanner scanner = new Scanner(System.in);

    public StudentService(ArrayList<Student> studentList, HashMap<String, Student> studentMap) {
        this.studentList = studentList;
        this.studentMap = studentMap;
    }

    // Input helper methods
    private String getString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private void pause() {
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }

    public void addStudent() {
        System.out.println("\n========================================");
        System.out.println("          ADD STUDENT");
        System.out.println("========================================");

        String regNo = getString("Enter Register Number: ");

        if (studentMap.containsKey(regNo)) {
            System.out.println("\nError: Student with this Register Number already exists!");
            pause();
            return;
        }

        String name = getString("Enter Name: ");
        String department = getString("Enter Department: ");
        String year = getString("Enter Year: ");
        String batch = getString("Enter Batch: ");

        Student student = new Student(name, regNo, department, year, batch);
        studentList.add(student);
        studentMap.put(regNo, student);
        DatabaseHandler.saveStudent(student);

        System.out.println("\nStudent added successfully!");
        pause();
    }

    public void viewStudents() {
        System.out.println("\n========================================");
        System.out.println("          VIEW STUDENTS");
        System.out.println("========================================");

        if (studentList.isEmpty()) {
            System.out.println("\nNo students found.");
            pause();
            return;
        }

        System.out.println("\n" + String.format("%-15s %-15s %-20s %-10s %-10s",
                "REG NO", "NAME", "DEPARTMENT", "YEAR", "BATCH"));
        System.out.println("--------------------------------------------------------------------------------");

        for (Student student : studentList) {
            System.out.println(student);
        }

        System.out.println("\nTotal Students: " + studentList.size());
        pause();
    }

    public void updateStudent() {
        System.out.println("\n========================================");
        System.out.println("          UPDATE STUDENT");
        System.out.println("========================================");

        String regNo = getString("Enter Register Number: ");

        Student student = studentMap.get(regNo);
        if (student == null) {
            System.out.println("\nError: Student not found!");
            pause();
            return;
        }

        System.out.println("\nCurrent Details:");
        System.out.println("Name: " + student.getName());
        System.out.println("Department: " + student.getDepartment());
        System.out.println("Year: " + student.getYear());
        System.out.println("Batch: " + student.getBatch());

        System.out.println("\nEnter new details (press Enter to keep current value):");

        String name = getString("Enter Name [" + student.getName() + "]: ");
        if (!name.isEmpty()) {
            student.setName(name);
        }

        String department = getString("Enter Department [" + student.getDepartment() + "]: ");
        if (!department.isEmpty()) {
            student.setDepartment(department);
        }

        String year = getString("Enter Year [" + student.getYear() + "]: ");
        if (!year.isEmpty()) {
            student.setYear(year);
        }

        String batch = getString("Enter Batch [" + student.getBatch() + "]: ");
        if (!batch.isEmpty()) {
            student.setBatch(batch);
        }

        DatabaseHandler.saveStudent(student);
        System.out.println("\nStudent updated successfully!");
        pause();
    }

    public void deleteStudent() {
        System.out.println("\n========================================");
        System.out.println("          DELETE STUDENT");
        System.out.println("========================================");

        String regNo = getString("Enter Register Number: ");

        Student student = studentMap.get(regNo);
        if (student == null) {
            System.out.println("\nError: Student not found!");
            pause();
            return;
        }

        System.out.println("\nStudent Details:");
        System.out.println("Name: " + student.getName());
        System.out.println("Register Number: " + student.getRegNo());
        System.out.println("Department: " + student.getDepartment());

        String confirm = getString("\nAre you sure you want to delete? (Y/N): ");
        if (confirm.equalsIgnoreCase("Y")) {
            studentList.remove(student);
            studentMap.remove(regNo);
            DatabaseHandler.deleteStudent(regNo);
            System.out.println("\nStudent deleted successfully!");
        } else {
            System.out.println("\nDeletion cancelled.");
        }
        pause();
    }

    public Student getStudentByRegNo(String regNo) {
        return studentMap.get(regNo);
    }
}
