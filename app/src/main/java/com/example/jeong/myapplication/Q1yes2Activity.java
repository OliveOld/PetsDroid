package com.example.jeong.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Q1yes2Activity extends AppCompatActivity{
    private Button next2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_q1yes2);

        next2=(Button)findViewById(R.id.info_input2);
        next2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Q1yes2Activity.this,Q1yes3Activity.class);
                startActivity(intent);

            }
        });
    }
}