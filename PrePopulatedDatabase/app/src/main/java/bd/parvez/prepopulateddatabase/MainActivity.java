package bd.parvez.prepopulateddatabase;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import bd.parvez.database.MyDatabaseHelper;
import bd.parvez.myservices.MyTag;

public class MainActivity extends AppCompatActivity {

    private MyDatabaseHelper myDatabaseHelper;
    private static final String className = "MainActivity";
    private ListView listView;
    private ArrayAdapter<Friend> arrayAdapter;
    private List<Friend> friendList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MyTag.myTag(className, "onCreated() method called.");
        listView = (ListView) findViewById(R.id.listView);
        myDatabaseHelper = new MyDatabaseHelper(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        MyTag.myTag(className, "onCreateOptionsMenu() method called.");
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        MyTag.myTag(className, "onResume() method called.");
        myDatabaseHelper.openDB();
        creatingList();
    }

    private void creatingList() {
        friendList = myDatabaseHelper.loadAllData();
        if (friendList.size() < 1) {
            Toast.makeText(this, "No data found.\nAdd some data.", Toast.LENGTH_LONG);
        } else {
            arrayAdapter = new ArrayAdapter<Friend>(this, android.R.layout.simple_list_item_1, friendList);
            listView.setAdapter(arrayAdapter);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        MyTag.myTag(className, "onPause() method called.");
        myDatabaseHelper.closeDB();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        MyTag.myTag(className, "onOptionsItemSelected() method called.");
        int id = item.getItemId();

        if (id == R.id.set_add) {
            //todo new add activity
        }

        return super.onOptionsItemSelected(item);
    }
}
