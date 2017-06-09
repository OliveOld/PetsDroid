package org.olive.pets.BLE;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.punchthrough.bean.sdk.Bean;
import com.punchthrough.bean.sdk.BeanDiscoveryListener;
import com.punchthrough.bean.sdk.BeanListener;
import com.punchthrough.bean.sdk.BeanManager;
import com.punchthrough.bean.sdk.message.BeanError;
import com.punchthrough.bean.sdk.message.Callback;
import com.punchthrough.bean.sdk.message.DeviceInfo;
import com.punchthrough.bean.sdk.message.ScratchBank;

import org.olive.pets.DB.PostureData;
import org.olive.pets.R;
import org.olive.pets.Tutorial.IntroActivity;
import org.olive.pets.Tutorial.Q1Activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

import static android.os.SystemClock.sleep;
import static android.view.View.VISIBLE;

/**
 * Created by KMJ on 2017-04-10.
 */

public class BluetoothActivity extends AppCompatActivity implements BeanDiscoveryListener, BeanListener{
    final String TAG = "BlueBean";
    final List<Bean> beans = new ArrayList<>();
    Bean bean = null;
    TextView tvConnect=null, tvData =null;
    String beanName;
    Realm mRealm;
    int discovery_flag = 0;
    ProgressBar progress;
    ImageButton btnConnect;
    int byteCnt = 1;
    byte tmp1byte = 0;
    byte[] tmp2byte = new byte[2];
    byte[] tmp6byte = new byte[6];
    Bean mBean;
    BeanPacket packet = new BeanPacket();
    PostureData dogPosture;
    int testCnt=1;
    String dataTime;
    private final static int REQUEST_ENABLE_BT = 1;
    Handler h;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

        //**********************actionbar_start**************************//
        // 액션바 title 지정
        getSupportActionBar().setTitle(" ");
        // 액션바 투명하게 해주기
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        // 색상넣기(투명색상 들어감)
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00ff0000")));
        // 왼쪽 화살표 버튼
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        //**********************actionbar_end**************************//


        BluetoothAdapter bt = BluetoothAdapter.getDefaultAdapter();
        if (bt == null){
            //Does not support Bluetooth
            //status.setText("Your device does not support Bluetooth");
        }else{
            //Magic starts. Let's check if it's enabled
            if (!bt.isEnabled()){
                Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
            }
        }

        tvData = (TextView)findViewById(R.id.bean_data);
        // 스크롤 텍스트뷰
        tvData.setMovementMethod(new ScrollingMovementMethod());

        tvConnect = (TextView)findViewById(R.id.bean_connect);
        progress = (ProgressBar)findViewById(R.id.progress_bean_connect);

        btnConnect = (ImageButton) findViewById(R.id.btn_bean_connect);
        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvConnect.setText("가장 가까운 기기를 찾고 있습니다...");
                BeanManager.getInstance().startDiscovery(BluetoothActivity.this);
                progress.setVisibility(VISIBLE);
                System.out.println("현재 시간 구하기 :: by Calendar..!!");
                Calendar cal = Calendar.getInstance();
                SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy:MM:dd");
                dataTime = sdf1.format(cal.getTime());
            }
        });

        mRealm = Realm.getDefaultInstance();

    }

    // 새로운 bean 찾았을 때 이름, 주소 보여줌
    @Override
    public void onBeanDiscovered(Bean bean, int rssi) {
        Log.d(TAG,"A bean is found: "+bean);
        StringBuffer aBuf= new StringBuffer(tvConnect.getText());
        aBuf.append("\n");
        //aBuf.append(""+bean.getDevice().getName()+" address: "+bean.getDevice().getAddress());
        tvConnect.setText(aBuf.toString());
        beans.add(bean);
    }

    // 탐색 완료되었을 때
    @Override
    public void onDiscoveryComplete() {
        progress.setVisibility(View.INVISIBLE);
        discovery_flag=1;
        StringBuffer aBuf= new StringBuffer(tvConnect.getText());
        aBuf.append("\n");
        aBuf.append("탐색이 끝났습니다.");
        tvConnect.setText(aBuf.toString());
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
        StringBuffer aBuf= new StringBuffer(tvConnect.getText());
        aBuf.append("\n");
        aBuf.append(beanName+"기기로의 연결이 완료되었습니다!");
        tvConnect.setText(aBuf.toString());
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

        sendRequest(BeanPacket.Oper.OP_Report, (byte)0, BeanPacket.Attr.A_Time);
    }

    @Override
    public void onConnectionFailed() {
        Log.d(TAG,"onConnectionFailed");
    }

    @Override
    public void onDisconnected() {
        Log.d(TAG,"onDisconnected");
        // text박스에 결과 출력
        //RealmResults<PostureData> postures = mRealm.where(PostureData.class).findAll();
        // 일단은 마지막 저장 된 값 된 놈 보이기
        //PostureData posture = postures.last();
        /*
        tvData.append("\nunknown: " + posture.getUnknown() + "\n"
                + "Lie: " + posture.getLie() + "\n"
                + "LieBack: " + posture.getLieBacke()  + "\n"
                + "LieSide: " + posture.getLieSide()  + "\n"
                + "Run: " + posture.getRun()   + "\n"
                + "Sit: " + posture.getSit() + "\n"
                + "Stand: " + posture.getStand() + "\n"
                + "Walk: " + posture.getWalk() + "\n"
                +"time: " + posture.getDate() + "\n"
        );
        */
        h= new Handler();
        h.postDelayed(mrun, 3000);
    }

    Runnable mrun = new Runnable(){
        @Override
        public void run(){
            //Intent i = new Intent(IntroActivity.this, Q1Activity.class); //인텐트 생성(현 액티비티, 새로 실행할 액티비티)
            //startActivity(i);

            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            //overridePendingTransition 이란 함수를 이용하여 fade in,out 효과를줌. 순서가 중요
        }
    };

    @Override
    public void onSerialMessageReceived(byte[] data) {
        Log.d(TAG,"onSerialMessageReceived");
        Log.d(TAG,"bytecnt: " + byteCnt);
        receiveResponse(data);
        Log.d(TAG,"data: " + data[0]);
        Log.d(TAG,"cnt: " + testCnt++);
    }

    public void sendRequest(byte op, byte pos, byte att) {
        switch(op) {
            case BeanPacket.Oper.OP_Discon:
                mBean.sendSerialMessage(packet.makeDisconnBytes());
                break;
            case BeanPacket.Oper.OP_Report:
                // Report 요청 : 총 8개의 자세 요청함 : 총 16바이트 보냄
                for (int i = 0; i < BeanPacket.Postures; i++)
                    mBean.sendSerialMessage(packet.makeReportngBytes((byte) i, att));
                break;
            case BeanPacket.Oper.OP_Sync:
                mBean.sendSerialMessage(packet.makeSynchronizeBytes(pos, att));
                break;
            case BeanPacket.Oper.OP_Train:
                mBean.sendSerialMessage(packet.makeTrainingBytes(pos));
                break;
        }
    }

    // 기기의 reposnse 받아 처리하는 함수
    public void receiveResponse(byte[] data) {
        switch(byteCnt) {
            // 첫번째 바이트 처리
            case 1: {
                switch (data[0]) {
                    case BeanPacket.Oper.OP_Discon:
                        //상관 없음 어짜피 연결 끝내니까
                        // 1바이트임
                        tmp1byte = -1;
                        byteCnt = 1;
                        break;
                    case BeanPacket.Oper.OP_Report:
                        tmp6byte[0] = data[0];
                        byteCnt++;
                        // byte cnt = 2가 됨
                        break;
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
            // 세번째 바이트 처리
            case 3:
                tmp6byte[5] = data[0];
                byteCnt++;
                break;
            case 4:
                tmp6byte[4] = data[0];
                byteCnt++;
                break;
            case 5:
                tmp6byte[3] = data[0];
                byteCnt++;
                break;
            case 6:
                tmp6byte[2] = data[0];
                for(int i=0; i<6; i++)
                    tvData.append(tmp6byte[i]+" ");
                // 다 받았을 경우에 해당하는 자세에 디비 저장 후 배열 초기화(는 안해도되겠지)
                saveDB();
                byteCnt=1;
                break;
        }
    }

    // 받아온 데이터 저장하는 함수
    public void saveDB(){

        RealmResults<PostureData> posture = mRealm.where(PostureData.class).equalTo("date", dataTime).findAll();

        // 이미 오늘 받아왔으면 => 이전 것에 덧씌우기
        if(posture.size() != 0) {
            final PostureData pos = posture.first();
            final int prev_unknown = pos.getUnknown();
            final int prev_lie = pos.getLie();
            final int prev_lieside = pos.getLieSide();
            final int prev_lieback = pos.getLieBacke();
            final int prev_sit = pos.getSit();
            final int prev_stand = pos.getStand();
            final int prev_walk = pos.getWalk();
            final int prev_run = pos.getRun();
            mRealm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    dogPosture = realm.where(PostureData.class).equalTo("date", dataTime).findFirst();
                    int value = (((int) tmp6byte[2] & 0xff) << 24 | ((int) tmp6byte[3] & 0xff) << 16 | ((int) tmp6byte[4] & 0xff) << 8 | ((int) tmp6byte[5] & 0xff));
                    //dogPosture.setDate(dataTime);
                    switch (packet.pos(tmp6byte[1])) {
                        case BeanPacket.Pos.P_Unknown:
                            dogPosture.setUnknown(prev_unknown + value);
                            break;
                        case BeanPacket.Pos.P_Lie:
                            dogPosture.setLie(prev_lie + value);
                            break;
                        case BeanPacket.Pos.P_LieSide:
                            dogPosture.setLieSide(prev_lieside + value);
                            break;
                        case BeanPacket.Pos.P_LieBack:
                            dogPosture.setLieBack(prev_lieback + value);
                            break;
                        case BeanPacket.Pos.P_Sit:
                            dogPosture.setSit(prev_sit + value);
                            break;
                        case BeanPacket.Pos.P_Stand:
                            dogPosture.setStand(prev_stand + value);
                            break;
                        case BeanPacket.Pos.P_Walk:
                            dogPosture.setWalk(prev_walk + value);
                            break;
                        case BeanPacket.Pos.P_Run:
                            dogPosture.setRun(prev_run + value);
                            break;
                    }
                }
            });
        } else {
            // 아니면 그냥 새로 저장
            mRealm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    dogPosture = realm.createObject(PostureData.class, dataTime);
                    int value = (((int) tmp6byte[2] & 0xff) << 24 | ((int) tmp6byte[3] & 0xff) << 16 | ((int) tmp6byte[4] & 0xff) << 8 | ((int) tmp6byte[5] & 0xff));
                    //dogPosture.setDate(dataTime);
                    switch (packet.pos(tmp6byte[1])) {
                        case BeanPacket.Pos.P_Unknown:
                            dogPosture.setUnknown(value);break;
                        case BeanPacket.Pos.P_Lie:
                            dogPosture.setLie(value);    break;
                        case BeanPacket.Pos.P_LieSide:
                            dogPosture.setLieSide(value);break;
                        case BeanPacket.Pos.P_LieBack:
                            dogPosture.setLieBack(value);break;
                        case BeanPacket.Pos.P_Sit:
                            dogPosture.setSit(value);  break;
                        case BeanPacket.Pos.P_Stand:
                            dogPosture.setStand(value);break;
                        case BeanPacket.Pos.P_Walk:
                            dogPosture.setWalk(value);break;
                        case BeanPacket.Pos.P_Run:
                            dogPosture.setRun(value);break;
                    }
                }
            });
        }
    }

    @Override
    public void onScratchValueChanged(ScratchBank bank, byte[] value) {
        Log.d(TAG,"onScratchValueChanged");
        Log.d(TAG,"bank: "+bank+"\tvalue: "+value);
    }

    @Override
    public void onError(BeanError error) {
        Log.d(TAG,"onError");
        Log.d(TAG,"error: "+error);
    }

}

