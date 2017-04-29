package bd.parvez.onlineradio;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.Toast;


public class About extends Activity {
    private Animation animRecord;
    private RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        relativeLayout = (RelativeLayout) findViewById(R.id.rel_about_rel);
        animRecord = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rel);
        relativeLayout.startAnimation(animRecord);
    }

    public void rateUs(View v) {
        String my_package_name = getApplicationContext().getPackageName();
        if (MyServices.checkConnectivity(this)) {

            try {

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("market://details?id=" + my_package_name));
                startActivity(intent);
            } catch (final Exception e) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("http://play.google.com/store/apps/details?id=" + my_package_name));
                startActivity(intent);
            }

        } else {
            AlertDialog.Builder netConnection = new AlertDialog.Builder(this);
            netConnection.setMessage("Internet is Not Connected");
            netConnection.show();
        }
    }

    public void myGmail(View view) {
        String[] myEmail = {"md.parvez28@gmail.com"};
        String sub = getResources().getString(R.string.app_name);//getResources().getString(R.string.app_name);
        if (MyServices.checkConnectivity(this)) {
            try {
                Intent send = new Intent(android.content.Intent.ACTION_SEND);
                send.putExtra(android.content.Intent.EXTRA_EMAIL, myEmail);
                send.putExtra(android.content.Intent.EXTRA_SUBJECT, sub);
                send.putExtra(android.content.Intent.EXTRA_TEXT, "");
                send.setType("plain/text");
                startActivity(send);
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "some thing wrong", Toast.LENGTH_LONG).show();
            }
        } else {
            AlertDialog.Builder netConnection = new AlertDialog.Builder(this);
            netConnection.setMessage("Internet is Not Connected");
            netConnection.show();
        }
    }

    public void myFacebook(View view) {
        if (MyServices.checkConnectivity(this)) {
            try {
                Intent facebook = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://profile/100005224663257"));
                startActivity(facebook);
            } catch (Exception e) {
                Intent facebook = new Intent(Intent.ACTION_VIEW, Uri.parse("https://m.facebook.com/mind.hunter.parvez"));
                facebook.setComponent(new ComponentName("com.android.browser", "com.android.browser.BrowserActivity"));
                facebook.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                this.startActivity(facebook);
            }
        } else {
            AlertDialog.Builder netConnection = new AlertDialog.Builder(this);
            netConnection.setMessage("Internet is Not Connected");
            netConnection.show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        About.this.overridePendingTransition(R.anim.slide_out_from, R.anim.slide_out_to);
    }

    @Override
    protected void onStop() {
        super.onStop();
        relativeLayout.clearAnimation();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void moreApps(View view) {
        if (MyServices.checkConnectivity(this)) {

            try {

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("market://search?q=pub:parvez"));
                startActivity(intent);
            } catch (final Exception e) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("http://play.google.com/store/search?q=pub:parvez"));
                startActivity(intent);
            }

        } else {
            AlertDialog.Builder netConnection = new AlertDialog.Builder(this);
            netConnection.setMessage("Internet is Not Connected");
            netConnection.show();
        }
    }
}
