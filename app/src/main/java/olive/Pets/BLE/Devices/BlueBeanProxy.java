package olive.Pets.BLE.Devices;

import olive.Pets.BLE.Conv;
import olive.Pets.BLE.Packet;
import olive.Pets.BLE.DeviceProxy;

import android.util.Log;

import com.punchthrough.bean.sdk.Bean;
import com.punchthrough.bean.sdk.BeanListener;
import com.punchthrough.bean.sdk.message.BeanError;
import com.punchthrough.bean.sdk.message.ScratchBank;


/**
 * Light Blue Bean으로부터 바이트를 전달받아 패킷을 생성
 * @author luncliff
 */
public class BlueBeanProxy
        implements DeviceProxy, BeanListener
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
        return bean != null && bean.isConnected();
    }

    boolean writable(){
        return bean != null && bean.isConnected();
    }

    // @// TODO: 6/5/2017 convert bytes in buffer to packet
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
        byte[] data = Conv.toBytes(pkt);
        bean.sendSerialMessage(data);
        return true;
    }

    @Override
    public boolean isConnected()
    {
        return bean != null && bean.isConnected();
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
