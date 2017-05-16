package org.olive.pets;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.punchthrough.bean.sdk.Bean;
import com.punchthrough.bean.sdk.BeanDiscoveryListener;
import com.punchthrough.bean.sdk.BeanListener;
import com.punchthrough.bean.sdk.BeanManager;
import com.punchthrough.bean.sdk.message.BeanError;
import com.punchthrough.bean.sdk.message.Callback;
import com.punchthrough.bean.sdk.message.DeviceInfo;
import com.punchthrough.bean.sdk.message.ScratchBank;

import org.olive.pets.DB.DogProfile;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;

import static java.lang.Integer.parseInt;

/**
 * Created by KMJ on 2017-04-10.
 */

public class BluetoothActivity extends AppCompatActivity implements BeanDiscoveryListener, BeanListener {
    private String state;
    final String TAG = "BlueBean";
    final List<Bean> beans = new ArrayList<>();
    Bean bean = null;
    TextView tvConnect =null;
    TextView tvData =null;
    String beanName;
    int saveFlag=0;
    String dirPath, fileName;
    Realm mRealm;
    CheckTypesTask task;
    int discovery_flag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

        tvData = (TextView)findViewById(R.id.bean_data);
        // 스크롤 텍스트뷰
        tvData.setMovementMethod(new ScrollingMovementMethod());

        tvConnect = (TextView)findViewById(R.id.bean_connect);
        tvConnect.setText("Start Bluebean discovery ...");

        // 실험 시작
        Log.d(TAG,"Start Bluebean discovery ...");
        BeanManager.getInstance().startDiscovery(this);

        task = new CheckTypesTask();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_bt, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //테스트 중단
        if (id == R.id.test_cancel) {
            // 저장 플래그를 0으로 만들어 더이상 텍스트 뷰에 시리얼 값이 출력되지 않도록 한다.
            saveFlag=0;

            // 텍스트 뷰의 내용을 텍스트로 내보낸다.
            String data = tvData.getText().toString();

            try {
                fileName = "Test"+System.currentTimeMillis()+".txt";
                File f = new File(dirPath, fileName); // 경로, 파일명(계속 새로운 이름 만드려고 시간씀)
                FileWriter write = new FileWriter(f, false);
                PrintWriter out = new PrintWriter(write);
                out.println(data);
                out.close();
                // 저장 완료되었습니다 토스트
                Toast.makeText(this, "저장되었습니다.", Toast.LENGTH_SHORT).show();
                tvData.setText("");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }else {
            // 저장 플래그를 1로 만들어 텍스트뷰에 시리얼값이 출력되도록 한다.
            saveFlag=1;
            Toast.makeText(this, "측정 시작 및 값 저장 시작", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

    // 새로운 bean 찾았을 때 이름, 주소 보여줌
    @Override
    public void onBeanDiscovered(Bean bean, int rssi) {
        Log.d(TAG,"A bean is found: "+bean);
        StringBuffer aBuf= new StringBuffer(tvConnect.getText());
        aBuf.append("\n");
        aBuf.append(""+bean.getDevice().getName()+" address: "+bean.getDevice().getAddress());
        tvConnect.setText(aBuf.toString());
        beans.add(bean);

        // 프로그레스
        task.execute();
    }

    // 탐색 완료되었을 때
    @Override
    public void onDiscoveryComplete() {
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
    }

    // BeanListener Methods
    @Override
    public void onConnected() {
        StringBuffer aBuf= new StringBuffer(tvConnect.getText());
        aBuf.append("\n");
        aBuf.append(beanName+"의 연결이 완료되었습니다!\n약 10초 후 상단의 실험 시작 메뉴를 누르세요.");
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
    }

    @Override
    public void onConnectionFailed() {
        Log.d(TAG,"onConnectionFailed");
    }

    @Override
    public void onDisconnected() {
        Log.d(TAG,"onDisconnected");
    }

    @Override
    public void onSerialMessageReceived(byte[] data) {
        String byteToString = new String(data, 0, data.length);
        Log.d(TAG,"onSerialMessageReceived");
        Log.d(TAG,"data: "+byteToString);
        // byte to string

        // 받아온 데이터 저장
        if(saveFlag==1) {
            // 저장 일 경우에만
        }

        // 여기서 DB 저장
        saveDB();
    }

    public void saveDB(){
        // DB 초기화
        mRealm = Realm.getDefaultInstance();
        // DB 데이터 넣기
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                DogProfile myDog = realm.where(DogProfile.class).equalTo("id", 1).findFirst();
                // fake data 일단 넣음
                myDog.setPostureData(1, "2017-04-17", 36000, 40000, 20000, 10000);
            }
        });
        Toast.makeText(this, "강아지 자세가 저장되었습니다.", Toast.LENGTH_SHORT).show();
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


    // Progress Spinner 구현 부분
   private class CheckTypesTask extends AsyncTask<Void, Void, Void> {

        ProgressDialog asyncDialog = new ProgressDialog(BluetoothActivity.this);

        @Override
        protected void onPreExecute() {
            asyncDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            asyncDialog.setMessage("기기를 찾고 있습니다..");

            // show dialog
            asyncDialog.show();
            super.onPreExecute();
        }

        // 진행중, 진행 정도를 표시
        @Override
        protected Void doInBackground(Void... arg0) {
            try {
                for (int i = 0; i < 5; i++) {
                    //asyncDialog.setProgress(i * 30);
                    Thread.sleep(500);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        // 종료 기능
        @Override
        protected void onPostExecute(Void result) {
            asyncDialog.dismiss();
            super.onPostExecute(result);
        }
    }
}

