package com.example.project1_gradetracker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.project1_gradetracker.DB.Assignment;

public class AddAssignment extends AppCompatActivity {

    private EditText Title;
    private EditText ID;
    private EditText DueDate;
    private EditText Points;
    private EditText Category;
    private Button Add;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_assignment);

        Title = (EditText)findViewById(R.id.etAssignmentTitle);
        ID = (EditText)findViewById(R.id.etAssignmentID);
        DueDate = (EditText)findViewById(R.id.etAssignmentDue);
        Points = (EditText)findViewById(R.id.etAssignmentPoints);
        Category = (EditText)findViewById(R.id.etAssignmentCategory);
        Add = (Button)findViewById(R.id.btAddAssignment);

        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean AssignmentExists = false;

                Assignment assignment = createNewAssignment();

            }
        });
    }

    private Assignment createNewAssignment(){
        Assignment assignment = new Assignment();

        assignment.setTitle(Title.getText().toString());
        assignment.setAssignmentID(Integer.parseInt(ID.getText().toString()));
        assignment.setDueDate(DueDate.getText().toString());
        assignment.setPoints(Integer.parseInt(Points.getText().toString()));
        assignment.setCategory(Category.getText().toString());

        return assignment;
    }
}