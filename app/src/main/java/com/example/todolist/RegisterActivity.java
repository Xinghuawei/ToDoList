package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.todolist.db.DatabaseHelper;

//interface ReturnResult{
//    public void re();
//        }

public class RegisterActivity extends AppCompatActivity {
    EditText passFirst, passSecnond;
    Button confirmPass,regToLog;
    DatabaseHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DatabaseHelper(this);
        setContentView(R.layout.activity_register);
        passFirst = findViewById(R.id.passReg);
        passSecnond = findViewById(R.id.confirmPassReg);
        confirmPass = findViewById(R.id.passRegBtn);
        regToLog = findViewById(R.id.regToLogin);
        regToLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                regToLogin();
            }
        });
        confirmPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstP = passFirst.getText().toString();
                String secondP = passSecnond.getText().toString();
                if (firstP.equals("") || secondP.equals("")) {
                    Toast.makeText(getApplicationContext(), "Field missing", Toast.LENGTH_SHORT).show();
                } else {
                    if (!firstP.equals(secondP)) {
                        Toast.makeText(getApplicationContext(), "Password not matching", Toast.LENGTH_SHORT).show();
                    }
                    else if(firstP.length()>16) {
                        Toast.makeText(getApplicationContext(), "Password should not be longer than 16 characters", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Boolean insert = db.insert(firstP);
                        if(insert==true){
                            Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getApplicationContext(), "Fail to create", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
    }



    private void regToLogin(){
        Intent i = new Intent(this,LoginActivity.class);
//        ReturnResult r=()->{
//            Toast.makeText(getApplicationContext(), "Back to Login page", Toast.LENGTH_SHORT).show();
//        }
        startActivity(i);
    }




}
