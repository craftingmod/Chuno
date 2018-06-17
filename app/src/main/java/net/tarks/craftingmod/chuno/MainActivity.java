package net.tarks.craftingmod.chuno;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;


public class MainActivity extends Activity {
    private static final String version = "1.4";
    public static final String ICON_BLACKLIST = "icon_blacklist";
    public static final String ICON_BLACKLIST_BACKUP = "icon_blacklist_backup";
    public static final String HOME_CARRIER = "slimindicator_home_carrier";
    public static final String LOCK_CARRIER = "slimindicator_lock_carrier";
    public static final String VOLTE = "ims_volte";
    private Switch cs;
    private Switch volte;
    private Switch hide;
    private TextView guide;
    private ContentResolver cr;
    private Context context;
    private PackageManager pm;
    private ComponentName cname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chuno);
        final String url = "https://github.com/craftingmod/Chuno/blob/v" + version + "/README.md";
        cr = this.getContentResolver();
        context = this;
        /**
         * Good lock check
         */
        boolean goodlock = true;
        try {
            context.getPackageManager().getPackageInfo("com.samsung.android.qstuner", PackageManager.GET_ACTIVITIES);
        }catch (PackageManager.NameNotFoundException e) {
            goodlock = false;
        }
        /**
         * Check secure settings perm
         */
        final boolean granted_write = this.checkCallingOrSelfPermission("android.permission.WRITE_SECURE_SETTINGS") == PackageManager.PERMISSION_GRANTED;
        if(!granted_write && !goodlock) {
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
        guide = findViewById(R.id.guide);
        guide.setText(getTextWithURL(getString(R.string.it_chuno_pc),url));
        guide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            }
        });

        /**
         * Good Lock Button
         */
        if(goodlock) {
            cs.setVisibility(View.GONE);
            volte.setVisibility(View.GONE);
            guide.setVisibility(View.GONE);
            Button goodlock_link = findViewById(R.id.btn_goodlook);
            goodlock_link.setVisibility(View.VISIBLE);
            goodlock_link.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent();
                    i.setComponent(new ComponentName("com.samsung.android.qstuner", "com.samsung.android.qstuner.slimindicator.SlimIndicatorActivity"));
                    startActivity(i);
                    // finish();
                }
            });
        }

        /**
         * Hide app
         */
        pm = getPackageManager();
        cname = new ComponentName(this, Hidden.class);
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
        String blacks = Settings.Secure.getString(cr,ICON_BLACKLIST);
        ArrayList<String> out = new ArrayList<>();
        if(blacks != null) {
            blacks = blacks.trim();
            out.addAll(Arrays.asList(blacks.split(",")));
        }else{
            out.add("headset");
            out.add("rotate");
        }
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
            final String join = TextUtils.join(",", blacks.toArray());
            Settings.Secure.putString(cr,ICON_BLACKLIST, join);
            if(Settings.Global.getString(cr,ICON_BLACKLIST_BACKUP) != null) {
                Settings.Global.putString(cr,ICON_BLACKLIST_BACKUP, join);
            }
        }
    }
}
