#include <iostream>
#include <fstream>
#include <sstream> 
#include <iomanip>
#include <ctime> 

using namespace std;

struct studentRecords
{
    string name;
    int asg1, asg2, test, quiz, exam;
    double courseWork, overallScore;
    char grade;
};

// Counters for grades
int countA = 0, countB = 0, countC = 0, countD = 0, countF = 0;

// Functions 
void computeMarks(studentRecords& student);
void computeGrade(const studentRecords& student);
void studentResults(const studentRecords& student);
void resultSummary(const studentRecords& student);
void readStudentData();


void computeMarks(studentRecords& student)
{
    student.courseWork = ((student.asg1 / 100.00) * 0.25 + (student.asg2 / 100.00) 
    * 0.25 + (student.test / 100.00) * 0.1 + (student.quiz / 100.00) * 0.1) * 70;

    student.overallScore = student.courseWork + ((student.exam / 100.0) * 30);
}

void computeGrade(studentRecords& student)
{
    if (student.overallScore >= 70)
    {
        student.grade = 'A';
        countA++;
    }
    else if (student.overallScore >= 60)
    {
        student.grade = 'B';
        countB++;
    }
    else if (student.overallScore >= 50)
    {
        student.grade = 'C';
        countC++;
    }
    else if (student.overallScore >= 40)
    {
        student.grade = 'D';
        countD++;
    }
    else
    {
        student.grade = 'F';
        countF++;
    }
}

// Write to Results File
void studentResults(const studentRecords& student)
{
    static bool header = false;

    ofstream oFile("Results.txt", ios::app);

    if (oFile.is_open())
    {
        if (!header)
        {
            oFile << left
                << setw(18) << "Name"
                << setw(22) << "Coursework"
                << setw(16) << "Exam"
                << setw(22) << "Overall Score"
                << setw(20) << "Grade" << endl

                << string(85, '-') << endl;

            header = true;
        }
        oFile << left
            << setw(20) << student.name
            << setw(20) << fixed << setprecision(2) << student.courseWork
            << setw(20) << fixed << setprecision(2) << static_cast<double>(student.exam)
            << setw(20) << fixed << setprecision(2) << student.overallScore
            << setw(20) << fixed << student.grade << endl;

        oFile.close();
    }
    else
    {
        cerr << "Unable to write to Results File" << endl;
    }
}

void resultSummary(const studentRecords& student)
{
    ofstream sumFile("Results.txt", ios::app);

    if (sumFile.is_open())
    {
        // Get current date 
        time_t now = time(0);
        char dt[26];
        ctime_s(dt, sizeof(dt), &now);

        sumFile
            << "\n\n" << string(14, '-') << endl
            << "Result Summary" << endl
            << string(14, '-') << endl
            << "Date: " << dt
            << "Subject: Programming Methodology" << "\n\n"
            << "Grade" << "\t" << "Student" << endl
            << setw(3) << "A " << setw(8) << countA << endl
            << setw(3) << "B " << setw(8) << countB << endl
            << setw(3) << "C " << setw(8) << countC << endl
            << setw(3) << "D " << setw(8) << countD << endl
            << setw(3) << "F " << setw(8) << countF << endl
            << endl << "Total Students: " << countA + countB + countC + countD + countF;
    }
    else
    {
        cerr << "Unable to write to Results File" << endl;
    }
}

void readStudentData()
{
    studentRecords student;

    string line;
    ifstream inFile("Student.txt");

    if (inFile.is_open())
    {
        // Header
        cout << endl << "Name " << setw(17) << "Asg1"
            << setw(9) << "Asg2" << setw(9) << "Test"
            << setw(9) << "Quiz" << setw(9) << "Exam" << "\n";

        cout << string(60, '-') << endl;

        while (getline(inFile, line)) {
            istringstream iss(line);

            // Extract the name 
            student.name = "";
            char ch;
            while (iss.get(ch) && !isdigit(ch)) {
                student.name += ch;
            }

            // Scores after name
            iss.putback(ch);
            iss >> student.asg1 >> student.asg2 >> student.test
                >> student.quiz >> student.exam;

            // Write scores to Results.txt File
            computeMarks(student);
            computeGrade(student);
            studentResults(student);

            // Display Student data In Console
            cout << left << setw(18) << student.name
                << setw(9) << fixed << setprecision(2) << static_cast<double>(student.asg1)
                << setw(9) << fixed << setprecision(2) << static_cast<double>(student.asg2)
                << setw(9) << fixed << setprecision(2) << static_cast<double>(student.test)
                << setw(9) << fixed << setprecision(2) << static_cast<double>(student.quiz)
                << setw(9) << fixed << setprecision(2) << static_cast<double>(student.exam) << "\n";
        }

        inFile.close();
        resultSummary(student);
    }
    else
    {
        cerr << "Error: Unable to open file." << endl;
        return;
    }
}

// Main Function
int main()
{
    readStudentData();
    cout << endl;
    return 0;
}