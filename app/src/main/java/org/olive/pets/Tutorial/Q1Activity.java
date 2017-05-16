package org.olive.pets.Tutorial;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import org.olive.pets.R;


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
                Intent intent = new Intent(Q1Activity.this, InitDogProfileActivity.class);
                startActivity(intent);
            }
        });

    }
}
