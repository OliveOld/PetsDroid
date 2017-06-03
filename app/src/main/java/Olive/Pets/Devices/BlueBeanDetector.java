package olive.Pets.Devices;

import olive.Pets.BLE.Detector;
import olive.Pets.BLE.Proxy;
import android.util.Log;
import android.view.View;

import com.punchthrough.bean.sdk.Bean;
import com.punchthrough.bean.sdk.BeanDiscoveryListener;

import java.util.ArrayList;
import java.util.List;


/**
 * Light Blue Bean들을 탐색하기 위한 Detector
 * @author luncliff
 */
public class BlueBeanDetector
        implements Detector, BeanDiscoveryListener
{
    ArrayList<Bean> beans;

    public BlueBeanDetector()
    {
        beans = new ArrayList<Bean>(12);
    }

    @Override
    public void begin()
    {
        // trigger scanning...
    }

    @Override
    public void end()
    {
        beans.clear();
    }

    @Override
    public Proxy next()
    {
        if(beans.size() > 0){
            Bean bean =  beans.get(0);
            beans.remove(0);
            return new BlueBeanProxy(bean);
        }
        return null;
    }

    /**
     * 새로운 bean 찾았을 때 이름, 주소 보여줌
     * @param bean
     * @param rssi
     */
    @Override
    public void onBeanDiscovered(Bean bean, int rssi)
    {
        Log.d("BlueBeanDetector", "onBeanDiscovered");
        beans.add(bean);
    }

    /**
     * 탐색 완료되었을 때
     */
    @Override
    public void onDiscoveryComplete()
    {
        Log.d("BlueBeanDetector", "onDiscoveryComplete");
    }
}
