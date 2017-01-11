package bd.com.parvez.recyclerview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import bd.com.parvez.controller.Key;

public class DetailsActivity extends AppCompatActivity {
    private TextView name,email;
    private ImageView image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        //Initialize TextView and ImageView ID
        name = (TextView) findViewById(R.id.details_name);
        email = (TextView) findViewById(R.id.details_email);
        image = (ImageView) findViewById(R.id.details_image_view);
        //Get bundle from MainActivity
        Bundle bundle = getIntent().getBundleExtra(Key.ITEM_KEY);
        //Set data into TextView and ImageView
        name.setText(bundle.getString(Key.NAME_KEY));
        email.setText(bundle.getString(Key.EMAIL_KEY));
        image.setImageResource(bundle.getInt(Key.IMAGE_KEY));
    }
}
