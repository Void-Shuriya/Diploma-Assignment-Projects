package com.example.studentattendanceapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class SummaryActivity extends AppCompatActivity {

    DBHelper db;
    TextView tvTotalClasses;
    ListView listSummary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);
        findViewById(R.id.btnQuit).setOnClickListener(v -> finishAffinity());

        db = new DBHelper(this);
        tvTotalClasses = findViewById(R.id.tvTotalClasses);
        listSummary = findViewById(R.id.listSummary);

        loadSummary();

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

    private void loadSummary() {
        ArrayList<Student> students = db.getAllStudents();
        int totalClasses = db.getTotalClasses(); // total unique dates in tbl_attendance
        tvTotalClasses.setText("Total classes: " + totalClasses);

        ArrayList<String> displayList = new ArrayList<>();
        for (Student s : students) {
            int attended = db.getStudentAttendanceCount(s.getId()); // number of "Present" for this student
            String percentage = totalClasses == 0 ? "0%" :
                    (attended * 100 / totalClasses) + "%";
            displayList.add(s.getName() + " (" + s.getMatricNo() + ") - " + percentage);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, displayList);
        listSummary.setAdapter(adapter);
    }
}
