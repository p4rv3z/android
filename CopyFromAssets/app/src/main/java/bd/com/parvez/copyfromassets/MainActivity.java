package bd.com.parvez.copyfromassets;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import bd.com.parvez.copy.Copy;
import bd.com.parvez.services.PermissionUtil;

public class MainActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback{
    TextView tvFrom, tvTo;
    private static final int REQUEST = 0;
    private static String WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private static String READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE;
    private static String[] PERMISSIONS = {WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Check Android Version
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (isAccepted()) {
                requestPermissions(PERMISSIONS, REQUEST);
            }
        }

        tvFrom = (TextView) findViewById(R.id.from);
        tvTo = (TextView) findViewById(R.id.to);
        String to = Environment.getExternalStorageDirectory()+"/css";
        tvFrom.setText("Assets");
        Copy.copyFromAssets(this,"css",to.toString());
        tvTo.setText(to.toString());
    }
    @TargetApi(Build.VERSION_CODES.M)
    private boolean isAccepted() {
        return checkSelfPermission(WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || checkSelfPermission(READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST) {
            if (PermissionUtil.verifyPermissions(grantResults)) {
                Toast.makeText(this, "!!!Permission Granted!!!", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "!!!Permission Denied!!!", Toast.LENGTH_LONG).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

}
