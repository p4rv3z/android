package bd.parvez.sqlite;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import bd.parvez.database.MyDatabaseHelper;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private MyDatabaseHelper myDatabaseHelper;
    private ListView listView;
    private ArrayAdapter<Student> arrayAdapter;
    private List<Student> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myDatabaseHelper = new MyDatabaseHelper(this);
        listView = (ListView) findViewById(R.id.listView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        myDatabaseHelper.openDB();
        createAllListView();
    }

    private void createAllListView() {
        list = myDatabaseHelper.loadAllData();
        createListView(list);
    }

    private void createListView(List<Student> list) {
        if (list.size()<1){
            Toast.makeText(this,"No data found.\nAdd some data.",Toast.LENGTH_LONG);
            addData(null);
            MainActivity.this.finish();
        }else {
            arrayAdapter = new ArrayAdapter<Student>(this,android.R.layout.simple_list_item_1,list);
            listView.setAdapter(arrayAdapter);
            listView.setOnItemClickListener(this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        myDatabaseHelper.closeDB();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if (id == R.id.set_add) {
            addData(null);
        }else if (id == R.id.set_asc){
            list = myDatabaseHelper.loadAllASC();
            createListView(list);
        }else if (id == R.id.set_desc){
            list = myDatabaseHelper.loadAllDESC();
            createListView(list);
        }else if (id == R.id.set_fav){
            list = myDatabaseHelper.loadAllFav();
            createListView(list);
        }

        return super.onOptionsItemSelected(item);
    }

    private void addData(Student student) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("student",student);
        Intent reg = new Intent(this,RegForm.class);
        reg.putExtras(bundle);
        startActivity(reg);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        addData(list.get(position));
    }
}
