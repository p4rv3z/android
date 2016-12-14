package bd.com.parvez.uninstallapp;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText et;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et = (EditText) findViewById(R.id.et);
    }
    public void Delete(View view) {
        String packageName = et.getText().toString();
        if (packageName.isEmpty()){
            Toast.makeText(this,"please enter your package name to delete.",Toast.LENGTH_LONG).show();
        }else {
            Intent intent = new Intent(Intent.ACTION_DELETE);
            intent.setData(Uri.parse("package:"+packageName));
            startActivity(intent);
        }

    }
}
