package bd.com.parvez.httprequestdata.view;

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
import android.widget.Toast;

import bd.com.parvez.httprequestdata.R;
import bd.com.parvez.httprequestdata.model.Item;
import bd.com.parvez.httprequestdata.services.MyIntentServices;
import bd.com.parvez.httprequestdata.utils.NetworkHealper;

public class MainActivity extends AppCompatActivity {
    private TextView textView;
    private boolean networkStatus;
    //public static final String JSON_URL = "http://192.168.0.105/android_apis/apis/public/items_json.php";
    //public static final String JSON_URL = "http://192.168.0.105/android_apis/apis/private/items_json.php";
    //public static final String XML_URL = "http://192.168.0.105/android_apis/apis/public/items_xml.php";
    public static final String XML_URL = "http://192.168.0.105/android_apis/apis/private/items_xml.php";

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
//            String msg = intent.getStringExtra(MyIntentServices.My_SERVICES_PAYLOAD);
//            textView.append(msg+"\n");
            if (intent.hasExtra(MyIntentServices.My_SERVICES_EXCEPTION)) {
                String msg = intent.getStringExtra(MyIntentServices.My_SERVICES_EXCEPTION);
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            }else
            if (intent.hasExtra(MyIntentServices.My_SERVICES_PAYLOAD)) {
                Item[] items = (Item[]) intent.getParcelableArrayExtra(MyIntentServices.My_SERVICES_PAYLOAD);
                for (Item item : items) {
                    textView.append(item.getName() + "\n");
                }

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.textView);
        networkStatus = NetworkHealper.checkConnectivity(this);
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(
                broadcastReceiver, new IntentFilter(MyIntentServices.My_SERVICES_MSG));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(broadcastReceiver);
    }

    public void asyncTask(View view) {
        if (networkStatus) {
            Intent intent = new Intent(this, MyIntentServices.class);
            intent.setData(Uri.parse(XML_URL));
            startService(intent);

        } else {
            Toast.makeText(this, "Network Failed.", Toast.LENGTH_LONG).show();
        }
    }

    public void cancel(View view) {
    }

    public void clear(View view) {
        textView.setText("");
    }

}
