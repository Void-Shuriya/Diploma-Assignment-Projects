package com.example.studentattendanceapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.*;

public class AttendanceActivity extends AppCompatActivity {

    DBHelper db;
    ListView listView;
    Button btnPickDate, btnSave;
    TextView tvDate;
    ArrayList<Student> students;
    ArrayList<String> statusList;
    ArrayList<String> displayList;
    ArrayAdapter<String> adapter;
    String selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);
        findViewById(R.id.btnQuit).setOnClickListener(v -> finishAffinity());

        db = new DBHelper(this);
        listView = findViewById(R.id.listAttendance);
        btnPickDate = findViewById(R.id.btnPickDate);
        btnSave = findViewById(R.id.btnSaveAttendance);
        tvDate = findViewById(R.id.tvDate);

        selectedDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        tvDate.setText(selectedDate);

        loadStudentsForAttendance();

        btnPickDate.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();
            new DatePickerDialog(
                    AttendanceActivity.this,
                    (view, year, month, day) -> {
                        month++;
                        selectedDate = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month, day);
                        tvDate.setText(selectedDate);
                        loadStudentsForAttendance();
                    },
                    c.get(Calendar.YEAR),
                    c.get(Calendar.MONTH),
                    c.get(Calendar.DAY_OF_MONTH)
            ).show();
        });

        // Toggle present/absent
        listView.setOnItemClickListener((parent, view, position, id) -> {
            String cur = statusList.get(position);
            if (cur.equals("Present")) statusList.set(position, "Absent");
            else statusList.set(position, "Present");

            updateDisplayList();
        });

        btnSave.setOnClickListener(v -> {
            for (int i = 0; i < students.size(); i++) {
                Student stu = students.get(i);
                String st = statusList.get(i);
                if (st.equals("NotMarked")) st = "Absent";
                db.markAttendance(stu.getId(), selectedDate, st);
            }
            Toast.makeText(this, "Saved for " + selectedDate, Toast.LENGTH_SHORT).show();
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

    private void loadStudentsForAttendance() {
        students = db.getAllStudents();
        statusList = new ArrayList<>();
        displayList = new ArrayList<>();

        for (Student s : students) {
            String st = db.getAttendanceStatus(s.getId(), selectedDate);
            if (st == null) st = "NotMarked";
            statusList.add(st);
            displayList.add(s.toString() + " - " + st);
        }

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, displayList);
        listView.setAdapter(adapter);
    }

    private void updateDisplayList() {
        displayList.clear();
        for (int i = 0; i < students.size(); i++) {
            displayList.add(students.get(i).toString() + " - " + statusList.get(i));
        }
        adapter.notifyDataSetChanged();
    }
}
