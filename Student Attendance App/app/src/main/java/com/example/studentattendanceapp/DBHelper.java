package com.example.studentattendanceapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "AttendanceDB";
    private static final int DATABASE_VERSION = 1;

    // Students table
    private static final String TABLE_STUDENTS = "tbl_student";
    private static final String COL_S_ID = "student_id";
    private static final String COL_S_NAME = "name";
    private static final String COL_S_MATRIC = "matric_no";
    private static final String COL_S_PROGRAM = "program";

    // Attendance table
    private static final String TABLE_ATT = "tbl_attendance";
    private static final String COL_A_ID = "attendance_id";
    private static final String COL_A_STUDENT_ID = "student_id";
    private static final String COL_A_DATE = "date";
    private static final String COL_A_STATUS = "status";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createStudents = "CREATE TABLE " + TABLE_STUDENTS + " (" +
                COL_S_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_S_NAME + " TEXT NOT NULL, " +
                COL_S_MATRIC + " TEXT NOT NULL, " +
                COL_S_PROGRAM + " TEXT);";

        String createAttendance = "CREATE TABLE " + TABLE_ATT + " (" +
                COL_A_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_A_STUDENT_ID + " INTEGER NOT NULL, " +
                COL_A_DATE + " TEXT NOT NULL, " +
                COL_A_STATUS + " TEXT NOT NULL, " +
                "FOREIGN KEY(" + COL_A_STUDENT_ID + ") REFERENCES " + TABLE_STUDENTS + "(" + COL_S_ID + "));";

        db.execSQL(createStudents);
        db.execSQL(createAttendance);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ATT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STUDENTS);
        onCreate(db);
    }

    // ---- Student CRUD ----
    public boolean addStudent(String name, String matric, String program) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_S_NAME, name);
        cv.put(COL_S_MATRIC, matric);
        cv.put(COL_S_PROGRAM, program);
        long res = db.insert(TABLE_STUDENTS, null, cv);
        db.close();
        return res != -1;
    }

    public boolean updateStudent(int id, String name, String matric, String program) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_S_NAME, name);
        cv.put(COL_S_MATRIC, matric);
        cv.put(COL_S_PROGRAM, program);
        int rows = db.update(TABLE_STUDENTS, cv, COL_S_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
        return rows > 0;
    }

    public boolean deleteStudent(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        // delete related attendance first
        db.delete(TABLE_ATT, COL_A_STUDENT_ID + "=?", new String[]{String.valueOf(id)});
        int rows = db.delete(TABLE_STUDENTS, COL_S_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
        return rows > 0;
    }

    public ArrayList<Student> getAllStudents() {
        ArrayList<Student> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_STUDENTS + " ORDER BY " + COL_S_NAME, null);
        if (c.moveToFirst()) {
            do {
                int id = c.getInt(c.getColumnIndexOrThrow(COL_S_ID));
                String name = c.getString(c.getColumnIndexOrThrow(COL_S_NAME));
                String matric = c.getString(c.getColumnIndexOrThrow(COL_S_MATRIC));
                String program = c.getString(c.getColumnIndexOrThrow(COL_S_PROGRAM));
                list.add(new Student(id, name, matric, program));
            } while (c.moveToNext());
        }
        c.close();
        db.close();
        return list;
    }

    // ---- Attendance ----
    // insert or update attendance for (studentId, date)
    public long markAttendance(int studentId, String date, String status) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor c = db.query(TABLE_ATT, new String[]{COL_A_ID},
                COL_A_STUDENT_ID + "=? AND " + COL_A_DATE + "=?",
                new String[]{String.valueOf(studentId), date}, null, null, null);

        ContentValues cv = new ContentValues();
        cv.put(COL_A_STUDENT_ID, studentId);
        cv.put(COL_A_DATE, date);
        cv.put(COL_A_STATUS, status);

        long result;
        if (c.moveToFirst()) {
            int attId = c.getInt(c.getColumnIndexOrThrow(COL_A_ID));
            result = db.update(TABLE_ATT, cv, COL_A_ID + "=?", new String[]{String.valueOf(attId)});
        } else {
            result = db.insert(TABLE_ATT, null, cv);
        }
        c.close();
        db.close();
        return result;
    }

    public String getAttendanceStatus(int studentId, String date) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query(TABLE_ATT, new String[]{COL_A_STATUS},
                COL_A_STUDENT_ID + "=? AND " + COL_A_DATE + "=?",
                new String[]{String.valueOf(studentId), date}, null, null, null);

        String status = null;
        if (c.moveToFirst()) status = c.getString(c.getColumnIndexOrThrow(COL_A_STATUS));
        c.close();
        db.close();
        return status;
    }

    // total unique dates (classes)
    public int getTotalClasses() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT COUNT(DISTINCT " + COL_A_DATE + ") as total FROM " + TABLE_ATT, null);
        int total = 0;
        if (c.moveToFirst()) total = c.getInt(c.getColumnIndexOrThrow("total"));
        c.close();
        db.close();
        return total;
    }

    public int getStudentAttendanceCount(int studentId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT COUNT(*) as cnt FROM " + TABLE_ATT +
                        " WHERE " + COL_A_STUDENT_ID + "=? AND " + COL_A_STATUS + "='Present'",
                new String[]{String.valueOf(studentId)});
        int cnt = 0;
        if (c.moveToFirst()) cnt = c.getInt(c.getColumnIndexOrThrow("cnt"));
        c.close();
        db.close();
        return cnt;
    }
}