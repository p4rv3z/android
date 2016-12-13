package bd.com.parvez.batteryinfo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = (TextView) findViewById(R.id.textView);
        this.registerReceiver(this.broadcastReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
    }


    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int health = intent.getIntExtra(BatteryManager.EXTRA_HEALTH, 0);
            int icon_small = intent.getIntExtra(BatteryManager.EXTRA_ICON_SMALL, 0);
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
            int plug = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, 0);
            boolean present = intent.getExtras().getBoolean(BatteryManager.EXTRA_PRESENT);
            int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, 0);
            int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, 0);
            String technology = intent.getStringExtra(BatteryManager.EXTRA_TECHNOLOGY);
            int temperature = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0);
            int voltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0);
            int pluggedAC = intent.getIntExtra("plugged", BatteryManager.BATTERY_PLUGGED_AC);
            int pluggedUSB = intent.getIntExtra("plugged", BatteryManager.BATTERY_PLUGGED_USB);

            tv.setText("Health : " + health +
                    "\nIcon-Small : " + icon_small +
                    "\nLevel : " + level +
                    "\nPlugged : " + plug +
                    "\nPresent : " + present +
                    "\nScale : " + scale +
                    "\nStatus : " + status +
                    "\nTechnology : " + technology +
                    "\nTemperature : " + temperature +
                    "\nVoltage : " + voltage +
                    "\nPlugged AC : " + pluggedAC +
                    "\nPlugged USB : " + pluggedUSB

            );

        }
    };
}
