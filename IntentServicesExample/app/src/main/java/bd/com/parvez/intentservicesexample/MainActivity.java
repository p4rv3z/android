package bd.com.parvez.intentservicesexample;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView textView;


    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String msg = intent.getStringExtra(MyIntentServices.My_SERVICES_PAYLOAD);
            textView.append(msg+"\n");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.textView);
        textView.setText("");

        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(
                broadcastReceiver,new IntentFilter(MyIntentServices.My_SERVICES_MSG));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(broadcastReceiver);
    }

    public void asyncTask(View view) {
        Intent intent = new Intent(this,MyIntentServices.class);
        intent.setData(Uri.parse("http://localhost/letsgo/apis/hotel_list.php"));
        startService(intent);
    }

    public void cancel(View view) {
    }

    public void clear(View view) {
    }

}
