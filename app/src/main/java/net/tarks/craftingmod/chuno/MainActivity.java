package net.tarks.craftingmod.chuno;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.om.IOverlayManager;
import android.content.om.OverlayInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManagerInternal;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.provider.Settings;
import android.text.Html;
import android.text.Spannable;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;


public class MainActivity extends Activity {
    private static final String ICON_BLACKLIST = "icon_blacklist";
    private static final String HOME_CARRIER = "slimindicator_home_carrier";
    private static final String LOCK_CARRIER = "slimindicator_lock_carrier";
    private Switch cs;
    private ContentResolver cr;
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final String url = "https://github.com/craftingmod/Chuno/blob/master/README.md";
        cr = this.getContentResolver();
        context = this;
        /**
         * Check secure settings perm
         */
        final boolean granted_write = this.checkCallingOrSelfPermission("android.permission.WRITE_SECURE_SETTINGS") == PackageManager.PERMISSION_GRANTED;
        if(!granted_write) {
            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.it_chuno_nopermTitle))
                    .setMessage(getString(R.string.it_chuno_nopermContent))
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                            // finish();
                        }
                    })
                    .show();
        }
        /**
         * Sync cs button
         */
        cs = findViewById(R.id.switch_cs);
        ArrayList<String> blacks = getIconBlacks();
        cs.setEnabled(granted_write);
        cs.setChecked(!(blacks.contains(HOME_CARRIER) && blacks.contains(LOCK_CARRIER)));
        cs.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                ArrayList<String> blacks = getIconBlacks();
                if(b){
                    blacks.remove(HOME_CARRIER);
                    blacks.remove(LOCK_CARRIER);
                } else {
                    if(!blacks.contains(HOME_CARRIER)){
                        blacks.add(HOME_CARRIER);
                    }
                    if(!blacks.contains(LOCK_CARRIER)){
                        blacks.add(LOCK_CARRIER);
                    }
                }
                Settings.Secure.putString(cr,ICON_BLACKLIST, TextUtils.join(",", blacks.toArray()));
                Toast.makeText(context,R.string.it_chuno_reboot,Toast.LENGTH_SHORT).show();
            }
        });
        /**
         * disable volte button.
         */
        boolean omsAval = false;
        boolean applied = false;
        try {
            // Failed in S9 Oreo.. Why?
            IOverlayManager oms = IOverlayManager.Stub.asInterface(ServiceManager.getService("overlay"));
            if (oms != null) {
                omsAval = true;
                OverlayInfo oi = oms.getOverlayInfo(this.getPackageName(),0);
                applied = oi.isEnabled();
            }
        } catch (Exception e) {
            // ClassNotFoundException
            e.printStackTrace();
        }
        final Switch volte = findViewById(R.id.switch_volte);
        if (!omsAval) {
            volte.setText(volte.getText() + " (OMS not available)");
        }
        volte.setEnabled(omsAval);
        volte.setClickable(false);
        volte.setChecked(applied);
        /**
         * guide
         */
        final TextView guide = findViewById(R.id.guide);
        guide.setText(getTextWithURL(getString(R.string.it_chuno_pc),url));
        guide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            }
        });
    }
    @SuppressWarnings("deprecation")
    private Spanned getTextWithURL(String text, String url){
        text = text.replace("$url","<a href=\'$url\'>$url</a>").replaceAll("\\$url",url);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(text,Html.FROM_HTML_MODE_LEGACY);
        } else {
            return Html.fromHtml(text);
        }
    }
    private ArrayList<String> getIconBlacks(){
        String blacks = Settings.Secure.getString(cr,ICON_BLACKLIST).trim();
        ArrayList<String> out = new ArrayList<>();
        out.addAll(Arrays.asList(blacks.split(",")));
        return out;
    }
    private void dialog(String title,String content,DialogInterface.OnClickListener clicked){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setPositiveButton(android.R.string.ok, clicked)
                .setMessage(content)
                .show();
    }
}
