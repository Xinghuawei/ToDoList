package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.todolist.db.DatabaseHelper;

public class LoginActivity extends AppCompatActivity {
    EditText userIn;
    Button login,toReg;
    DatabaseHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        userIn = findViewById(R.id.userPassword);
        login = findViewById(R.id.logToList);
        toReg = findViewById(R.id.logToReg);
        db = new DatabaseHelper(this);

        if(checkExist()==true){
            toReg.setVisibility(View.GONE);
        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pass = userIn.getText().toString();
                if(pass.equals("")){
                    Toast.makeText(getApplicationContext(),"Missing password",Toast.LENGTH_SHORT).show();
                }else{
                    Boolean l = db.checkPassword(pass);
                    if(l==true){
                        logSuc();
                    }else{
                        Toast.makeText(getApplicationContext(),"Password not correct",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        toReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toReg();
            }
        });

    }

    private void logSuc(){
        Intent i = new Intent(this,MainActivity.class);
        startActivity(i);
    }

    private void toReg(){
        Intent i = new Intent(this,RegisterActivity.class);
        startActivity(i);
    }

    private Boolean checkExist() {
        db = new DatabaseHelper(this);
        Boolean r = db.checkPasswordExist();
        return r;
    }
}

