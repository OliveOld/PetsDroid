package org.olive.pets.tutorial;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.olive.pets.R;

public class Q1yes3Activity  extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_q1yes3);

        //다음 페이지로 넘어가기 전에 완전히 튜토리얼 끝나면 endTutorial()로 마무리 해줘야함
        endTutorial();
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

