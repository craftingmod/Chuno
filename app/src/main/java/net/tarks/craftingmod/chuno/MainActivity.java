package net.tarks.craftingmod.chuno;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.Spanned;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final String url = "https://github.com/craftingmod/Chuno/blob/master/README.md";
        final boolean granted_write = this.checkCallingOrSelfPermission("android.permission.WRITE_SECURE_SETTINGS") == PackageManager.PERMISSION_GRANTED;
        if(!granted_write) {
            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.it_chuno_nopermTitle))
                    .setMessage(getTextWithURL(getString(R.string.it_chuno_nopermContent),url))
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                            finish();
                        }
                    })
                    .show();
        }
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
    private void dialog(String title,String content,DialogInterface.OnClickListener clicked){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setPositiveButton(android.R.string.ok, clicked)
                .setMessage(content)
                .show();
    }
}
