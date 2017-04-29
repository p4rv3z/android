package bd.parvez.onlineradio;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

/**
 * Created by Parvez on 6/7/2015.
 */
public class CustomPathList extends BaseAdapter {
    private Context context;
    private ArrayList<String> name;
    private Intent radioPlayerServices;

    public CustomPathList(Context context, ArrayList<String> name) {
        this.context = context;
        this.name = name;

        Log.e("path", MyServices.getPath(context) + "/OnlineRadioBangladesh/");
        try {
            radioPlayerServices = new Intent(context, MyRadioPlayer.class);
        } catch (Exception e) {
            Log.e("BD", e.getMessage());
        }
    }

    @Override
    public int getCount() {
        return name.size();
    }

    @Override
    public Object getItem(int position) {
        return name.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = null;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.custom_layout, parent, false);
        } else {
            row = convertView;
        }
        TextView tvName = (TextView) row.findViewById(R.id.tv_custom_ll_name);
        ImageView imFav = (ImageView) row.findViewById(R.id.img_custom_ll_fav);
        imFav.setVisibility(View.GONE);
        final ImageView imPlay = (ImageView) row.findViewById(R.id.img_custom_ll_play);

        imPlay.setImageResource(R.mipmap.play);
        tvName.setText(name.get(position));


        imPlay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                notifyDataSetChanged();
                if (MyServices.isMyServiceRunning(MyRadioPlayer.class, context)) {
                    play(position);
                    imPlay.setImageResource(R.mipmap.pause);
                } else {
                    play(position);
                    imPlay.setImageResource(R.mipmap.pause);
                }
            }
        });

        return row;
    }

    private void play(int position) {
        String radioName = name.get(position).toString();
        String url = (MyServices.getPath(context) + "/OnlineRadioBangladesh/" + name.get(position).trim()).toString();

        try {
            if (MyServices.isMyServiceRunning(MyRadioPlayer.class, context)) {
                context.stopService(radioPlayerServices);
            }
        } catch (Exception e) {
            Log.v("BD", e.getMessage());
        }
        radioPlayerServices.putExtra("url", url);
        radioPlayerServices.putExtra("radioName", radioName);
        try {
            context.startService(radioPlayerServices);
        } catch (Exception e) {
            Log.v("BD", e.getMessage());
        }
    }

}
