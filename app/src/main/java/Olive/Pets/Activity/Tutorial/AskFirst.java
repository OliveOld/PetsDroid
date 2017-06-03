package Olive.Pets.Activity.Tutorial;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import Olive.Pets.MainActivity;
import Olive.Pets.R;

/**
 * 사용자에게 앱 사용이 처음인지 확인한다.
 */
public class AskFirst
        extends AppCompatActivity
{
    private Button yes;
    private Button no;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
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
                Intent intent = new Intent(AskFirst.this, InitDogProfile.class);
                startActivity(intent);
            }
        });

        no = (Button)findViewById(R.id.q1no1);
        no.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AskFirst.this, MainActivity.class);
                startActivity(intent);

                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

    }
}
