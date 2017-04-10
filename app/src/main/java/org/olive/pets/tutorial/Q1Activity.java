package org.olive.pets.tutorial;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;


public class Q1Activity extends AppCompatActivity {
    private Button yes;
    private Button no;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_q1);

        yes = (Button)findViewById(R.id.q1yes1);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Q1Activity.this,Q1yes1Activity.class);
                startActivity(intent);
            }
        });

        no = (Button)findViewById(R.id.q1no1);
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Q1Activity.this,Q1no1Activity.class);
                startActivity(intent);
            }
        });


    }
}
