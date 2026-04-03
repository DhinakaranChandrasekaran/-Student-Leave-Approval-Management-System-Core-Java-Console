package model;

import java.io.Serializable;

public class LeaveRequest implements Serializable {
    private String leaveId;
    private String regNo;
    private String studentName;
    private String fromDate;
    private String toDate;
    private String reason;
    private String appliedDate;
    private String status;

    public LeaveRequest() {
    }

    public LeaveRequest(String leaveId, String regNo, String studentName, String fromDate,
            String toDate, String reason, String appliedDate, String status) {
        this.leaveId = leaveId;
        this.regNo = regNo;
        this.studentName = studentName;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.reason = reason;
        this.appliedDate = appliedDate;
        this.status = status;
    }

    public String getLeaveId() {
        return leaveId;
    }

    public void setLeaveId(String leaveId) {
        this.leaveId = leaveId;
    }

    public String getRegNo() {
        return regNo;
    }

    public void setRegNo(String regNo) {
        this.regNo = regNo;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getAppliedDate() {
        return appliedDate;
    }

    public void setAppliedDate(String appliedDate) {
        this.appliedDate = appliedDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return String.format("%-10s %-15s %-20s %-12s %-12s %-15s %-12s %-10s",
                leaveId, regNo, studentName, fromDate, toDate, reason, appliedDate, status);
    }

    public String toFileString() {
        return leaveId + "," + regNo + "," + studentName + "," + fromDate + "," +
                toDate + "," + reason + "," + appliedDate + "," + status;
    }

    public static LeaveRequest fromFileString(String line) {
        String[] parts = line.split(",");
        if (parts.length == 8) {
            return new LeaveRequest(parts[0], parts[1], parts[2], parts[3],
                    parts[4], parts[5], parts[6], parts[7]);
        }
        return null;
    }
}
