========================================
STUDENT LEAVE MANAGEMENT SYSTEM
========================================

A Java console-based application for managing student leave requests.

========================================
PROJECT STRUCTURE
========================================

src/
  model/
     Student.java           - Student model class
     LeaveRequest.java      - Leave request model class

  service/
     StudentService.java    - Student CRUD operations
     LeaveService.java      - Leave CRUD operations
     FileService.java       - File handling operations

  util/
     InputUtil.java         - Input utility methods

  main/
     Main.java              - Application entry point
     MenuManager.java       - Menu navigation manager

data/
   students.txt            - Student records storage
   leaves.txt              - Leave records storage

========================================
COMPILATION INSTRUCTIONS
========================================

1. Open terminal in the project directory

2. Compile all Java files:
   javac -d bin src/model/*.java src/util/*.java src/service/*.java src/main/*.java

3. Create bin directory if it doesn't exist:
   mkdir bin

========================================
EXECUTION INSTRUCTIONS
========================================

Run the application:
   java -cp bin main.Main

========================================
FEATURES
========================================

1. STUDENT MANAGEMENT
   - Add Student
   - View Students
   - Update Student
   - Delete Student

2. LEAVE MANAGEMENT
   - Apply Leave
   - View Leave Requests
   - Approve Leave
   - Reject Leave
   - Delete Leave

3. LEAVE STATISTICS
   - View Overall Statistics
   - Search by Register Number
   - Filter by Reason
   - Filter by Status

========================================
DATA PERSISTENCE
========================================

- Student records saved in: data/students.txt
- Leave records saved in: data/leaves.txt
- Data loaded automatically on startup
- Data saved after every CRUD operation

========================================
TECHNICAL DETAILS
========================================

- Collections: ArrayList and HashMap
- File Handling: BufferedReader/BufferedWriter
- Auto-generated Leave IDs: L001, L002, L003...
- Leave Status: Pending, Approved, Rejected
- Date Format: DD-MM-YYYY

========================================
USAGE GUIDELINES
========================================

1. Add students first before applying leave
2. Leave IDs are auto-generated
3. Only pending leaves can be approved/rejected
4. All operations are file-persisted
5. Use valid register numbers for leave applications

========================================
