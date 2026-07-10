package com.example.studentattendanceapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AddEditStudentActivity extends AppCompatActivity {

    EditText etName, etMatric, etProgram;
    Button btnSave;
    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);

        findViewById(R.id.btnQuit).setOnClickListener(v -> finishAffinity());

        db = new DBHelper(this);
        etName = findViewById(R.id.etName);
        etMatric = findViewById(R.id.etMatric);
        etProgram = findViewById(R.id.etProgram);
        btnSave = findViewById(R.id.btnSave);

        btnSave.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String matric = etMatric.getText().toString().trim();
            String program = etProgram.getText().toString().trim();

            if (name.isEmpty() || matric.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean inserted = db.addStudent(name, matric, program);
            if (inserted) Toast.makeText(this, "Student added", Toast.LENGTH_SHORT).show();
            else Toast.makeText(this, "Error adding student", Toast.LENGTH_SHORT).show();
            finish();
        });

        // Bottom navigation
        findViewById(R.id.nav_home).setOnClickListener(v ->
                startActivity(new Intent(this, MainActivity.class)));
        findViewById(R.id.nav_add).setOnClickListener(v ->
                startActivity(new Intent(this, AddEditStudentActivity.class)));
        findViewById(R.id.nav_attendance).setOnClickListener(v ->
                startActivity(new Intent(this, AttendanceActivity.class)));
        findViewById(R.id.nav_summary).setOnClickListener(v ->
                startActivity(new Intent(this, SummaryActivity.class)));
    }
}