package bd.parvez.fragment;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by PARVEZ on 7/4/2015.
 */
public class FragmentB extends Fragment {
    private View fragB;
    private TextView tvTitles, tvDetails;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragB = inflater.inflate(R.layout.fragment_b, container, false);
        tvTitles = (TextView) fragB.findViewById(R.id.textViewName);
        tvDetails = (TextView) fragB.findViewById(R.id.textViewDetails);
        //get current position and pass it in changeData()
        //changeData(0);
        return fragB;
    }
    public void changeData(int position,ItemObject itemObject){
        tvTitles.setText(itemObject.getName());
        tvDetails.setText(itemObject.getDetails());
    }
}
