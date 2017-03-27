package org.olive.pets;

/**
 * Created by KMJ on 2017-03-26.
 */

import android.os.Bundle;
import android.os.Environment;
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

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class BluetoothActivity extends AppCompatActivity implements BeanDiscoveryListener, BeanListener {
    private String state;
    final String TAG = "BlueBean";
    final List<Bean> beans = new ArrayList<>();
    Bean bean = null;
    TextView tvConnect =null;
    TextView tvData =null;
    String beanName;
    int saveFlag=0;
    String dirPath;
    String fileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

        checkExternalStorage(); // 외부메모리 상태 확인 메서드

        String str = Environment.getExternalStorageState();
        if ( str.equals(Environment.MEDIA_MOUNTED)) {
            dirPath = Environment.getExternalStorageDirectory()+"/PetTrackBean";
            File file = new File(dirPath);
            if (!file.exists())  // 원하는 경로에 폴더가 있는지 확인
                file.mkdirs();
        }

        // 저장될 파일 이름 지정 (시간값 사용)


        tvData = (TextView)findViewById(R.id.bean_data);
        // 스크롤 텍스트뷰
        tvData.setMovementMethod(new ScrollingMovementMethod());
        tvConnect = (TextView)findViewById(R.id.bean_connect);
        tvConnect.setText("Start Bluebean discovery ...");

        // 실험 시작
        Log.d(TAG,"Start Bluebean discovery ...");
        BeanManager.getInstance().startDiscovery(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_bt, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //테스트 중단
        if (id == R.id.test_cancel) {
            // 저장 플래그를 0으로 만들어 더이상 텍스트 뷰에 시리얼 값이 출력되지 않도록 한다.
            saveFlag=0;

            // 텍스트 뷰의 내용을 텍스트로 내보낸다.
            if (!checkExternalStorage()) return true;

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

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG,"MainActivity onResume");
    }

    @Override
    protected void onStop() {
        Log.d(TAG,"MainActivity.onStop");
        super.onStop();
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
    }

    // 탐색 완료되었을 때
    @Override
    public void onDiscoveryComplete() {
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

        //LedColor ledColor = LedColor.create(0,0,60);
        //bean.setLed(ledColor);
        /*
        bean.readTemperature(new Callback<Integer>() {
            @Override
            public void onResult(Integer data){
                Log.d(TAG, "Temperature: "+data);
                LedColor ledColor = LedColor.create(0,0,0);
                bean.setLed(ledColor);
            }
        });
        */
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


        // 저장 버튼 누를 때만 텍스트뷰에 출력력
        if(saveFlag==1) {


            // 시리얼로 보내는 메시지 텍스트뷰에 출력
            tvData.append(byteToString);
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
    /**
     * 외부메모리 상태 확인 메서드
     */
    boolean checkExternalStorage() {
        state = Environment.getExternalStorageState();
        // 외부메모리 상태
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            // 읽기 쓰기 모두 가능
            Log.d("test", "외부메모리 읽기 쓰기 모두 가능");
            Toast.makeText(this, "외부메모리 읽기 쓰기 모두 가능", Toast.LENGTH_SHORT).show();
            return true;
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)){
            //읽기전용
            Log.d("test", "외부메모리 읽기만 가능");
            Toast.makeText(this, "외부메모리 읽기만 가능", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            // 읽기쓰기 모두 안됨
            Log.d("test", "외부메모리 읽기쓰기 모두 안됨 : "+ state);
            Toast.makeText(this, "외부메모리 읽기쓰기 모두 안됨 : "+ state, Toast.LENGTH_SHORT).show();
            return false;
        }
    }


}
