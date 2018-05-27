package net.tarks.craftingmod.chuno;

import android.app.Notification;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.text.TextUtils;

import java.util.ArrayList;

import static net.tarks.craftingmod.chuno.MainActivity.HOME_CARRIER;
import static net.tarks.craftingmod.chuno.MainActivity.ICON_BLACKLIST;
import static net.tarks.craftingmod.chuno.MainActivity.ICON_BLACKLIST_BACKUP;
import static net.tarks.craftingmod.chuno.MainActivity.LOCK_CARRIER;

/**
 * Created by superuser on 27/05/2018.
 * Class for SystemUITuner's force reset on shutdown
 * force put string at shutdown
 * from https://github.com/zacharee/SystemUITunerRedesign
 */

public class TunerFallbackService extends Service implements Runnable {
    private Handler handler;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handler = new Handler();
        handler.post(this);
        return Service.START_NOT_STICKY;
    }
    @Override
    public void run() {
        Context context = this.getApplicationContext();
        ContentResolver cr = context.getContentResolver();
        String org = Settings.Global.getString(cr,ICON_BLACKLIST_BACKUP);
        if(org != null){
            // handle....
            String black = Settings.Secure.getString(cr,ICON_BLACKLIST);
            if(black != null && black.length() == 0){
                ArrayList<String> out = new ArrayList<>();
                out.add("rotate");
                out.add("headset");

                if(org.contains(HOME_CARRIER)){
                    out.add(HOME_CARRIER);
                }
                if(org.contains(LOCK_CARRIER)){
                    out.add(LOCK_CARRIER);
                }
                if(org.contains(MainActivity.VOLTE)){
                    out.add(MainActivity.VOLTE);
                }
                Settings.Secure.putString(cr,ICON_BLACKLIST, TextUtils.join(",",out.toArray()));
                this.stopSelf();
            }else{
                handler.postDelayed(this,300);
            }
        }else{
            this.stopSelf();
        }
    }
}
