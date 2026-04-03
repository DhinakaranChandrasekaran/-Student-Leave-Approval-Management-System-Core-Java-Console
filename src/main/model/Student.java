package model;

import java.io.Serializable;

public class Student implements Serializable {
    private String name;
    private String regNo;
    private String department;
    private String year;
    private String batch;

    public Student() {
    }

    public Student(String name, String regNo, String department, String year, String batch) {
        this.name = name;
        this.regNo = regNo;
        this.department = department;
        this.year = year;
        this.batch = batch;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegNo() {
        return regNo;
    }

    public void setRegNo(String regNo) {
        this.regNo = regNo;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    @Override
    public String toString() {
        return String.format("%-15s %-15s %-20s %-10s %-10s",
                regNo, name, department, year, batch);
    }

    public String toFileString() {
        return regNo + "," + name + "," + department + "," + year + "," + batch;
    }

    public static Student fromFileString(String line) {
        String[] parts = line.split(",");
        if (parts.length == 5) {
            return new Student(parts[1], parts[0], parts[2], parts[3], parts[4]);
        }
        return null;
    }
}
