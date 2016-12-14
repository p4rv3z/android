package bd.parvez.savedatainfiles;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback{
    private Spinner spinner;
    private EditText etValue;
    private TextView tvResult;
    private Button btnSave, btnLoad;
    private ArrayAdapter<String> name;
    private static final String item[] = {"Internal", "External"};
    private int position;
    private static final int REQUEST = 0;
    private static String WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private static String[] PERMISSIONS = {WRITE_EXTERNAL_STORAGE};

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
        spinner = (Spinner) findViewById(R.id.spinner);
        etValue = (EditText) findViewById(R.id.etValue);
        tvResult = (TextView) findViewById(R.id.tvResult);
        btnSave = (Button) findViewById(R.id.btnSave);
        btnLoad = (Button) findViewById(R.id.btnLoad);
        ///////Set Spinner
        setSpinner();
    }

    private void setSpinner() {
        name = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, item);
        spinner.setAdapter(name);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                position = pos;
                etValue.setText("");
                etValue.setHint(item[pos] + " value.");
                tvResult.setText("");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                position = 0;
            }
        });
    }

    public void save(View view) throws IOException {
        String value = etValue.getText().toString();
        String nameOfFile = item[position];
        switch (position) {
            case 0:
                MyFiles.saveFileInternalStorage(nameOfFile, value, this);
                break;
            case 1:
                MyFiles.saveFileExternalStorage(nameOfFile, value, this);
                break;
            default:
                break;
        }

    }

    public void load(View view) throws IOException {
        String nameOfFile = item[position];
        String data;
        switch (position) {
            case 0:
                data = MyFiles.loadFileInternalStorage(nameOfFile, this);
                tvResult.setText(data);
                break;
            case 1:
                data = MyFiles.loadFileExternalStorage(nameOfFile, this);
                tvResult.setText(data);
                break;
            default:
                break;
        }
    }
    @TargetApi(Build.VERSION_CODES.M)
    private boolean isAccepted() {
        return (checkSelfPermission(WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED);
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