package org.olive.pets.Tutorial;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import org.olive.pets.R;

public class Q1yes1Activity extends AppCompatActivity {
    private Button next1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_q1yes1);

        next1=(Button)findViewById(R.id.info_input1);
        next1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Q1yes1Activity.this,InitDogProfileActivity.class);
                startActivity(intent);
            }
    });
}
}