package bd.parvez.fragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


/**
 * Created by PARVEZ on 7/4/2015.
 */
public class CustomListOfTitle extends BaseAdapter{
    private Context context;
    private String names[];
    private String details[];
    public CustomListOfTitle(Context context){
        this.context = context;
        names = context.getResources().getStringArray(R.array.namesOfHeros);
        details = context.getResources().getStringArray(R.array.detailsOfHeros);
    }
    @Override
    public int getCount() {
        return names.length;
    }

    @Override
    public Object getItem(int position) {
        ItemObject itemObject = new ItemObject(names[position],details[position]);
        return itemObject;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = null;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.custom_list, parent, false);
        } else {
            row = convertView;
        }

        TextView tvName = (TextView) row.findViewById(R.id.tvCustom_title);
        TextView tvDetail = (TextView) row.findViewById(R.id.tvCustom_details);

        tvName.setText(names[position]);
        tvDetail.setText(details[position].substring(0,80));

        return row;
    }
}
