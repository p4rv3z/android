package bd.parvez.fragment;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;


public class Details extends Activity {
    private FragmentB fragmentB;
    private FragmentManager fragmentManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        int index = getIntent().getIntExtra("index",0);
        ItemObject itemObject= (ItemObject) getIntent().getSerializableExtra("object");
        fragmentManager = getFragmentManager();
        fragmentB = (FragmentB) fragmentManager.findFragmentById(R.id.fragmentB);
        if (fragmentB!=null)
        fragmentB.changeData(index,itemObject);
    }
}
