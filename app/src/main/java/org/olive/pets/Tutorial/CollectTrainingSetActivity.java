package org.olive.pets.Tutorial;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import org.olive.pets.MainActivity;
import org.olive.pets.R;

public class CollectTrainingSetActivity extends AppCompatActivity {
    private Button btnSubmit;
    int tutorialFlag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect_training_set);

        //**********************actionbar_start**************************//
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //**********************actionbar_start**************************//

        //다음 페이지로 넘어가기 전에 완전히 튜토리얼 끝나면 endTutorial()로 마무리 해줘야함
        btnSubmit=(Button)findViewById(R.id.btn_submit_tutorial);
        btnSubmit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //튜토리얼 끝내고
                SharedPreferences shPref = getSharedPreferences("MyPref", 0);
                tutorialFlag = shPref.getInt("Flag", 0);

                // 맨 처음 실행 일 경우 => 메인으로 돌아가기
                if(tutorialFlag == 0) {
                    SharedPreferences.Editor prefEditor = shPref.edit();
                    prefEditor.putInt("Flag", ++tutorialFlag);
                    prefEditor.commit();
                    Intent intent = new Intent(CollectTrainingSetActivity.this, MainActivity.class);
                    finish();
                    startActivity(intent);
                } else {
                    // 프로필 추가에서 온 경우 => 리스트로 돌아가기
                    // 혹은 셋팅>데이터 더 받기에서 온 경우 => 돌아가기
                    finish();
                }
            }
        });
    }
}


