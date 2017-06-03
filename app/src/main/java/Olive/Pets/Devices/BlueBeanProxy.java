package Olive.Pets.Devices;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
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

import java.text.SimpleDateFormat;
import java.text.StringCharacterIterator;
import java.util.Calendar;

import Olive.Pets.Activity.Bluetooth;
import Olive.Pets.BLE.Packet;
import Olive.Pets.BLE.Proxy;
import Olive.Pets.DB.PostureData;
import Olive.Pets.R;
import io.realm.Realm;
import io.realm.RealmResults;

import static android.os.SystemClock.sleep;
import static android.view.View.VISIBLE;


/**
 * Light Blue Bean으로부터 바이트를 전달받아 패킷을 생성
 * @author luncliff
 */
public class BlueBeanProxy
        implements Proxy, BeanListener
{
    static final String TAG = BlueBeanProxy.class.getName();

    Bean bean;
    int gage;
    byte[] buffer;

    public BlueBeanProxy(Bean _bean)
    {
        bean = _bean;
        buffer = new byte[240];
        gage = 0;
    }

    void expand(int size)
    {
        if(buffer.length > size){
            return;
        }
        byte[] old = buffer;
        buffer = new byte[size];

        // Copy old data
        for(int i = 0; i < old.length; i++){
            buffer[i] = old[i];
        }
        // zero memory
        for(int i = old.length; i < size; i++){
            buffer[i] = 0;
        }
    }

    boolean readable(){
        if(bean != null && bean.isConnected()){
            return true;
        }
        return false;
    }

    boolean writable(){
        if(bean != null && bean.isConnected()){
            return true;
        }
        return false;
    }

    public Packet recv()
    {
        // check if packet is available

        // Generate packet

        // After Packet generation, shift bytes and gage to front

        return null;
    }

    public boolean send(Packet pkt)
    {
        if(readable() == false){
            return false;
        }
        byte[] data = pkt.toBytes();
        bean.sendSerialMessage(data);
        return true;
    }

    @Override
    public boolean isConnected()
    {
        if(bean != null && bean.isConnected())
            return true;
        else
            return false;
    }

    @Override
    public void close()
    {
        if(bean != null) bean.disconnect();
    }


    @Override
    public void onConnected()
    {
        Log.d(TAG, "onConnected");
    }

    @Override
    public void onConnectionFailed()
    {
        Log.d(TAG,"onConnectionFailed");
        if(bean != null && bean.isConnected()){
            bean.disconnect();
        }
        bean = null;
        gage = 0;
    }

    @Override
    public void onDisconnected()
    {
        Log.d(TAG,"onDisconnected");
        gage = 0;
    }

    @Override
    public void onSerialMessageReceived(byte[] data)
    {
        Log.d(TAG, "onSerialMessageReceived");

        // Check available space
        int available = buffer.length - gage;
        while(available < data.length)
        {
            expand(buffer.length * 2);
        }

        // Copy data
        available = buffer.length - gage;
        for(int i = 0; i<data.length; i++){
            buffer[gage] = data[i];
            gage += 1;
        }
    }

    @Override
    public void onScratchValueChanged(ScratchBank bank, byte[] value)
    {
        Log.d(TAG,"onScratchValueChanged");
        Log.d(TAG, bank.toString());
        //Log.d(TAG, value);
    }

    @Override
    public void onError(BeanError error)
    {
        Log.e(TAG,"onError");
        Log.e(TAG, error.toString());
    }

}
