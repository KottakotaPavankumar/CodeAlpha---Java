package Project;

/* Console-based */

import java.util.ArrayList;
import java.util.Scanner;

class Student {
    private String name;
    private ArrayList<Integer> grades;

    public Student(String name) {
        this.name = name;
        this.grades = new ArrayList<>();
    }

    public void addGrade(int grade) {
        grades.add(grade);
    }

    public double getAverage() {
        if (grades.isEmpty()) return 0.0;
        int sum = 0;
        for (int grade : grades) sum += grade;
        return (double) sum / grades.size();
    }

    public int getHighest() {
        int max = Integer.MIN_VALUE;
        for (int grade : grades) {
            if (grade > max) max = grade;
        }
        return (grades.isEmpty()) ? 0 : max;
    }

    public int getLowest() {
        int min = Integer.MAX_VALUE;
        for (int grade : grades) {
            if (grade < min) min = grade;
        }
        return (grades.isEmpty()) ? 0 : min;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Integer> getGrades() {
        return grades;
    }
}

public class Student_grade {
    private static ArrayList<Student> students = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        int choice;
        do {
            System.out.println("\n--- Student Grade Manager ---");
            System.out.println("1. Add Student");
            System.out.println("2. Add Grade to Student");
            System.out.println("3. Show Student Report");
            System.out.println("4. Show All Students Summary");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1 -> addStudent();
                case 2 -> addGradeToStudent();
                case 3 -> showStudentReport();
                case 4 -> showAllSummary();
                case 5 -> System.out.println("Exiting... Goodbye!");
                default -> System.out.println("Invalid choice. Try again.");
            }
        } while (choice != 5);
    }

    private static void addStudent() {
        System.out.print("Enter student name: ");
        String name = scanner.nextLine();
        students.add(new Student(name));
        System.out.println("Student added successfully!");
    }

    private static void addGradeToStudent() {
        if (students.isEmpty()) {
            System.out.println("No students available. Add a student first.");
            return;
        }

        showStudents();
        System.out.print("Choose student index: ");
        int index = scanner.nextInt();
        if (index < 0 || index >= students.size()) {
            System.out.println("Invalid student index.");
            return;
        }

        System.out.print("Enter grade (0-100): ");
        int grade = scanner.nextInt();
        students.get(index).addGrade(grade);
        System.out.println("Grade added to " + students.get(index).getName());
    }

    private static void showStudentReport() {
        if (students.isEmpty()) {
            System.out.println("No students available.");
            return;
        }

        showStudents();
        System.out.print("Choose student index: ");
        int index = scanner.nextInt();
        if (index < 0 || index >= students.size()) {
            System.out.println("Invalid student index.");
            return;
        }

        Student s = students.get(index);
        System.out.println("\n--- Report for " + s.getName() + " ---");
        System.out.println("Grades: " + s.getGrades());
        System.out.println("Average: " + s.getAverage());
        System.out.println("Highest: " + s.getHighest());
        System.out.println("Lowest: " + s.getLowest());
    }

    private static void showAllSummary() {
        if (students.isEmpty()) {
            System.out.println("No student data available.");
            return;
        }

        System.out.println("\n--- Summary of All Students ---");
        for (Student s : students) {
            System.out.println("Name: " + s.getName());
            System.out.println("Grades: " + s.getGrades());
            System.out.printf("Average: %.2f, Highest: %d, Lowest: %d%n",
                    s.getAverage(), s.getHighest(), s.getLowest());
            System.out.println("-------------------------");
        }
    }

    private static void showStudents() {
        System.out.println("\nAvailable students:");
        for (int i = 0; i < students.size(); i++) {
            System.out.println(i + " - " + students.get(i).getName());
        }
    }
}



/*

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class StudentGradeManagerGUI {
    private JFrame frame;
    private JTextField nameField, gradeField;
    private JTextArea outputArea;
    private Student currentStudent;
    private ArrayList<Student> students;

    public StudentGradeManagerGUI() {
        students = new ArrayList<>();
        frame = new JFrame("Student Grade Manager");
        frame.setSize(500, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new GridLayout(2, 2));
        topPanel.add(new JLabel("Student Name: "));
        nameField = new JTextField();
        topPanel.add(nameField);

        topPanel.add(new JLabel("Grade: "));
        gradeField = new JTextField();
        topPanel.add(gradeField);
        frame.add(topPanel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel();
        JButton addStudentBtn = new JButton("Add Student");
        JButton addGradeBtn = new JButton("Add Grade");
        JButton showReportBtn = new JButton("Show Report");

        buttonPanel.add(addStudentBtn);
        buttonPanel.add(addGradeBtn);
        buttonPanel.add(showReportBtn);
        frame.add(buttonPanel, BorderLayout.CENTER);

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        frame.add(new JScrollPane(outputArea), BorderLayout.SOUTH);

        addStudentBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                if (!name.isEmpty()) {
                    currentStudent = new Student(name);
                    students.add(currentStudent);
                    outputArea.setText("Added student: " + name + "\n");
                    nameField.setText("");
                }
            }
        });

        addGradeBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (currentStudent != null) {
                    try {
                        int grade = Integer.parseInt(gradeField.getText());
                        currentStudent.addGrade(grade);
                        outputArea.append("Added grade " + grade + " to " + currentStudent.name + "\n");
                        gradeField.setText("");
                    } catch (Exception ex) {
                        outputArea.append("Invalid grade input.\n");
                    }
                } else {
                    outputArea.append("No student selected!\n");
                }
            }
        });

        showReportBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                outputArea.setText("==== Summary Report ====\n\n");
                for (Student s : students) {
                    outputArea.append(s.toString() + "\n");
                }
            }
        });

        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new StudentGradeManagerGUI();
    }
}

*/