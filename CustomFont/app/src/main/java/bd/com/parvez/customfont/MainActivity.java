package bd.com.parvez.customfont;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private static final String FONT_PATH_ADORSHOLIPI = "fonts/adorsholipi.ttf";
    private static final String FONT_PATH_KALPURUSH = "fonts/kalpurush.ttf";
    private static final String FONT_PATH_SIYAMRUPALI = "fonts/siyamrupali.ttf";
    private static final String text = "\"ঈশ্বর থাকেন ঐ গ্রামে ভদ্র পল্লীতে, এখানে তাহাকে খুঁজিয়া পাওয়া যাবে না।\"\n" +
            "-মানিক বন্দ্যোপাধ্যায়";
    private TextView adorsholipi, kalpurush, siyamrupali;
    private Typeface tf_adorsholipi, tf_kalpurush, tf_siyamrupali;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        adorsholipi = (TextView) findViewById(R.id.tv_adorsholipi);
        kalpurush = (TextView) findViewById(R.id.tv_kalpurush);
        siyamrupali = (TextView) findViewById(R.id.tv_siyamrupali);
        //adorsholipi
        tf_adorsholipi = Typeface.createFromAsset(getAssets(),FONT_PATH_ADORSHOLIPI);
        adorsholipi.setText(text);
        adorsholipi.setTypeface(tf_adorsholipi);
        //kalpurush
        tf_kalpurush = Typeface.createFromAsset(getAssets(),FONT_PATH_KALPURUSH);
        kalpurush.setText(text);
        kalpurush.setTypeface(tf_kalpurush);
        //siyamrupali
        tf_siyamrupali = Typeface.createFromAsset(getAssets(),FONT_PATH_SIYAMRUPALI);
        siyamrupali.setText(text);
        siyamrupali.setTypeface(tf_siyamrupali);

    }

}
