package com.example.studentattendanceapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView listStudents;
    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btnQuit).setOnClickListener(v -> finishAffinity());

        db = new DBHelper(this);
        listStudents = findViewById(R.id.listStudents);

        loadStudents();

        // Long press to delete a student
        listStudents.setOnItemLongClickListener((parent, view, position, id) -> {
            Student s = db.getAllStudents().get(position);

            new androidx.appcompat.app.AlertDialog.Builder(this)
                    .setTitle("Delete Student")
                    .setMessage("Are you sure you want to delete " + s.getName() + "?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        db.deleteStudent(s.getId());
                        Toast.makeText(this, s.getName() + " deleted", Toast.LENGTH_SHORT).show();
                        loadStudents(); // refresh list
                    })
                    .setNegativeButton("No", null)
                    .show();

            return true;
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

    private void loadStudents() {
        ArrayList<Student> students = db.getAllStudents();
        ArrayList<String> display = new ArrayList<>();
        for (Student s : students) display.add(s.getName() + " (" + s.getMatricNo() + ")");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, display);
        listStudents.setAdapter(adapter);
    }
}
