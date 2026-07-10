package com.example.studentattendanceapp;

public class Student {
    private int id;
    private String name;
    private String matricNo;
    private String program;

    public Student(int id, String name, String matricNo, String program) {
        this.id = id;
        this.name = name;
        this.matricNo = matricNo;
        this.program = program;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getMatricNo() { return matricNo; }
    public String getProgram() { return program; }

    @Override
    public String toString() { return name; }
}
