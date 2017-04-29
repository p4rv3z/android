package bd.parvez.onlineradio;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Parvez on 6/5/2015.
 */
public class CustomList extends BaseAdapter {
    private Context context;
    private static final String TAG = "Online Radio Bangladesh";
    private ArrayList<RadioClass> alldata;
    private ArrayList<RadioClass> search;
    private boolean x;
    private SharedPreferences sp;
    private SharedPreferences.Editor spEditor;
    private RadioDB radioDB;
    private Intent radioPlayerServices;


    public CustomList(Context context, ArrayList<RadioClass> alldata, boolean x) {
        this.context = context;
        this.alldata = alldata;
        search = new ArrayList<RadioClass>();
        search.addAll(alldata);
        this.x = x;
        sp = context.getSharedPreferences("position", context.MODE_PRIVATE);
        radioDB = RadioDB.getInstance(context);
        try {
            radioPlayerServices = new Intent(context, MyRadioPlayer.class);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    public ArrayList<RadioClass> getCuctomItem() {
        return alldata;
    }

    @Override
    public int getCount() {
        return alldata.size();
    }

    @Override
    public Object getItem(int position) {
        return alldata.get(position);
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
        final ImageView imFav = (ImageView) row.findViewById(R.id.img_custom_ll_fav);
        final ImageView imPlay = (ImageView) row.findViewById(R.id.img_custom_ll_play);
        imPlay.setImageResource(R.mipmap.play);
        tvName.setText(alldata.get(position).getName().toString());
        if (alldata.get(position).getFav() == 1) {
            imFav.setImageResource(R.mipmap.fav_ena);
        } else {
            imFav.setImageResource(R.mipmap.fav_dis);
        }

        if (MyServices.isMyServiceRunning(MyRadioPlayer.class, context)) {
            if (radioDB.getAllData().get(getPosition()).getId() == alldata.get(position).getId()) {
                imPlay.setImageResource(R.mipmap.pause);
            } else {
                imPlay.setImageResource(R.mipmap.play);
            }
        }

        imPlay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (MyServices.isMyServiceRunning(MyRadioPlayer.class, context)) {
                    if (getPosition() == position) {
                        stop();
                    } else {
                        imPlay.setImageResource(R.mipmap.pause);
                        play(position);
                        setPosition(getAbsPosi(position));
                    }

                } else {
                    imPlay.setImageResource(R.mipmap.pause);
                    play(position);
                    setPosition(getAbsPosi(position));
                }
            }
        });
        imFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int id = alldata.get(position).getId();

                if (alldata.get(position).getFav() == 0) {
                    radioDB.updateFav(id, 1);
                    alldata.get(position).setFav(1);
                    imFav.setImageResource(R.mipmap.fav_ena);
                } else {
                    radioDB.updateFav(id, 0);
                    alldata.get(position).setFav(0);
                    imFav.setImageResource(R.mipmap.fav_dis);
                    if (!x) {
                        alldata.remove(position);
                    }
                }
                notifyDataSetChanged();
            }
        });
        return row;
    }

    private int getAbsPosi(int position) {
        int z = 0;
        for (int i = 0; i < radioDB.getAllData().size(); i++) {
            if (alldata.get(position).getId() == radioDB.getAllData().get(i).getId()) {
                z = i;
                break;
            }
        }
        return z;
    }

    private void play(int position) {
        String name = alldata.get(position).getName().toString();
        String url = alldata.get(position).getUrl().trim().toString();
        if (MyServices.checkConnectivity(context)) {
            try {
                if (MyServices.isMyServiceRunning(MyRadioPlayer.class, context)) {
                    context.stopService(radioPlayerServices);
                }
            } catch (Exception e) {
                Log.v(TAG, e.getMessage());
            }
            radioPlayerServices.putExtra("url", url);
            radioPlayerServices.putExtra("radioName", name);
            try {
                context.startService(radioPlayerServices);
            } catch (Exception e) {
                Log.v(TAG, e.getMessage());
            }
        } else {
            Toast.makeText(context, "Please Connect your internet.", Toast.LENGTH_LONG).show();
        }
        notifyDataSetChanged();
    }

    private void stop() {
        try {
            //imgPlay.setImageResource(R.mipmap.play);
            context.stopService(radioPlayerServices);
        } catch (Exception e) {
            Log.v(TAG, e.getMessage());
        }
        notifyDataSetChanged();
    }

    public void filter(String etText) {
        etText = etText.toLowerCase(Locale.getDefault());
        Log.v(TAG, etText + "");
        alldata.clear();
        if (etText.length() == 0) {
            alldata.addAll(search);
        } else {
            for (int i = 0; i < search.size(); i++) {
                if (search.get(i).getName().toLowerCase(Locale.getDefault()).contains(etText)) {
                    alldata.add(search.get(i));
                }
            }
        }
        notifyDataSetChanged();
    }

    private int getPosition() {
        return sp.getInt("position", 0);
    }

    private void setPosition(int pos) {
        spEditor = sp.edit();
        spEditor.putInt("position", pos);
        spEditor.commit();
    }
}
