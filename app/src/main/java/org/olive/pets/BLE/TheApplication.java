package org.olive.pets.BLE;

/**
 * Created by KMJ on 2017-05-15.
 */

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import org.olive.pets.BLE.events.DisconnectAllRequestEvent;

import de.greenrobot.event.EventBus;

/**
 * Here goes the app's initializations.
 * <p/>
 * Created by pascal on 7/22/15.
 */
public class TheApplication extends Application {
    public void onCreate() {
        super.onCreate();
        BluetoothHandler.INSTANCE.init(this);
        DeviceDatabase.INSTANCE.init(this); // init devices database
        startService(new Intent(this, TheService.class));
        registerScreenOffReceiver();
    }

    private void registerScreenOffReceiver() {
        registerReceiver(
                new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        EventBus.getDefault().post(new DisconnectAllRequestEvent());
                    }
                },
                new IntentFilter(Intent.ACTION_SCREEN_OFF));
    }
}