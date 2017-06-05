package olive.Pets.BLE.Devices;

import olive.Pets.BLE.Detector;
import olive.Pets.BLE.DeviceProxy;
import android.util.Log;

import com.punchthrough.bean.sdk.Bean;
import com.punchthrough.bean.sdk.BeanDiscoveryListener;

import java.util.ArrayList;


/**
 * Light Blue Bean들을 탐색하기 위한 Detector
 * @author luncliff
 */
public class BlueBeanDetector
        implements Detector, BeanDiscoveryListener
{
    int idx;
    ArrayList<Bean> beans;

    public BlueBeanDetector()
    {
        idx = -1;
        beans = new ArrayList<Bean>(12);
    }

    /**
     * Scan 준비/실행
     */
    @Override
    public void begin()
    {
        // trigger scanning...
    }

    /**
     * 현재까지 찾아낸 모든 Bean들을 정리
     */
    @Override
    public void end()
    {
        beans.clear();
    }

    /**
     * Scan 결과를 하나씩 획득
     */
    @Override
    public DeviceProxy next()
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
