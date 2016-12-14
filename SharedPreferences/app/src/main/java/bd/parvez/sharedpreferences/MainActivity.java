package bd.parvez.sharedpreferences;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private Spinner spinner;
    private EditText etValue;
    private TextView tvResult;
    private Button btnSave, btnLoad;
    private ArrayAdapter<String> name;
    private static final String item[] = {"Integer","String","Boolean","Float","Long"};
    private int position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
               position=pos;
                etValue.setText("");
                etValue.setHint(item[pos]+" type of value.");
                tvResult.setText("");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                position=0;
            }
        });
    }

    public void save(View view) {
        String value = etValue.getText().toString();
        String nameOfFile = item[position];
        switch (position){
            case 0:
                int i = Integer.parseInt(value);
                MySharedPreferences.saveInt(nameOfFile,i,this);
                break;
            case 1:
                MySharedPreferences.saveString(nameOfFile,value,this);
                break;
            case 2:
                boolean b = Boolean.parseBoolean(value);
                MySharedPreferences.saveBoolean(nameOfFile,b,this);
                break;
            case 3:
                float f = Float.parseFloat(value);
                MySharedPreferences.saveFloat(nameOfFile,f,this);
                break;
            case 4:
                long l = Long.parseLong(value);
                MySharedPreferences.saveLong(nameOfFile,l,this);
                break;
            default:
                break;
        }

    }

    public void load(View view) {
        String nameOfFile = item[position];
        switch (position){
            case 0:
                int i = MySharedPreferences.loadInt(nameOfFile,this);
                tvResult.setText(String.valueOf(i));
                break;
            case 1:
                String s = MySharedPreferences.loadString(nameOfFile,this);
                tvResult.setText(s);
                break;
            case 2:
                boolean b = MySharedPreferences.loadBoolean(nameOfFile,this);
                tvResult.setText(String.valueOf(b));
                break;
            case 3:
                float f = MySharedPreferences.loadFloat(nameOfFile,this);
                tvResult.setText(String.valueOf(f));
                break;
            case 4:
                long l = MySharedPreferences.loadLong(nameOfFile,this);
                tvResult.setText(String.valueOf(l));
                break;
            default:
                break;
        }
    }
}
