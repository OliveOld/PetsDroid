package org.olive.pets.Tutorial;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import org.olive.pets.MainActivity;
import org.olive.pets.R;


public class Q1Activity extends AppCompatActivity {
    private Button yes;
    private Button no;
    int tutorialFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_q1);

        //**********************actionbar_start**************************//
        getSupportActionBar().setTitle(" ");
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00ff0000")));
        //**********************actionbar_start**************************//


        yes = (Button)findViewById(R.id.q1yes1);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Q1Activity.this, InitDogProfileActivity.class);
                startActivity(intent);
            }
        });

        no = (Button)findViewById(R.id.q1no1);
        no.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                SharedPreferences shPref = getSharedPreferences("MyPref", 0);
                tutorialFlag = shPref.getInt("Flag", 0);
                SharedPreferences.Editor prefEditor = shPref.edit();
                prefEditor.putInt("Flag", ++tutorialFlag);
                prefEditor.commit();
                Intent intent = new Intent(Q1Activity.this, MainActivity.class);
                finish();
                startActivity(intent);

                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

    }
}
