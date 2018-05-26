package net.tarks.craftingmod.chuno;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
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
    private static final String VOLTE = "ims_volte";
    private Switch cs;
    private Switch volte;
    private Switch hide;
    private ContentResolver cr;
    private Context context;
    private PackageManager pm;
    private ComponentName cname;

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
        ArrayList<String> blacks = getIconBlacks();
        cs = findViewById(R.id.switch_cs);
        cs.setEnabled(granted_write);
        cs.setChecked(!(blacks.contains(HOME_CARRIER) && blacks.contains(LOCK_CARRIER)));
        cs.setOnCheckedChangeListener(new onButtonChanged(HOME_CARRIER,LOCK_CARRIER) {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                super.onCheckedChanged(compoundButton, b);
                Toast.makeText(context,R.string.it_chuno_reboot,Toast.LENGTH_SHORT).show();
            }
        });
        /**
         * Sync volte button
         */
        volte = findViewById(R.id.switch_volte);
        volte.setEnabled(granted_write);
        volte.setChecked(!blacks.contains(VOLTE));
        volte.setOnCheckedChangeListener(new onButtonChanged(VOLTE));
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
        /**
         * Hide app
         */
        pm = getPackageManager();
        cname = new ComponentName(this, MainActivity.class);
        final boolean hided = pm.getComponentEnabledSetting(cname) >= PackageManager.COMPONENT_ENABLED_STATE_DISABLED;
        hide = findViewById(R.id.switch_hide);
        hide.setChecked(hided);
        hide.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                pm.setComponentEnabledSetting(cname,b?PackageManager.COMPONENT_ENABLED_STATE_DISABLED:PackageManager.COMPONENT_ENABLED_STATE_ENABLED,PackageManager.DONT_KILL_APP);
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
    private class onButtonChanged implements CompoundButton.OnCheckedChangeListener {

        protected String[] _settings;

        public onButtonChanged(String... settings){
            this._settings = settings;
        }

        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            ArrayList<String> blacks = getIconBlacks();
            for(String set : this._settings) {
                if(b) {
                    blacks.remove(set);
                } else {
                    if(!blacks.contains(set)){
                        blacks.add(set);
                    }
                }
            }
            Settings.Secure.putString(cr,ICON_BLACKLIST, TextUtils.join(",", blacks.toArray()));
        }
    }
}
