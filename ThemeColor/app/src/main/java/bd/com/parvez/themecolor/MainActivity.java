package bd.com.parvez.themecolor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private static SharedPreferences sp;
    private static SharedPreferences.Editor spEditor;
    private int theme[] = {R.style.AppTheme, R.style.AppTheme_Sky, R.style.AppTheme_Yellow};
    private int nBartheme[] = {R.color.colorPrimary, R.color.colorPrimarySky, R.color.colorPrimaryYellow};
    private int sBartheme[] = {R.color.colorPrimaryDark, R.color.colorPrimaryDarkSky, R.color.colorPrimaryDarkYellow};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        themeSet(); // Theme set
        setContentView(R.layout.activity_main);
    }

    private void themeSet() {
        setTheme(theme[getValue()]); // Theme set for below api 21
        // Theme set for upper api 20
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setNavigationBarColor(getColor(nBartheme[getValue()]));
            getWindow().setStatusBarColor(getColor(sBartheme[getValue()]));
        }
    }

    //  For Default Theme
    public void defaultBtn(View view) {
        setValue(0);
        restart();
    }

    //  For Sky Theme
    public void skyBtn(View view) {
        setValue(1);
        restart();
    }

    //  For Yellow Theme
    public void yellowBtn(View view) {
        setValue(2);
        restart();
    }

    //  Get theme from SharedPreferences
    public int getValue() {
        sp = getSharedPreferences("theme", MODE_PRIVATE);
        return sp.getInt("theme", 0);
    }

    //  Set theme to SharedPreferences
    public void setValue(int values) {
        sp = getSharedPreferences("theme", MODE_PRIVATE);
        spEditor = sp.edit();
        spEditor.putInt("theme", values);
        spEditor.commit();
    }

    //  Restart the application
    private void restart() {
        Intent intent = getBaseContext().getPackageManager()
                .getLaunchIntentForPackage(getBaseContext().getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
