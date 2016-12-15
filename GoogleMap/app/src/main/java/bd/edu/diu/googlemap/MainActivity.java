package bd.edu.diu.googlemap;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void basicMap(View view) {
        Intent intent = new Intent(this,MapBasic.class);
        startActivity(intent);
    }

    public void mapType(View view) {
        Intent intent = new Intent(this,MapType.class);
        startActivity(intent);
    }

    public void mapWithAnim(View view) {
        Intent intent = new Intent(this,MapAnim.class);
        startActivity(intent);
    }

    public void mapWithIcon(View view) {
        Intent intent = new Intent(this,MapIcon.class);
        startActivity(intent);
    }
    public void mapWithArea(View view) {
        Intent intent = new Intent(this,MapArea.class);
        startActivity(intent);
    }

    public void mapWithStreetView(View view) {
        Intent intent = new Intent(this,MapStreetView.class);
        startActivity(intent);
    }
}
