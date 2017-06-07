package org.olive.pets.Tutorial;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.punchthrough.bean.sdk.Bean;
import com.punchthrough.bean.sdk.BeanDiscoveryListener;
import com.punchthrough.bean.sdk.BeanListener;
import com.punchthrough.bean.sdk.BeanManager;
import com.punchthrough.bean.sdk.message.BeanError;
import com.punchthrough.bean.sdk.message.Callback;
import com.punchthrough.bean.sdk.message.DeviceInfo;
import com.punchthrough.bean.sdk.message.ScratchBank;

import org.olive.pets.BLE.BeanPacket;
import org.olive.pets.DB.PostureData;
import org.olive.pets.MainActivity;
import org.olive.pets.R;
import org.olive.pets.SettingActivity;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

import static android.os.SystemClock.sleep;

public class CollectTrainingSetActivity extends AppCompatActivity implements BeanDiscoveryListener, BeanListener, View.OnClickListener {
    private Button btnSubmit;
    int tutorialFlag;
    private String state;
    final String TAG = "BlueBean";
    final List<Bean> beans = new ArrayList<>();
    Bean bean = null;
    String beanName;
    Realm mRealm;
    int discovery_flag = 0;
    ProgressBar progress;
    int byteCnt = 1;
    byte tmp1byte = 0;
    byte[] tmp2byte = new byte[2];
    byte[] tmp6byte = new byte[6];
    Bean mBean;
    BeanPacket packet = new BeanPacket();
    PostureData dogPosture;
    int testCnt = 1;
    ImageButton imgbtnWalk, imgbtnRun, imgbtnLie, imgbtnStand, imgbtnSeat, imgbtnLieBack, imgbtnLieSide;
    LinearLayout layoutButton, layoutConnect, layoutProgress;
    Button btnTraining, btnConnect, btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect_training_set);

        //**********************actionbar_start**************************//
        // 액션바 title 지정
        getSupportActionBar().setTitle(" ");
        // 액션바 투명하게 해주기
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        // 색상넣기(투명색상 들어감)
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00ff0000")));
        // 왼쪽 화살표 버튼
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //**********************actionbar_start**************************//

        //**********************컨트롤들 선언부**************************//

        layoutConnect = (LinearLayout)findViewById(R.id.layout_connect);

        layoutButton = (LinearLayout)findViewById(R.id.layout_button);
        layoutButton.setVisibility(View.INVISIBLE);

        layoutProgress = (LinearLayout)findViewById(R.id.layout_progress);
        layoutProgress.setVisibility(View.INVISIBLE);

        // 튜토리얼 끝내기
        btnCancel = (Button)findViewById(R.id.btn_bt_cancel);
        btnCancel.setOnClickListener(this);
        // 기기 연결
        btnConnect = (Button) findViewById(R.id.btn_bt_training);
        btnConnect.setOnClickListener(this);

        btnSubmit = (Button) findViewById(R.id.btn_submit_tutorial);
        btnSubmit.setOnClickListener(this);

        imgbtnWalk = (ImageButton) findViewById(R.id.imgbtn_walk);
        imgbtnRun = (ImageButton) findViewById(R.id.imgbtn_run);
        imgbtnLie = (ImageButton) findViewById(R.id.imgbtn_lie);
        imgbtnStand = (ImageButton) findViewById(R.id.imgbtn_stand);
        imgbtnSeat = (ImageButton) findViewById(R.id.imgbtn_seat);
        imgbtnLieBack = (ImageButton) findViewById(R.id.imgbtn_lieback);
        imgbtnLieSide = (ImageButton) findViewById(R.id.imgbtn_lieside);
        imgbtnWalk.setOnClickListener(this);
        imgbtnRun.setOnClickListener(this);
        imgbtnLie.setOnClickListener(this);
        imgbtnStand.setOnClickListener(this);
        imgbtnSeat.setOnClickListener(this);
        imgbtnLieBack.setOnClickListener(this);
        imgbtnLieSide.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_submit_tutorial:
            case R.id.btn_bt_cancel:
                //다음 페이지로 넘어가기 전에 완전히 튜토리얼 끝나면 endTutorial()로 마무리 해줘야함
                if(mBean != null)
                    mBean.disconnect();
                SharedPreferences shPref = getSharedPreferences("MyPref", 0);
                tutorialFlag = shPref.getInt("Flag", 0);

                // 맨 처음 실행 일 경우 => 메인으로 돌아가기
                if (tutorialFlag == 0) {
                    SharedPreferences.Editor prefEditor = shPref.edit();
                    prefEditor.putInt("Flag", ++tutorialFlag);
                    prefEditor.commit();
                    Intent intent = new Intent(CollectTrainingSetActivity.this, MainActivity.class);
                    finish();
                    startActivity(intent);
                } else {
                    // 프로필 추가에서 온 경우 => 리스트로 돌아가기
                    // 혹은 셋팅>데이터 더 받기에서 온 경우 => 돌아가기
                    Intent intent = new Intent(CollectTrainingSetActivity.this, SettingActivity.class);
                    finish();
                    startActivity(intent);
                }
                break;
            case R.id.btn_bt_training:
                // 기기 연결 시도
                BeanManager.getInstance().startDiscovery(CollectTrainingSetActivity.this);
                layoutProgress.setVisibility(View.VISIBLE);
                break;
            case R.id.imgbtn_walk:
                mBean.sendSerialMessage(packet.makeTrainingBytes(BeanPacket.Pos.P_Walk));
                break;
            case R.id.imgbtn_run:
                mBean.sendSerialMessage(packet.makeTrainingBytes(BeanPacket.Pos.P_Run));
                break;
            case R.id.imgbtn_lie:
                mBean.sendSerialMessage(packet.makeTrainingBytes(BeanPacket.Pos.P_Lie));
                break;
            case R.id.imgbtn_lieback:
                mBean.sendSerialMessage(packet.makeTrainingBytes(BeanPacket.Pos.P_LieBack));
                break;
            case R.id.imgbtn_stand:
                mBean.sendSerialMessage(packet.makeTrainingBytes(BeanPacket.Pos.P_Stand));
                break;
            case R.id.imgbtn_seat:
                mBean.sendSerialMessage(packet.makeTrainingBytes(BeanPacket.Pos.P_Sit));
                break;
            case R.id.imgbtn_lieside:
                mBean.sendSerialMessage(packet.makeTrainingBytes(BeanPacket.Pos.P_LieSide));
                break;
        }
    }

    // 새로운 bean 찾았을 때 이름, 주소 보여줌
    @Override
    public void onBeanDiscovered(Bean bean, int rssi) {
        Log.d(TAG, "A bean is found: " + bean);
        beans.add(bean);
    }

    // 탐색 완료되었을 때
    @Override
    public void onDiscoveryComplete() {
//        progress.setVisibility(View.INVISIBLE);
        discovery_flag = 1;
        for (Bean bean : beans) {
            Log.d(TAG, "Bean name: " + bean.getDevice().getName());
            Log.d(TAG, "Bean address: " + bean.getDevice().getAddress());
        }
        if (beans.size() > 0) {
            bean = beans.get(0);
            bean.connect(this, this);
            // 연결한 기기 이름 가져오기
            beanName = bean.getDevice().getName();
        }
        mBean = bean;
    }

    // BeanListener Methods
    @Override
    public void onConnected() {
        Log.d(TAG, "connected to Bean! ");
        bean.readDeviceInfo(new Callback<DeviceInfo>() {
            @Override
            public void onResult(DeviceInfo deviceInfo) {
                Log.d(TAG, deviceInfo.hardwareVersion());
                Log.d(TAG, deviceInfo.firmwareVersion());
                Log.d(TAG, deviceInfo.softwareVersion());
            }
        });
        sleep(10000);

        layoutProgress.setVisibility(View.INVISIBLE);
        layoutConnect.setVisibility(View.INVISIBLE);
        layoutButton.setVisibility(View.VISIBLE);
        layoutButton.bringToFront();
    }

    @Override
    public void onConnectionFailed() {
        Log.d(TAG, "onConnectionFailed");
    }

    @Override
    public void onDisconnected() {
        Log.d(TAG, "onDisconnected");
        Toast toast = Toast.makeText(CollectTrainingSetActivity.this, "연결이 끊겼습니다.", Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    public void onSerialMessageReceived(byte[] data) {
        Log.d(TAG,"onSerialMessageReceived");
        Log.d(TAG,"bytecnt: " + byteCnt);
        receiveResponse(data);
        Log.d(TAG,"data: " + data[0]);
        Log.d(TAG,"cnt: " + testCnt++);
    }

    @Override
    public void onScratchValueChanged(ScratchBank bank, byte[] value) {
        Log.d(TAG, "onScratchValueChanged");
        Log.d(TAG, "bank: " + bank + "\tvalue: " + value);
    }

    @Override
    public void onError(BeanError error) {
        Log.d(TAG, "onError");
        Log.d(TAG, "error: " + error);
    }

    // 기기의 reposnse 받아 처리하는 함수
    public void receiveResponse(byte[] data) {
        switch(byteCnt) {
            // 첫번째 바이트 처리
            case 1: {
                switch (data[0]) {
                    case BeanPacket.Oper.OP_Train:
                        tmp2byte[0] = data[0];
                        byteCnt++;
                        // byte cnt = 2가 됨
                        break;
                    case BeanPacket.Oper.OP_Sync:
                        tmp2byte[0] = data[0];
                        byteCnt++;
                        // byte cnt = 2가 됨
                        break;
                }
                break;
            }
            // 두번째 바이트 처리 여기서부턴 Oper로 나눔
            case 2:
            {
                if(tmp2byte[0]==BeanPacket.Oper.OP_Train) {
                    tmp2byte[1]=data[0];
                    byteCnt = 1;
                } else if(tmp2byte[0]== BeanPacket.Oper.OP_Sync) {
                    tmp2byte[1]=data[0];
                    byteCnt = 1;
                }
                if(tmp6byte[0]==BeanPacket.Oper.OP_Report) {
                    tmp6byte[1] = data[0];
                    byteCnt++;
                }
            }
            break;
        }
    }
}


