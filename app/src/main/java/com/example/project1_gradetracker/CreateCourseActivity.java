package com.example.project1_gradetracker;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project1_gradetracker.DB.Course;
import com.example.project1_gradetracker.DB.CourseDAO;
import com.example.project1_gradetracker.DB.User;

import java.util.List;

import static com.example.project1_gradetracker.LoginActivity.USER_NAME;
import static com.example.project1_gradetracker.LoginActivity.database;
import static com.example.project1_gradetracker.LoginActivity.userDAO;

public class CreateCourseActivity extends AppCompatActivity {

    private EditText Title;
    private EditText ID;    // integer
    private EditText Instructor;
    private EditText Start;
    private EditText End;
    private EditText Description;

    private Button Create;
    private TextView Remind;

    List<Course> courseList;
    public static CourseDAO courseDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_course);

        // TODO: once course overview is finished, move the courseDAO initialization to that class
        // initialize the DAO and populate the list with current existing courses

        courseDAO = database.courseDAO();
        courseList = courseDAO.getAllCourses();

        Title = (EditText)findViewById(R.id.etTitle);
        ID = (EditText)findViewById(R.id.etID);
        Instructor = (EditText)findViewById(R.id.etInstructor);
        Start = (EditText)findViewById(R.id.etStart);
        End = (EditText)findViewById(R.id.etEnd);
        Description = (EditText)findViewById(R.id.etDescription);
        Create = (Button)findViewById(R.id.btnCreateC);
        Remind = (TextView)findViewById(R.id.tvRemind);

        //Toast.makeText(CreateCourseActivity.this, courseDAO.getAllCourses().toString(), Toast.LENGTH_SHORT).show();

        Create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean courseExists = false;
                boolean switchActivity = true;

                Intent i = getIntent();
                String user_name = i.getStringExtra(USER_NAME);
                User user = null;

                for(User u : database.userDAO().getAllUsers()) {
                    if (u.getUsername().equals(user_name)) {
                        user = u;
                        break;
                    }
                }
                if(user == null){
                    Toast.makeText(CreateCourseActivity.this, "no user found", Toast.LENGTH_SHORT).show();
                }

                if(ID.getText().toString().isEmpty()){
                    ID.setError("ID field cannot be empty");
                    Toast.makeText(CreateCourseActivity.this, "ID cannot be empty", Toast.LENGTH_SHORT).show();
                    switchActivity = false;
                }
                if(Title.getText().toString().isEmpty()){
                    Title.setError("Title field cannot be empty");
                    Toast.makeText(CreateCourseActivity.this, "Title cannot be empty", Toast.LENGTH_SHORT).show();
                    switchActivity = false;
                }
                if(Instructor.getText().toString().isEmpty()){
                    Instructor.setError("Instructor field cannot be empty");
                    Toast.makeText(CreateCourseActivity.this, "Instructor cannot be empty", Toast.LENGTH_SHORT).show();
                    switchActivity = false;
                }

                if(!switchActivity){
                    return;
                }

                // populate course with data from screen (user input)
                Course course;
                String userID = user.getUSER_ID();
                int id = Integer.parseInt(ID.getText().toString());
                String title = Title.getText().toString();
                String instructor = Instructor.getText().toString();
                String desc = Description.getText().toString();
                course = new Course(userID, id, title, instructor, desc);

                // check if user entered course is already in the DB
                for(Course c : courseList){
                    // course exists, add course to user course-list
                    if(c.getCourseID() == course.getCourseID()){
                        courseExists = true;
                        if(user.getCourseList().contains(course)){
                            Toast.makeText(CreateCourseActivity.this, "This course is already in your Course List", Toast.LENGTH_SHORT).show();
                        } else {
                            user.addCourse(course);
                            courseDAO.update(course);
                            userDAO.update(user);
                            Toast.makeText(CreateCourseActivity.this, "Course Added to Course List", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    }
                }
                // if the course does not exist, add it to the database and user courselist
                if(!courseExists){
                    courseDAO.insert(course);
                    user.addCourse(course);
                    userDAO.update(user);
                    Toast.makeText(CreateCourseActivity.this, "Course Added to Database and Course List", Toast.LENGTH_SHORT).show();
                }
                if(switchActivity) {
                    Intent intent = OverallGradeActivity.getIntent(getApplicationContext(), user_name);
                    startActivity(intent);
                } else {
                    return;
                }
            }
        });
    }

    public static Intent getIntent(Context context, String username){
        Intent intent = new Intent(context, CreateCourseActivity.class);
        intent.putExtra(USER_NAME, username);

        return intent;
    }
}