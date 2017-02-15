package com.example.seobink.bluetoothkim;

//  이것은 주석이다...

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Set;
import java.util.UUID;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
{
    // DB용 변수다
    SQLiteDatabase db;
    MySQLiteHelper helper;

    private final static int DEVICES_DIALOG = 1;
    private final static int ERROR_DIALOG = 2;

    public static Context mContext;
    public static AppCompatActivity activity;

    TextView myLabel, mRecv, text;
    EditText myTextbox;
    static BluetoothAdapter mBluetoothAdapter;
    BluetoothSocket mmSocket;
    BluetoothDevice mmDevice;
    OutputStream mmOutputStream;
    InputStream mmInputStream;
    Thread workerThread;
    byte[] readBuffer;
    int readBufferPosition;
    volatile boolean stopWorker;
    int flag=0;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button sendButton = (Button)findViewById(R.id.send);
        //Button scanButton = (Button)findViewById(R.id.button_scan);
        myLabel = (TextView)findViewById(R.id.label);
        myTextbox = (EditText)findViewById(R.id.entry);
        mRecv = (TextView)findViewById(R.id.recv);
        text= (TextView)findViewById(R.id.result);

        mContext = this;
        activity=this;

        //1.블루투스 사용 가능한지 검사합니다.
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (mBluetoothAdapter == null) {
            ErrorDialog("This device is not implement Bluetooth.");
            return;
        }

        // 사용자폰에 블루투스 안켜져 잇을 때
        if (!mBluetoothAdapter.isEnabled()) {
            ErrorDialog("This device is disabled Bluetooth.");
            return;
        }
        else {
            try  {
                DeviceDialog();
                //블루투스 사용가능
                //2. 페어링 되어 있는 블루투스 장치들의 목록을 보여줍니다.
                //3. 목록에서 블루투스 장치를 선택하면 선택한 디바이스를 인자로 하여 doConnect 함수가 호출됩니다.
                //
            } catch (Exception e)
            {
                ErrorDialog("연결실패1");
            }
        }



        //11. Send 버튼을 누르면 sendData함수가 호출됩니다.
        sendButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    sendData();
                } catch (IOException ex) {
                }
            }
        });

        helper = new MySQLiteHelper(MainActivity.this, "petinfo.db",null,1 );

       // if()
    }

    /* 옵션 메뉴 관련 메소드 시작 */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 옵션 메뉴 이어주기
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // 옵션 메뉴의 아이템 눌렸을 때
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.bluetooth_scan) {
            try {
                mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                DeviceDialog();
            }
            catch (Exception e)
            {
                ErrorDialog("재연결실패");
            }
            return true;
        }
        else {
            doClose();
            mRecv.setText(" ");
            myLabel.setText("disconnect");
        }
        return super.onOptionsItemSelected(item);
    }
    /* 옵션 메뉴 관련 메소드 끝 */

    /* DB 관련 메소드 시작 */
    public void insert(float accel_X, float accel_Y, float accel_Z, float gyro_X,float gyro_Y,float gyro_Z ) {
        db = helper.getWritableDatabase(); // db 객체를 얻어온다. 쓰기 가능
        ContentValues values = new ContentValues();
        // db.insert의 매개변수인 values가 ContentValues 변수이므로 그에 맞춤
        // 데이터의 삽입은 put을 이용한다.
        values.put("accel_X", accel_X);
        values.put("accel_Y", accel_Y);
        values.put("accel_Z", accel_Z);
        values.put("gyro_X", gyro_X);
        values.put("gyro_Y", gyro_Y);
        values.put("gyro_Z", gyro_Z);
        db.insert("DATA", null, values);
    }

    public void select() {
        // 1) db의 데이터를 읽어와서, 2) 결과 저장, 3)해당 데이터를 꺼내 사용
        db = helper.getReadableDatabase(); // db객체를 얻어온다. 읽기 전용
        Cursor c = db.query("DATA", null, null, null, null, null, null);
        /*
         * 위 결과는 select * from DATA 가 된다. Cursor는 DB결과를 저장한다. public Cursor
         * query (String table, String[] columns, String selection, String[]
         * selectionArgs, String groupBy, String having, String orderBy)
         */
        while (c.moveToNext()) {
            // c의 int가져와라 ( c의 컬럼 중 id) 인 것의 형태이다.
            int _id = c.getInt(c.getColumnIndex("_id"));
            float accel_X = c.getFloat(c.getColumnIndex("accel_X"));
            float accel_Y = c.getFloat(c.getColumnIndex("accel_Y"));
            float accel_Z = c.getFloat(c.getColumnIndex("accel_Z"));
            float gyro_X = c.getFloat(c.getColumnIndex("gyro_X"));
            float gyro_Y = c.getFloat(c.getColumnIndex("gyro_Y"));
            float gyro_Z = c.getFloat(c.getColumnIndex("gyro_Z"));
            Log.i("db", "id: " + _id + ", accel_X : " + accel_X + ", accel_Y : " + accel_Y
                    + ", accel_Z : " + accel_Z+ ", gyro_X : " + gyro_X+ ", gyro_Y : " + gyro_Y+ ", gyro_Z : " + gyro_Z);
        }
    }
    /* DB 관련 메소드 끝 */

    /* 블루투스 관련 메소드 시작 */
    static public Set<BluetoothDevice> getPairedDevices() {
        // 블루투스 사용가능한지 검사하는 함수
        return mBluetoothAdapter.getBondedDevices();
    }

    // 백버튼
    @Override
    public void onBackPressed() {
        doClose();
        super.onBackPressed();
    }

    //13. 백버튼이 눌러지거나, ConnectTask에서 예외발생시
    //데이터 수신을 위한 스레드를 종료시키고 CloseTask를 실행하여 입출력 스트림을 닫고,
    //소켓을 닫아 통신을 종료합니다.
    public void doClose() {
        try {
            //workerThread.interrupt();
            new CloseTask().execute();
        }
        catch (Exception ex)
        {
            ErrorDialog("연결실패");
        }
    }

    // 연결시도
    public void doConnect(BluetoothDevice device) {
        mmDevice = device;

        //Standard SerialPortService ID
        UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

        try {
            // 4. 지정한 블루투스 장치에 대한 특정 UUID 서비스를 하기 위한 소켓을 생성합니다.
            // 여기선 시리얼 통신을 위한 UUID를 지정하고 있습니다.
            mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
            // 5. 블루투스 장치 검색을 중단합니다.
            mBluetoothAdapter.cancelDiscovery();
            // 6. ConnectTask를 시작합니다.
            new ConnectTask().execute();
        }
        catch (IOException e) {
            Log.e("", e.toString(), e);
            ErrorDialog("doConnect "+e.toString());
        }
    }

    // ConnectTask
    private class ConnectTask extends AsyncTask<Void, Void, Object> {
        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Object doInBackground(Void... params) {
            try {
                //7. 블루투스 장치로 연결을 시도합니다.
                mmSocket.connect();

                //8. 소켓에 대한 입출력 스트림을 가져옵니다.
                mmOutputStream = mmSocket.getOutputStream();
                mmInputStream = mmSocket.getInputStream();

                //9. 데이터 수신을 대기하기 위한 스레드를 생성하여 입력스트림로부터의 데이터를 대기하다가
                //   들어오기 시작하면 버퍼에 저장합니다.
                //  '\n' 문자가 들어오면 지금까지 버퍼에 저장한 데이터를 UI에 출력하기 위해 핸들러를 사용합니다.
                beginListenForData();
            } catch (Throwable t) {
                Log.e( "", "connect? "+ t.getMessage() );
                doClose();
                return t;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            /*
            //10. 블루투스 통신이 연결되었음을 화면에 출력합니다.
            myLabel.setText("Bluetooth Opened");
            if (result instanceof Throwable)
            {
                Log.d("","ConnectTask "+result.toString() );
                ErrorDialog("ConnectTask "+result.toString());
            }
            */
            // 블루투스 통신이 연결된 유무 판별
            myLabel.setText("Bluetooth Opened");
            if (result instanceof Throwable) {
                // Log.d("", "ConnectTask " + result.toString());
                myLabel.setText("Disconnect");
                //ErrorDialog("ConnectTask " );
                mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                DeviceDialog();
            }
        }
    }

    // 연결 끊기 task
    private class CloseTask extends AsyncTask<Void, Void, Object> {
        @Override
        protected Object doInBackground(Void... params) {
            try {
                try{mmOutputStream.close();}catch(Throwable t){/*ignore*/}
                try{mmInputStream.close();}catch(Throwable t){/*ignore*/}
                mmSocket.close();
            } catch (Throwable t) {
                return t;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
                if (result instanceof Throwable) {
                    Log.e("",result.toString(),(Throwable)result);
                    ErrorDialog(result.toString());
                }
        }
    }

    // 목록에서 블루투스 장치를 선택하면 선택한 디바이스를 인자로 하여 doConnect 함수가 호출
    public void DeviceDialog() {
        if (activity.isFinishing()) return;

        FragmentManager fm = MainActivity.this.getSupportFragmentManager();
        MyDialogFragment alertDialog = MyDialogFragment.newInstance(DEVICES_DIALOG, "");
        alertDialog.show(fm, "");
    }

    // 디바이스 커넥트 실패 메시지창
    public void ErrorDialog(String text) {
        if (activity.isFinishing())
            return;

        FragmentManager fm = MainActivity.this.getSupportFragmentManager();
        MyDialogFragment alertDialog = MyDialogFragment.newInstance(ERROR_DIALOG, text);
        alertDialog.show(fm, "");
    }

    // 기기로 부터 데이터 가져오기
    void beginListenForData()
    {
        final Handler handler = new Handler(Looper.getMainLooper());

        stopWorker = false;
        readBufferPosition = 0;
        readBuffer = new byte[1024];
        workerThread = new Thread(new Runnable()
        {
            public void run()
            {
                while(!Thread.currentThread().isInterrupted() && !stopWorker)
                {
                    try
                    {
                        int bytesAvailable = mmInputStream.available();
                        if(bytesAvailable > 0)
                        {
                            byte[] packetBytes = new byte[bytesAvailable];
                            mmInputStream.read(packetBytes);
                            for(int i=0;i<bytesAvailable;i++)
                            {
                                byte b = packetBytes[i];
                                if(b == '\n')
                                {
                                    byte[] encodedBytes = new byte[readBufferPosition];
                                    System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.length);
                                    final String data = new String(encodedBytes, "US-ASCII");

                                    readBufferPosition = 0;

                                    handler.post(new Runnable()
                                    {
                                        public void run()
                                        {
                                            mRecv.setText(data);
                                            if(flag==1)
                                                Savedata(data);
                                        }
                                    });
                                }
                                else
                                {
                                    readBuffer[readBufferPosition++] = b;
                                }
                            }
                        }
                    }
                    catch (IOException ex)
                    {
                        stopWorker = true;
                    }
                }
            }
        });
        workerThread.start();
    }

    //12. UI에 입력된 문자열이 있다면 출력 스트림에 기록하고
    //화면에 "Data Sent"를 출력해줍니다.
    void sendData() throws IOException
    {
        String msg = myTextbox.getText().toString();
        if ( msg.length() == 0 ) return;

        msg += "\n";
        Log.d(msg, msg);
        mmOutputStream.write(msg.getBytes());
        myLabel.setText("Data Sent");
        myTextbox.setText(" ");
    }
    /* 블루투스 관련 메소드 끝 */

    /* 파일 입출력 관련 메소드 시작 */
    public void Savedata(String _data) {
        try {
            //FileOutputStream 객체생성, 파일명 "data.txt", 새로운 텍스트 추가하기 모드
            FileOutputStream fos = openFileOutput("data.txt", Context.MODE_APPEND);
            /*
             * MODE_PRIVATE : 혼자만 사용하는 배타적모드의 파일생성(기본설정)
             * MODE_APPEND : 덮어씌우지 않고 추가모드로 연다.
             * MODE_WORLD_READABLE : 다른 응용프로그램이 파일을 읽을 수 있도록 허용한다.
             * MODE_WORLD_WRITABLE : 다른 응용프로그램이 파일을 기록할 수 있도록 허용한다.
             */
            PrintWriter writer = new PrintWriter(fos);
            writer.println(_data);
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //소프트 키보드 없애기
        //InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        //mm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);*/
    }

    public void mOnClick(View v) {
        switch (v.getId()) {
            case R.id.btn_save:
                flag=1;
                break;
            case R.id.btn_cancel:
                flag=0;
                break;
            case R.id.btn_load:
                StringBuffer buffer= new StringBuffer();
                try {
                    //FileInputStream 객체생성, 파일명 "data.txt"
                    FileInputStream fis=openFileInput("data.txt");
                    BufferedReader reader= new BufferedReader(new InputStreamReader(fis));
                    String str=reader.readLine();//한 줄씩 읽어오기


                    while(str!=null){
                        buffer.append(str+"\n");
                        str=reader.readLine();
                    }
                    text.setText(buffer.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;


            case R.id.btn_delete:
                try{
                    //public boolean deleteFile (String name)
                    if(deleteFile("data.txt")){ //정상적으로 파일이 삭제되면 true
                        text.setText(" ");
                        Toast toast = Toast.makeText(this, "파일 삭제 성공",
                                Toast.LENGTH_SHORT);
                        toast.show();
                    }else{
                        Toast toast = Toast.makeText(this, "삭제할 파일이 없습니다.",
                                Toast.LENGTH_SHORT);
                        toast.show();
                    }

                }catch (Exception e) {
                    //
                }

                break;
        }
    }
    /* 파일 입출력 관련 메소드 끝 */
}

