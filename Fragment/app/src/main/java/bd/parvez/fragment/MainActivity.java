package bd.parvez.fragment;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;


public class MainActivity extends Activity implements FragmentA.Communicator {

    private FragmentA fragmentA;
    private FragmentB fragmentB;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentManager = getFragmentManager();
        fragmentA = (FragmentA) fragmentManager.findFragmentById(R.id.fragmentA);
        fragmentA.setCommunicator(this);
    }

    @Override
    public void respond(int index,ItemObject itemObject) {
        fragmentB = (FragmentB) fragmentManager.findFragmentById(R.id.fragmentB);
        if (fragmentB!=null&&fragmentB.isVisible()){
            fragmentB.changeData(index,itemObject);
        }else {
            Intent intent = new Intent(this,Details.class);
            intent.putExtra("index",index);
            intent.putExtra("object",itemObject);
            startActivity(intent);
        }
    }
}
