package bd.com.parvez.listviewwithheaderandfooter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    ListView lv;
    ImageButton btn;
    ArrayAdapter<String> arrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lv = (ListView) findViewById(R.id.listView);
        arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,FrindList.name);
        //LayoutInflater
        LayoutInflater inflater=this.getLayoutInflater();
        //create a header view
        View header=inflater.inflate(R.layout.listview_header, lv,false);
        //create a button in header
        btn = (ImageButton) header.findViewById(R.id.button);
        btn.setOnClickListener(this);
        lv.addHeaderView(header);
        //create a footer view
        View footer=inflater.inflate(R.layout.listview_footer, lv,false);
        lv.addFooterView(footer);

        lv.setAdapter(arrayAdapter);
        lv.setHeaderDividersEnabled(true);
        lv.setFooterDividersEnabled(true);
        lv.setOnItemClickListener(this);
    }






    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (v==btn){
            Toast.makeText(getApplicationContext(),"Button Click",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d("BD",""+parent.getCount());
        if (position<1) {
            Toast.makeText(getApplicationContext(),"Head", Toast.LENGTH_LONG).show();
        }else if (position > (parent.getCount()-2)){
            Toast.makeText(getApplicationContext(),"Footer", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(getApplicationContext(),position+": "+FrindList.name[position-1], Toast.LENGTH_LONG).show();
        }
    }
}
