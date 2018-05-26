package net.tarks.craftingmod.chuno;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

/**
 * Created by superuser on 27/05/2018.
 * Class for SystemUITuner's force reset on shutdown
 * from https://github.com/zacharee/SystemUITunerRedesign
 */

public class ShutdownReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        try{
            context.getPackageManager().getPackageInfo("com.zacharee1.systemuituner", PackageManager.GET_ACTIVITIES);
        }catch (PackageManager.NameNotFoundException e){
            return;
        }
        context.startService(new Intent(context,TunerFallbackService.class));
    }
}
