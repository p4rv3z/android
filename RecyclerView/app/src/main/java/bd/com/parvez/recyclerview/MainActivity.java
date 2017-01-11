package bd.com.parvez.recyclerview;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import bd.com.parvez.controller.CustomAdapter;
import bd.com.parvez.controller.Data;
import bd.com.parvez.controller.Key;
import bd.com.parvez.model.Item;

public class MainActivity extends AppCompatActivity implements CustomAdapter.ItemClickCallBack{
    private RecyclerView recyclerView;
    private CustomAdapter customAdapter;
    private List<Item> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Get Information From Data class
        data = Data.getAllData();
        recyclerView = (RecyclerView) findViewById(R.id.recycleView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //Create CustomAdapter Object
        customAdapter = new CustomAdapter(data,this);

        recyclerView.setAdapter(customAdapter);
        //Set Custom Adapter Clickable
        customAdapter.setItemClickCallBack(this);
    }

    //On Item Click method
    @Override
    public void onItemClickCallBack(int position) {
        //Get Clicked Item Information
        Item item = data.get(position);
        //Create Intent
        Intent intent = new Intent(this,DetailsActivity.class);
        //Create Bundle
        Bundle bundle = new Bundle();
        //Insert a single Item Information into bundle
        bundle.putString(Key.NAME_KEY,item.getName());
        bundle.putString(Key.EMAIL_KEY,item.getEmail());
        bundle.putInt(Key.IMAGE_KEY,item.getImage());
        //include bundle into intent
        intent.putExtra(Key.ITEM_KEY,bundle);
        //start another activity
        startActivity(intent);
    }

    //on Item Option Click Method
    @Override
    public void onStatusClickCallBack(int position) {
        Item item = data.get(position);
        if (item.isStatus()){
            item.setStatus(false);
        }else{
            item.setStatus(true);
        }
        customAdapter.notifyDataSetChanged();
    }
}
