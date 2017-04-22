package org.olive.pets.tutorial;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import org.olive.pets.MainActivity;
import org.olive.pets.R;

public class Q1yes3Activity  extends AppCompatActivity {
    private Button btnSubmit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_q1yes3);

        //다음 페이지로 넘어가기 전에 완전히 튜토리얼 끝나면 endTutorial()로 마무리 해줘야함
        btnSubmit=(Button)findViewById(R.id.btn_submit_tutorial);
        btnSubmit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //튜토리얼 끝내고
                endTutorial();
                // 액티비티 전환
                Intent intent=new Intent(Q1yes3Activity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    public void endTutorial()
    {
        SharedPreferences shPref = getSharedPreferences("MyPref", 0);
        int tutorialFlag = 1;   // 튜토리얼 완료했음은 1로 표현
        SharedPreferences.Editor prefEditor = shPref.edit();
        prefEditor.putInt("Flag", tutorialFlag);
        prefEditor.commit();
    }


}

