package bd.parvez.fragment;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Created by PARVEZ on 7/4/2015.
 */
public class FragmentA extends Fragment implements AdapterView.OnItemClickListener {
    private View fragA;
    private ListView listOfTutorials;
    private Communicator communicator;
    private CustomListOfTitle customListOfTitle;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragA = inflater.inflate(R.layout.fragment_a,container,false);
        listOfTutorials = (ListView) fragA.findViewById(R.id.listView);
        customListOfTitle = new CustomListOfTitle(FragmentA.this.getActivity());
        //listAdapter = ArrayAdapter.createFromResource(getActivity(),R.array.namesOfHeros,android.R.layout.simple_list_item_1);
        listOfTutorials.setAdapter(customListOfTitle);
        listOfTutorials.setOnItemClickListener(this);
        return fragA;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ItemObject itemObject = (ItemObject)parent.getAdapter().getItem(position);
        communicator.respond(position,itemObject);
        //String val = (itemObject.getName()+itemObject.getDetails()).toString();
        //Toast.makeText(getActivity(),val,Toast.LENGTH_LONG).show();
    }

    public void setCommunicator(Communicator communicator){
        this.communicator = communicator;
    }
    public interface Communicator{
        public void respond(int index,ItemObject itemObject);
    }
}
