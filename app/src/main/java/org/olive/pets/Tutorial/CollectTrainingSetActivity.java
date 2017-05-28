package org.olive.pets.Tutorial;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
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
import org.olive.pets.BLE.BluetoothActivity;
import org.olive.pets.DB.PostureData;
import org.olive.pets.MainActivity;
import org.olive.pets.R;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

import static android.os.SystemClock.sleep;

public class CollectTrainingSetActivity extends BluetoothActivity implements BeanDiscoveryListener, BeanListener {
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
    int testCnt=1;
    ImageButton imgbtnWalk, imgbtnRun, imgbtnLie, imgbtnStand, imgbtnSeat, imgbtnLieBack, imgbtnLieSide;
    boolean flagConnect=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect_training_set);

        //**********************actionbar_start**************************//
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //**********************actionbar_start**************************//

        //다음 페이지로 넘어가기 전에 완전히 튜토리얼 끝나면 endTutorial()로 마무리 해줘야함
        btnSubmit = (Button) findViewById(R.id.btn_submit_tutorial);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //튜토리얼 끝내고
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
                    finish();
                }
            }
        });

        mRealm = Realm.getDefaultInstance();
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                dogPosture = realm.createObject(PostureData.class);
            }
        });

        ImageButton.OnClickListener onClickListener = new ImageButton.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.imgbtn_walk :
                        sendRequest(BeanPacket.Oper.OP_Train, BeanPacket.Pos.P_Walk, (byte)0);
                        break ;
                    case R.id.imgbtn_run :
                        sendRequest(BeanPacket.Oper.OP_Train, BeanPacket.Pos.P_Run, (byte)0);
                        break ;
                    case R.id.imgbtn_lie :
                        sendRequest(BeanPacket.Oper.OP_Train, BeanPacket.Pos.P_Lie, (byte)0);
                        break ;
                    case R.id.imgbtn_lieback :
                        sendRequest(BeanPacket.Oper.OP_Train, BeanPacket.Pos.P_LieBack, (byte)0);
                        break ;
                    case R.id.imgbtn_stand :
                        sendRequest(BeanPacket.Oper.OP_Train, BeanPacket.Pos.P_Stand, (byte)0);
                        break ;
                    case R.id.imgbtn_seat :
                        sendRequest(BeanPacket.Oper.OP_Train, BeanPacket.Pos.P_Sit, (byte)0);
                        break ;
                    case R.id.imgbtn_lieside :
                        sendRequest(BeanPacket.Oper.OP_Train, BeanPacket.Pos.P_LieSide, (byte)0);
                        break ;
                }
            }
        } ;

        if(flagConnect) {
            imgbtnWalk = (ImageButton) findViewById(R.id.imgbtn_walk);
            imgbtnRun = (ImageButton) findViewById(R.id.imgbtn_run);
            imgbtnLie = (ImageButton) findViewById(R.id.imgbtn_lie);
            imgbtnStand = (ImageButton) findViewById(R.id.imgbtn_stand);
            imgbtnSeat = (ImageButton) findViewById(R.id.imgbtn_seat);
            imgbtnLieBack = (ImageButton) findViewById(R.id.imgbtn_lieback);
            imgbtnLieSide = (ImageButton) findViewById(R.id.imgbtn_lieside);
            imgbtnWalk.setOnClickListener(onClickListener);
            imgbtnRun.setOnClickListener(onClickListener);
            imgbtnLie.setOnClickListener(onClickListener);
            imgbtnStand.setOnClickListener(onClickListener);
            imgbtnSeat.setOnClickListener(onClickListener);
            imgbtnLieBack.setOnClickListener(onClickListener);
            imgbtnLieSide.setOnClickListener(onClickListener);
        }

        BeanManager.getInstance().startDiscovery(CollectTrainingSetActivity.this);
    }

    // 새로운 bean 찾았을 때 이름, 주소 보여줌
    @Override
    public void onBeanDiscovered(Bean bean, int rssi) {
        Log.d(TAG,"A bean is found: "+bean);
        beans.add(bean);
    }

    // 탐색 완료되었을 때
    @Override
    public void onDiscoveryComplete() {
//        progress.setVisibility(View.INVISIBLE);
        discovery_flag=1;
        for (Bean bean : beans) {
            Log.d(TAG, "Bean name: "+bean.getDevice().getName());
            Log.d(TAG, "Bean address: "+bean.getDevice().getAddress());
        }
        if(beans.size()>0){
            bean=beans.get(0);
            bean.connect(this,this);
            // 연결한 기기 이름 가져오기
            beanName=bean.getDevice().getName();
        }
        mBean = bean;
    }

    // BeanListener Methods
    @Override
    public void onConnected() {
        Log.d(TAG,"connected to Bean! ");
        bean.readDeviceInfo(new Callback<DeviceInfo>() {
            @Override
            public void onResult(DeviceInfo deviceInfo) {
                Log.d(TAG,deviceInfo.hardwareVersion());
                Log.d(TAG,deviceInfo.firmwareVersion());
                Log.d(TAG,deviceInfo.softwareVersion());
            }
        });
        sleep(10000);

        Toast toast = Toast.makeText(CollectTrainingSetActivity.this, "버튼을 누르세요.", Toast.LENGTH_SHORT);
        toast.show();
        flagConnect=true;
    }

    @Override
    public void onConnectionFailed() {
        Log.d(TAG,"onConnectionFailed");
    }

    @Override
    public void onDisconnected() {
        Log.d(TAG,"onDisconnected");
        // text박스에 결과 출력
        RealmResults<PostureData> postures = mRealm.where(PostureData.class).findAll();
        postures = mRealm.where(PostureData.class).findAll();
        // 일단은 마지막 저장 된 값 된 놈 보이기
        PostureData posture = postures.last();
    }

    @Override
    public void onSerialMessageReceived(byte[] data) {
        receiveResponse(data);
        //String byteToString = new String(data, 0, data.length);
        for(int i = 0; i<data.length;i++) {
            Log.d(TAG,"onSerialMessageReceived");
            Log.d(TAG,"data: " + data[i]);
            Log.d(TAG,"bytecnt: " + byteCnt);
            Log.d(TAG,"cnt: " + testCnt++);
        }
    }

    @Override
    public void onScratchValueChanged(ScratchBank bank, byte[] value) {
        Log.d(TAG,"onScratchValueChanged");
        Log.d(TAG,"bank: "+bank+"\tvalue: "+value);
    }

    @Override
    public void onError(BeanError error) {
        Log.d(TAG, "onError");
        Log.d(TAG, "error: " + error);
        }
}


